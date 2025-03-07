package com.kandaovr.meeting.kotlinDemo.audio

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioPlaybackCaptureConfiguration
import android.media.AudioRecord
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.MediaMuxer
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.util.Log
import android.util.MathUtils
import java.util.*

class ScreenInternalAudioRecorder(val outFile: String,
                                  val mMediaProjection: MediaProjection,
                                  val mMic: Boolean) {

    private val TAG = javaClass.simpleName
    private val TIMEOUT = 500
    private val MIC_VOLUME_SCALE = 1.4f
    private var mAudioRecord: AudioRecord? = null
    private var mAudioRecordMic: AudioRecord? = null
    private val mConfig: Config = Config()
    private var mThread: Thread? = null
    private var mCodec: MediaCodec? = null
    private var mPresentationTime: Long = 0
    private var mTotalBytes: Long = 0
    private var mMuxer: MediaMuxer? = null
    private var mStarted = false

    private var mTrackId = -1


    init {
        mMuxer = MediaMuxer(outFile, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
        setupSimple()
    }

    @SuppressLint("MissingPermission")
    private fun setupSimple() {
        val size = AudioRecord.getMinBufferSize(mConfig.sampleRate,
                                                mConfig.channelInMask,
                                                mConfig.encoding) * 2

        Log.d(TAG, "audio buffer size: $size")

        val format =
            AudioFormat.Builder().setEncoding(mConfig.encoding).setSampleRate(mConfig.sampleRate)
                .setChannelMask(mConfig.channelOutMask).build()

        val playbackConfig = AudioPlaybackCaptureConfiguration.Builder(mMediaProjection)
            .addMatchingUsage(AudioAttributes.USAGE_MEDIA)
            .addMatchingUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
            .addMatchingUsage(AudioAttributes.USAGE_UNKNOWN)
            .addMatchingUsage(AudioAttributes.USAGE_GAME).build()


        mAudioRecord = AudioRecord.Builder().setAudioFormat(format)
            .setAudioPlaybackCaptureConfig(playbackConfig).build()

        if (mMic) {
            mAudioRecordMic = AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION,
                                          mConfig.sampleRate,
                                          AudioFormat.CHANNEL_IN_MONO,
                                          mConfig.encoding,
                                          size)
        }

        mCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC)
        val medFormat =
            MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, mConfig.sampleRate, 1)
        medFormat.setInteger(MediaFormat.KEY_AAC_PROFILE,
                             MediaCodecInfo.CodecProfileLevel.AACObjectLC)
        medFormat.setInteger(MediaFormat.KEY_BIT_RATE, mConfig.bitRate)
        medFormat.setInteger(MediaFormat.KEY_PCM_ENCODING, mConfig.encoding)
        mCodec?.configure(medFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)

        mThread = Thread {
            var bufferInternal: ShortArray? = null
            var bufferMic: ShortArray? = null
            val buffer = ByteArray(size)
            if (mMic) {
                bufferInternal = ShortArray(size / 2)
                bufferMic = ShortArray(size / 2)
            }
            var readBytes = 0
            var readShortsInternal = 0
            var offsetShortsInternal = 0
            var readShortsMic = 0
            var offsetShortsMic = 0
            while (true) {
                if (mMic) {
                    readShortsInternal = mAudioRecord!!.read(bufferInternal,
                                                             offsetShortsInternal,
                                                             bufferInternal!!.size - offsetShortsInternal)
                    readShortsMic = mAudioRecordMic!!.read(bufferMic,
                                                           offsetShortsMic,
                                                           bufferMic!!.size - offsetShortsMic)

                    // if both error, end the recording
                    if (readShortsInternal < 0 && readShortsMic < 0) {
                        break
                    }

                    // if one has an errors, fill its buffer with zeros and assume it is mute
                    // with the same size as the other buffer
                    if (readShortsInternal < 0) {
                        readShortsInternal = readShortsMic
                        offsetShortsInternal = offsetShortsMic
                        Arrays.fill(bufferInternal, 0.toShort())
                    }
                    if (readShortsMic < 0) {
                        readShortsMic = readShortsInternal
                        offsetShortsMic = offsetShortsInternal
                        Arrays.fill(bufferMic, 0.toShort())
                    }

                    // Add offset (previous unmixed values) to the buffer
                    readShortsInternal += offsetShortsInternal
                    readShortsMic += offsetShortsMic
                    val minShorts = Math.min(readShortsInternal, readShortsMic)
                    readBytes = minShorts * 2

                    // modify the volume
                    // scale only mixed shorts
                    scaleValues(bufferMic, minShorts, MIC_VOLUME_SCALE)
                    // Mix the two buffers
                    addAndConvertBuffers(bufferInternal, bufferMic, buffer, minShorts)

                    // shift unmixed shorts to the beginning of the buffer
                    shiftToStart(bufferInternal, minShorts, offsetShortsInternal)
                    shiftToStart(bufferMic, minShorts, offsetShortsMic)

                    // reset the offset for the next loop
                    offsetShortsInternal = readShortsInternal - minShorts
                    offsetShortsMic = readShortsMic - minShorts
                } else {
                    readBytes = mAudioRecord!!.read(buffer, 0, buffer.size)
                }

                //exit the loop when at end of stream
                if (readBytes < 0) {
                    Log.e(TAG,
                          "read error $readBytes, shorts internal: $readShortsInternal, shorts mic: $readShortsMic")
                    break
                }
                encode(buffer, readBytes)
            }
            endStream()
        }

    }


    /**
     * moves all bits from start to end to the beginning of the array
     */
    private fun shiftToStart(target: ShortArray, start: Int, end: Int) {
        for (i in 0 until end - start) {
            target[i] = target[start + i]
        }
    }

    private fun scaleValues(buff: ShortArray, len: Int, scale: Float) {
        for (i in 0 until len) {
            val newValue = (buff[i] * scale).toInt()
            buff[i] =
                MathUtils.constrain(newValue, Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                    .toShort()
        }
    }

    private fun addAndConvertBuffers(src1: ShortArray,
                                     src2: ShortArray,
                                     dst: ByteArray,
                                     sizeShorts: Int) {
        for (i in 0 until sizeShorts) {
            var sum: Int
            sum = MathUtils.constrain(src1[i].toInt() + src2[i].toInt(),
                                      Short.MIN_VALUE.toInt(),
                                      Short.MAX_VALUE.toInt()).toShort().toInt()
            val byteIndex = i * 2
            dst[byteIndex] = (sum and 0xff).toByte()
            dst[byteIndex + 1] = (sum shr 8 and 0xff).toByte()
        }
    }

    private fun encode(buffer: ByteArray, readBytes: Int) {
        var readBytes = readBytes
        var offset = 0
        while (readBytes > 0) {
            var totalBytesRead = 0
            val bufferIndex = mCodec!!.dequeueInputBuffer(TIMEOUT.toLong())
            if (bufferIndex < 0) {
                writeOutput()
                return
            }
            val buff = mCodec!!.getInputBuffer(bufferIndex)
            buff.clear()
            val bufferSize = buff.capacity()
            val bytesToRead = if (readBytes > bufferSize) bufferSize else readBytes
            totalBytesRead += bytesToRead
            readBytes -= bytesToRead
            buff.put(buffer, offset, bytesToRead)
            offset += bytesToRead
            mCodec!!.queueInputBuffer(bufferIndex, 0, bytesToRead, mPresentationTime, 0)
            mTotalBytes += totalBytesRead.toLong()
            mPresentationTime = 1000000L * (mTotalBytes / 2) / mConfig.sampleRate
            writeOutput()
        }
    }

    private fun endStream() {
        val bufferIndex = mCodec!!.dequeueInputBuffer(TIMEOUT.toLong())
        mCodec!!.queueInputBuffer(bufferIndex,
                                  0,
                                  0,
                                  mPresentationTime,
                                  MediaCodec.BUFFER_FLAG_END_OF_STREAM)
        writeOutput()
    }

    private fun writeOutput() {
        while (true) {
            val bufferInfo = MediaCodec.BufferInfo()
            val bufferIndex = mCodec!!.dequeueOutputBuffer(bufferInfo,
                                                           TIMEOUT.toLong())
            if (bufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                mTrackId = mMuxer!!.addTrack(mCodec!!.outputFormat)
                mMuxer!!.start()
                continue
            }
            if (bufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                break
            }
            if (mTrackId < 0) return
            val buff = mCodec!!.getOutputBuffer(bufferIndex)
            if (!(bufferInfo.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG != 0 && bufferInfo.size != 0)) {
                mMuxer!!.writeSampleData(mTrackId, buff, bufferInfo)
            }
            mCodec!!.releaseOutputBuffer(bufferIndex, false)
        }
    }

    /**
     * start recording
     * @throws IllegalStateException if recording fails to initialize
     */
    @Synchronized
    @Throws(IllegalStateException::class)
    fun start() {
        if (mStarted) {
            checkNotNull(mThread) { "Recording stopped and can't restart (single use)" }
            throw IllegalStateException("Recording already started")
        }
        mStarted = true
        mAudioRecord!!.startRecording()
        if (mMic) mAudioRecordMic!!.startRecording()
        Log.d(TAG, "channel count " + mAudioRecord!!.channelCount)
        mCodec!!.start()
        check(mAudioRecord!!.recordingState == AudioRecord.RECORDSTATE_RECORDING) { "Audio recording failed to start" }
        mThread!!.start()
    }

    /**
     * end recording
     */
    fun end() {
        mAudioRecord!!.stop()
        if (mMic) {
            mAudioRecordMic!!.stop()
        }
        mAudioRecord!!.release()
        if (mMic) {
            mAudioRecordMic!!.release()
        }
        try {
            mThread!!.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        mCodec!!.stop()
        mCodec!!.release()
        mMuxer!!.stop()
        mMuxer!!.release()
        mThread = null
    }


    /**
     * Audio recoding configuration
     */
    class Config {
        var channelOutMask = AudioFormat.CHANNEL_OUT_MONO
        var channelInMask = AudioFormat.CHANNEL_IN_MONO
        var encoding = AudioFormat.ENCODING_PCM_16BIT
        var sampleRate = 44100
        var bitRate = 196000
        var bufferSizeBytes = 1 shl 17
        var privileged = true
        var legacy_app_looback = false
        override fun toString(): String {
            return "channelMask=$channelOutMask,encoding=$encoding,sampleRate=$sampleRate,bufferSize=$bufferSizeBytes,privileged=$privileged,legacy app looback=$legacy_app_looback"
        }
    }


}