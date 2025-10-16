package com.cjx.kotlin.base.log;


import android.annotation.NonNull;
import android.annotation.Nullable;
import android.text.TextUtils;

import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogStrategy;
import com.orhanobut.logger.LogcatLogStrategy;
import com.orhanobut.logger.Logger;


/**
 * Draws borders around the given log message along with additional information such as :
 *
 * <ul>
 *   <li>Thread information</li>
 *   <li>Method stack trace</li>
 * </ul>
 *
 * <pre>
 *  ┌──────────────────────────
 *  │ Method stack history
 *  ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
 *  │ Thread information
 *  ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
 *  │ Log message
 *  └──────────────────────────
 * </pre>
 */
public class MyPrettyFormatStrategy implements FormatStrategy {

    /**
     * Android's max limit for a log entry is ~4076 bytes,
     * so 4000 bytes is used as chunk size since default charset
     * is UTF-8
     */
    private static final int CHUNK_SIZE = 2048;

    /**
     * The minimum stack trace index, starts at this class after two native calls.
     */
    private static final int MIN_STACK_OFFSET = 5;

    /**
     * Drawing toolbox
     */
    private static final char TOP_LEFT_CORNER = '┌';
    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final char HORIZONTAL_LINE = '│';
    private static final String TOP_BORDER = "──────────────────── StackTrace ────────────────────────────────────────────────────────>>>";
    private static final String BOTTOM_BORDER = "──────────────────── StackTrace ────────────────────────────────────────────────────────<<<";

    private final int methodCount;
    private final int methodOffset;
    private final boolean showThreadInfo;
    @NonNull
    private final LogStrategy logStrategy;
    @Nullable
    private final String tag;
    private final String lineSeparator = System.getProperty("line.separator");

    private MyPrettyFormatStrategy(@NonNull Builder builder) {
        checkNotNull(builder);

        methodCount = builder.methodCount;
        methodOffset = builder.methodOffset;
        showThreadInfo = builder.showThreadInfo;
        logStrategy = builder.logStrategy;
        tag = builder.tag;
    }

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public void log(int priority, @Nullable String onceOnlyTag, @NonNull String message) {
        checkNotNull(message);

        String tag = formatTag(onceOnlyTag);

        String footer = generateFooter(methodCount);

        //get bytes of message with system's default charset (which is UTF-8 for Android)
        int length = message.getBytes().length;

        if (length <= CHUNK_SIZE) {
            logContent(priority, tag, footer, message);
            return;
        }
        String[] strings = message.split(checkNotNull(lineSeparator));
        int i = 0;
        StringBuilder sb = new StringBuilder();
        int lenWaitLog = 0;
        int loggedLen = 0;
        int separatorLen = lineSeparator.getBytes().length;
        while (i < strings.length) {
            byte[] currentLine = strings[i].getBytes();
            int lenCurrentLine = currentLine.length;
            boolean addSeparator = (i != strings.length - 1);
            if (addSeparator) {
                lenCurrentLine += separatorLen;
            }
            if (lenCurrentLine <= CHUNK_SIZE) {
                if (lenWaitLog + lenCurrentLine <= CHUNK_SIZE) {
                    sb.append(strings[i]);
                } else {
                    logContent(priority, tag, loggedLen + lenWaitLog >= length ? footer : null, sb.toString());
                    loggedLen += lenWaitLog;
                    lenWaitLog = 0;
                    sb = new StringBuilder(strings[i]);
                }
                if (addSeparator) {
                    sb.append(lineSeparator);
                }
                lenWaitLog += lenCurrentLine;
            } else {
                if (lenWaitLog > 0) {
                    logContent(priority, tag, null, sb.toString());
                    sb = new StringBuilder();
                    loggedLen += lenWaitLog;
                    lenWaitLog = 0;
                }
                for (int j = 0; j < lenCurrentLine; j += CHUNK_SIZE) {
                    int count = Math.min(lenCurrentLine - j, CHUNK_SIZE);
                    logContent(priority, tag, loggedLen + count >= length ? footer : null,
                            new String(currentLine, j, count));
                    loggedLen += count;
                }
            }
            i++;
        }
        if (lenWaitLog > 0) {
            logContent(priority, tag, footer, sb.toString());
        }
    }

