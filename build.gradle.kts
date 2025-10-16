// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
}

//buildscript {
//    val kotlinVersion by extra("1.6.21")
//
//    repositories {
//        maven { url = uri("https://maven.aliyun.com/repository/google") }
//        maven { url = uri("https://maven.aliyun.com/repository/public") }
//        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
//        google()
//        mavenCentral()
//    }
//
//    dependencies {
//        classpath("com.android.tools.build:gradle:7.0.4")
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
//    }
//}
//
//allprojects {
//    repositories {
//        maven { url = uri("https://maven.aliyun.com/repository/google") }
//        maven { url = uri("https://maven.aliyun.com/repository/public") }
//        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
//        google()
//        mavenCentral()
//    }
//}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}