plugins {
    alias(libs.plugins.android.app)
    alias(libs.plugins.kotlin.gradle)
    alias(libs.plugins.kotlin.kapt)
    id("maven-publish") // 这个是发布本地用的
    id("nav-plugin-plus")
}

NavFilePath {
    navRegistryPath = "app${File.separator}src${File.separator}main${File.separator}java${File.separator}"
    navRegistryPackageName = "com.cat.android.lib_plugin_demo"
}
android {
    namespace = "com.cat.android.lib_plugin_demo"
    compileSdk = 34

    defaultConfig {
//        applicationId = "com.jiali.android.lib_plugin_demo"
        minSdk = 24
        targetSdk = 34
//        versionCode = 1
//        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.bundles.app)
    implementation(libs.bundles.kt)
    implementation(libs.bundles.jetpack)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(project(":plugin-navigation-runtime"))
    kapt(libs.moshi.codegen)
}