    private String generateFooter(int methodCount) {
        if (methodCount <= 0) {
            return null;
        }
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int stackOffset = getStackOffset(trace) + methodOffset;

        //corresponding method count with the current stack may exceeds the stack trace. Trims the count
        if (methodCount + stackOffset > trace.length) {
            methodCount = trace.length - stackOffset - 1;
        }

        StringBuilder builder = new StringBuilder()
                .append(TOP_LEFT_CORNER)
                .append(TOP_BORDER);

        //add thread info
        if (showThreadInfo) {
            builder.append(lineSeparator)
                    .append(HORIZONTAL_LINE)
                    .append(" ")
                    .append("[ Thread:")
                    .append(Thread.currentThread().getName())
                    .append(" ]");
        }

        builder.append(lineSeparator);

        for (int i = 0; i < methodCount; i++) {
            int stackIndex = stackOffset + i;
            if (stackIndex >= trace.length) {
                break;
            }
            builder.append(HORIZONTAL_LINE)
                    .append(" ")
                    .append(getSimpleClassName(trace[stackIndex].getClassName()))
                    .append(".")
                    .append(trace[stackIndex].getMethodName())
                    .append("(")
                    .append(trace[stackIndex].getFileName())
                    .append(":")
                    .append(trace[stackIndex].getLineNumber())
                    .append(")")
                    .append(lineSeparator);
        }
        builder.append(BOTTOM_LEFT_CORNER).append(BOTTOM_BORDER);
        return builder.toString();
    }

    private void logContent(int logType, @Nullable String tag, String footer, @NonNull String chunk) {
        checkNotNull(chunk);
        if (!TextUtils.isEmpty(footer)) {
            chunk = chunk + lineSeparator + footer;
        }
        logChunk(logType, tag, chunk);
    }

    private void logChunk(int priority, @Nullable String tag, @NonNull String chunk) {
        checkNotNull(chunk);

        logStrategy.log(priority, tag, chunk);
    }

    private String getSimpleClassName(@NonNull String name) {
        checkNotNull(name);

        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    /**
     * Determines the starting index of the stack trace, after method calls made by this class.
     *
     * @param trace the stack trace
     * @return the stack offset
     */
    private int getStackOffset(@NonNull StackTraceElement[] trace) {
        checkNotNull(trace);

        for (int i = MIN_STACK_OFFSET; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (!name.contains("LoggerPrinter")
                    && !name.equals(Logger.class.getName())
                    && !name.equals(ClzLogger.class.getName())) {
                return i;
            }
        }
        return -1;
    }

    @Nullable
    private String formatTag(@Nullable String tag) {
        if (!TextUtils.isEmpty(tag) && !TextUtils.equals(this.tag, tag)) {
            return this.tag + "-" + tag;
        }
        return this.tag;
    }

    public static class Builder {
        int methodCount = 2;
        int methodOffset = 0;
        boolean showThreadInfo = true;
        @Nullable
        LogStrategy logStrategy;
        @Nullable
        String tag = "PRETTY_LOGGER";

        private Builder() {
        }

        @NonNull
        public Builder methodCount(int val) {
            methodCount = val;
            return this;
        }

        @NonNull
        public Builder methodOffset(int val) {
            methodOffset = val;
            return this;
        }

        @NonNull
        public Builder showThreadInfo(boolean val) {
            showThreadInfo = val;
            return this;
        }

        @NonNull
        public Builder logStrategy(@Nullable LogStrategy val) {
            logStrategy = val;
            return this;
        }

        @NonNull
        public Builder tag(@Nullable String tag) {
            this.tag = tag;
            return this;
        }

        @NonNull
        public MyPrettyFormatStrategy build() {
            if (logStrategy == null) {
                logStrategy = new LogcatLogStrategy();
            }
            return new MyPrettyFormatStrategy(this);
        }
    }

    public @NonNull
    static <T> T checkNotNull(@Nullable final T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }
}