// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    configurations.all {
        resolutionStrategy.cacheDynamicVersionsFor(0, TimeUnit.SECONDS)
        resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
    }
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
//    dependencies {
//        classpath("com.github.tanzhihao1qaz.lib_plugin_demo:catalog:1.0")
//    }
}
plugins {
    alias(libs.plugins.android.app) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.gradle) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.gradle.publish) apply false
}
