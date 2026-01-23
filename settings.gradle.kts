// 注释：定义插件的远程仓库
pluginManagement {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        google()
        mavenCentral()
        // 注释：Gradle 插件门户网站，plugins {} 块会默认从这里查找插件
        gradlePluginPortal()
    }
}

// 注释：定义依赖库的远程仓库
dependencyResolutionManagement {
    // 注释：所有模块都将默认使用这里定义的仓库，模块内无需再写 repositories {}
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        google()
        mavenCentral()
    }
}

rootProject.name = "kotlinDemo"


include(":app")
include(":base")
include(":feature_user")
include(":mvi")
