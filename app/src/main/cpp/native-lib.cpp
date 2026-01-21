#include <jni.h>
#include <string>

/**
 * 这是一个 JNI 静态注册的函数。
 * 它的命名遵循了严格的规则: Java_包名_类名_方法名
 * 注意：类名中的 `$` 用于分隔伴生对象或内部类，但在我们的例子中 NativeLib 是一个 object，
 * 其 JNI 路径通常是 "包名/类名"，如果它是伴生对象则可能是 "包名/外部类名$内部类名"。
 * 在 object 的情况下，可以直接使用类名。
 */
extern "C"
JNIEXPORT jstring JNICALL
Java_com_kandaovr_meeting_kotlinDemo_jni_NativeLib_stringFromJNI(JNIEnv *env, jobject thiz) {
    std::string hello = "Hello from C++ (Static Registration in NativeLib)";
    return env->NewStringUTF(hello.c_str());
}
