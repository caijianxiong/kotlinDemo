#include <jni.h>
#include <string>
#include <android/log.h>]
#include <pthread.h>
#include <unistd.h> // 添加此头文件以使用 sleep() 函数

#define LOG_TAG "JNI_Advanced"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

/**
 * =====================================================================================
 * JNI 核心要点与最佳实践 (JNI Core Concepts & Best Practices)
 * =====================================================================================
 *
 * 1. 线程模型 (Threading Model)
 *    - **同步调用**: 当 Java 线程调用一个 JNI 方法时，该 JNI 方法会在同一个 Java 线程上同步执行。JNI 本身不会自动创建或切换线程。
 *    - **JNIEnv* 线程局部性**: `JNIEnv*` 指针是线程本地的，它不能在线程之间共享。每个需要与 JVM 交互的线程都必须有自己的 `JNIEnv*`。
 *    - **JavaVM* 全局性**: `JavaVM*` 指针是全局的，代表整个 JVM 实例。它可以在线程间安全地共享，并用于在任何线程中获取该线程专属的 `JNIEnv*`。
 *
 * 2. C++ 原生线程回调 Java (Native Threads Calling Java)
 *    - **附加到 JVM**: 如果你在 JNI 中创建了一个新的 C++ 线程 (如使用 pthread)，这个线程在默认情况下不是 JVM 的一部分。
 *      为了能回调 Java 方法，它必须首先调用 `g_vm->AttachCurrentThread(&env, nullptr)` 来附加到 JVM 并获取一个合法的 `JNIEnv*`。
 *    - **从 JVM 分离**: 当线程任务完成时，应该调用 `g_vm->DetachCurrentThread()` 来释放相关资源。
 *
 * 3. 引用管理：局部引用 vs 全局引用 (Reference Management: Local vs. Global)
 *    - **局部引用 (Local References)**: JNI 函数中创建的大多数对象 (如 `NewStringUTF`) 都是局部引用。它们只在当前 JNI 方法执行期间有效，方法返回后会被 JVM 自动释放。
 *      **陷阱**: 在循环中创建大量局部引用可能耗尽内存，需要使用 `env->DeleteLocalRef()` 手动释放。
 *    - **全局引用 (Global References)**: 如果你想跨线程或跨 JNI 调用持有一个 Java 对象（如此处的 `g_cache.global_handler_ref`），
 *      必须使用 `env->NewGlobalRef()` 将其创建为全局引用。全局引用不会被 GC 回收。
 *    - **释放全局引用**: 全局引用必须在不再需要时手动调用 `env->DeleteGlobalRef()` 来释放，否则会造成内存泄漏。通常在 `JNI_OnUnload` 或专门的 cleanup 方法中执行。
 *    -** 弱全局引用（WeakGlobalRef）
 *      不阻止 GC 回收，需先通过 IsSameObject(env, weakRef, NULL) 判断是否存活
 *
 * 4. 异常处理 (Exception Handling) - 极易出错！
 *    - **捕获从 Java 抛出的异常**: 如果 C++ 调用的 Java 方法抛出了异常，JNI 不会立即中断，但会设置一个 "pending exception"。
 *      C++ 代码必须调用 `env->ExceptionCheck()` 来检测它，然后通过 `env->ExceptionDescribe()` 打印，最后调用 `env->ExceptionClear()` 来清除它，否则 App 会在返回 Java 层后崩溃。
 *    - **处理 JNI 函数自身的错误**: 当调用 JNI 函数失败时（例如，`GetMethodID` 找不到方法），也会设置一个 "pending exception"。处理方式同上。
 *      **核心原则**: 在调用任何可能失败的 JNI 函数后，都应该检查异常。
 */

// 1. 全局 JavaVM 指针，用于在任何线程中获取 JNIEnv
static JavaVM *g_vm = nullptr;

// 2. 用于缓存回调类的方法ID，避免重复查找，提升性能。
struct CallbackCache {
    jobject global_handler_ref; // 全局引用，持有 Java 回调对象实例
    jmethodID on_progress_mid;
    jmethodID on_complete_mid;
    jmethodID on_exception_mid;
};
static CallbackCache g_cache;

// 3. 线程任务参数
struct ThreadArgs {
    int rounds;
};

// =====================================================================================
// 核心 JNI 函数 (Core JNI Functions)
// =====================================================================================

/**
 * C++ 子线程的执行函数
 */
