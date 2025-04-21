// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    configurations.all {
        resolutionStrategy.cacheDynamicVersionsFor(0, TimeUnit.SECONDS)
        resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
    }
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io/") }
        maven {
            credentials {
                username = "6531f582e7be53b98df4b4c5"
                password = "dr9hUm4uSc1q"
            }
            url = uri("https://packages.aliyun.com/6531f58acd1f19fc10797d87/maven/2426451-release-caz7rf")
        }
        maven {
            url = uri("https://storage.googleapis.com/r8-releases/raw") // 加了的话makeProject就不会报错了
        }
    }
    dependencies {
        classpath("com.cat.android:plugin-navigation-plus:1.0.0")
        classpath("com.android.tools:r8:8.2.24") // 加了的话makeProject就不会报错了
    }
}


plugins {
    alias(libs.plugins.android.app) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.gradle) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.gradle.publish) apply false
}
