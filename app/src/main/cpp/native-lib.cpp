#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

Java_com_kandaovr_meeting_kotlinDemo_MainActivity_stringFromJNI(JNIEnv *env, jobject thiz) {
    // TODO: implement stringFromJNI()
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}