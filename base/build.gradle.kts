plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}


//kapt {
//    generateStubs = true
//}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        // 从 gradle.properties 读取值
        val logTag = project.property("APP_LOG_TAG") as? String ?: "KD_DEBUG_MEETING_COMMON"
        buildConfigField("String", "LOG_TAG", logTag)
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    @Suppress("UnstableApiUsage") // 抑制实验性 API 警告
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // 严格按照 libs.versions.toml 中的命名引用（- 转换为 .）
    implementation(libs.kotlin.stdlib)                  // 对应 toml 中的 kotlin-stdlib
    implementation(libs.kotlinx.coroutines.android)     // 对应 toml 中的 kotlinx-coroutines-android
    
    // Add annotation dependency
    implementation(libs.androidx.annotation)


    // 协程（使用版本目录中的定义）
    api(libs.kotlinx.coroutines.core)                   // 对应 toml 中的 kotlinx-coroutines-core

    // ViewModel 与 Lifecycle（可优化为版本目录引用）
    api(libs.lifecycle.viewmodel.ktx)
    api(libs.lifecycle.runtime.ktx)
    kapt(libs.lifecycle.compiler)

    // RxLifecycle
    api(libs.rxlifecycle.components)

    // 网络相关（使用版本目录中的定义）
    api(libs.retrofit)
    api(libs.retrofit.converter.gson)
    api(libs.squareup.okhttp)                           // 对应 toml 中的 squareup-okhttp
    api(libs.squareup.okhttp.logging.interceptor)       // 对应 toml 中的 squareup-okhttp-logging-interceptor

    // 日志（使用版本目录中的定义）
    api(libs.orhanobut.logger)                          // 对应 toml 中的 orhanobut-logger
}