void *native_thread_start(void *args) {
    JNIEnv *env = nullptr;
    bool attached = false;

    // 1. 将当前 C++ 线程附加到 JVM，并获取 JNIEnv
    if (g_vm->AttachCurrentThread(&env, nullptr) == JNI_OK) {
        attached = true;
    } else {
        LOGD("Error: Failed to attach thread to JVM");
        return nullptr;
    }

    ThreadArgs *thread_args = (ThreadArgs *) args;

    // 模拟耗时任务，并回调 Java 更新进度
    for (int i = 1; i <= thread_args->rounds; ++i) {
        sleep(1); // 模拟耗时
        int progress = (int) (((float) i / thread_args->rounds) * 100);

        // 2. 在子线程中调用 Java 方法
        env->CallVoidMethod(g_cache.global_handler_ref, g_cache.on_progress_mid, progress);
    }

    // --- 演示捕获 JNI 异常 (非 Java 抛出的异常) ---
    LOGD("Intentionally causing a JNI exception by finding a non-existent method...");
    jclass handler_class = env->GetObjectClass(g_cache.global_handler_ref);
    jmethodID non_existent_mid = env->GetMethodID(handler_class, "nonExistentMethod", "()V");

    // 最佳实践: 在调用可能失败的 JNI 函数后立即检查异常
    if (env->ExceptionCheck()) {
        LOGD("A JNI exception occurred (as expected) when calling GetMethodID.");
        env->ExceptionDescribe(); // 将异常信息打印到 logcat，非常便于调试
        env->ExceptionClear();    // 清除异常，防止 APP 崩溃，并允许后续的 JNI 调用继续
    } else {
        // 这段代码不应该被执行
        if (non_existent_mid != nullptr) {
            env->CallVoidMethod(g_cache.global_handler_ref, non_existent_mid);
        }
    }

    // 任务完成回调
    jstring result_str = env->NewStringUTF("All rounds completed successfully!");
    env->CallVoidMethod(g_cache.global_handler_ref, g_cache.on_complete_mid, result_str);
    env->DeleteLocalRef(result_str);

    // --- 演示处理由 Java 抛出的异常 ---
    env->CallVoidMethod(g_cache.global_handler_ref, g_cache.on_exception_mid);
    if(env->ExceptionCheck()) {
        LOGD("An exception was thrown from Java code. Clearing it now.");
        env->ExceptionDescribe(); 
        env->ExceptionClear();    
    }

    delete thread_args;

    // 4. 从 JVM 分离当前线程
    if (attached) {
        g_vm->DetachCurrentThread();
    }

    pthread_exit(nullptr);
}

/**
 * JNI 入口：初始化并启动一个 C++ 线程来执行耗时任务
 */
JNIEXPORT void JNICALL startNativeThread(JNIEnv *env, jobject thiz, jobject handler) {
    // --- 缓存 Method IDs ---
    LOGD("startNativeThread JNI enter.");
    jclass handler_class = env->GetObjectClass(handler);
    g_cache.on_progress_mid = env->GetMethodID(handler_class, "onProgress", "(I)V");
    g_cache.on_complete_mid = env->GetMethodID(handler_class, "onComplete", "(Ljava/lang/String;)V");
    g_cache.on_exception_mid = env->GetMethodID(handler_class, "methodThatThrowsException", "()V");

    // --- 创建全局引用 ---
    if (g_cache.global_handler_ref != nullptr) {
        env->DeleteGlobalRef(g_cache.global_handler_ref);
    }
    g_cache.global_handler_ref = env->NewGlobalRef(handler);
    if (g_cache.global_handler_ref == nullptr) {
        LOGD("Error: Failed to create global reference for handler.");
        return;
    }

    // --- 启动 C++ 线程 ---
    pthread_t thread;
    ThreadArgs *args = new ThreadArgs();
    args->rounds = 2; // 缩短演示时间

    int result = pthread_create(&thread, nullptr, native_thread_start, args);
    if (result != 0) {
        LOGD("Error: Failed to create native thread.");
    }
}

/**
 * JNI 入口：用于清理资源
 */
JNIEXPORT void JNICALL cleanup(JNIEnv *env, jobject thiz) {
    if (g_cache.global_handler_ref != nullptr) {
        LOGD("Cleaning up JNI global reference.");
        env->DeleteGlobalRef(g_cache.global_handler_ref);
        g_cache.global_handler_ref = nullptr;
    }
}

// ... JNI_OnLoad and JNI_OnUnload (内容省略) ...

#define JAVA_CLASS "com/kandaovr/meeting/kotlinDemo/jni/NativeLib"

static const JNINativeMethod gMethods[] = {
    {
        "startNativeThread",
        "(Lcom/kandaovr/meeting/kotlinDemo/jni/JniCallbackHandler;)V",
        (void *) startNativeThread
    },
    {
        "cleanup",
        "()V",
        (void *) cleanup
    }
};

/**
 * 当虚拟机（VM）加载 Native 库（.so 文件）时调用，用于初始化
 */
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    // 保存全局 JavaVM 指针
    g_vm = vm;

    JNIEnv *env = nullptr;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }

    jclass javaClass = env->FindClass(JAVA_CLASS);
    if (javaClass == nullptr) { return JNI_ERR; }

    if (env->RegisterNatives(javaClass, gMethods, sizeof(gMethods) / sizeof(gMethods[0])) < 0) {
        return JNI_ERR;
    }

    LOGD("JNI_OnLoad successful.");
    return JNI_VERSION_1_6;
}

/**
 * 当虚拟机（VM）卸载 Native 库时调用，用于释放 Native 资源
 * 当加载 Native 库的 ClassLoader 被 GC（垃圾回收） 回收时
 * ，VM 会卸载该 ClassLoader 加载的所有 Native 库，此时触发 JNI_OnUnload。
 * @param vm
 * @param reserved
 */
JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved) {
    JNIEnv *env = nullptr;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) == JNI_OK) {
        if (g_cache.global_handler_ref != nullptr) {
            env->DeleteGlobalRef(g_cache.global_handler_ref);
            g_cache.global_handler_ref = nullptr;
        }
    }
    LOGD("JNI_OnUnload called.");
}
