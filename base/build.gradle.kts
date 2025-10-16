plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}


kapt {
    generateStubs = true
}

android {
    compileSdk = 31

    buildFeatures {
        dataBinding = true
    }

    defaultConfig {
        // 从 gradle.properties 读取值
        val logTag = project.property("APP_LOG_TAG") as? String ?: "KD_DEBUG_MEETING_COMMON"
        buildConfigField("String", "LOG_TAG", logTag)

        minSdk = 29
        targetSdk = 31

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    api(libs.androidx.appcompat)                        // 对应 toml 中的 androidx-appcompat
    api(libs.google.material)                           // 对应 toml 中的 google-material
    api(libs.androidx.constraintlayout)                 // 对应 toml 中的 androidx-constraintlayout

    // 协程（使用版本目录中的定义）
    api(libs.kotlinx.coroutines.core)                   // 对应 toml 中的 kotlinx-coroutines-core

    // ViewModel 与 Lifecycle（可优化为版本目录引用）
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.5.0")
    kapt("androidx.lifecycle:lifecycle-compiler:2.3.1")

    // RxLifecycle
    api("com.trello.rxlifecycle2:rxlifecycle-components:2.2.2")

    // 网络相关（使用版本目录中的定义）
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")
    api(libs.squareup.okhttp)                           // 对应 toml 中的 squareup-okhttp
    api(libs.squareup.okhttp.logging.interceptor)       // 对应 toml 中的 squareup-okhttp-logging-interceptor

    // 日志（使用版本目录中的定义）
    api(libs.orhanobut.logger)                          // 对应 toml 中的 orhanobut-logger
}