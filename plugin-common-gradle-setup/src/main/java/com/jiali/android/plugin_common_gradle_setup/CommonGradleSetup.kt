package com.jiali.android.plugin_common_gradle_setup

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

class CommonGradleSetup : Plugin<Project> {

    override fun apply(target: Project) {
        try {
            (target.extensions.getByName("android") as BaseAppModuleExtension).apply {
                handleApp(target, this)
            }
        } catch (e: Exception) {
            (target.extensions.getByName("android") as LibraryExtension).apply {
                handleLibrary(target, this)
            }
        }
    }

    private fun handleApp(target: Project, extension: BaseAppModuleExtension) {
        extension.apply {
            compileSdk = 34 // 不能低于34，不知道为啥低于的话发布的时候编译失败
            defaultConfig {
                minSdk = 24
                targetSdk = 33

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }

            setupKotlinOptions(this)

            buildTypes {
                release {
                    isMinifyEnabled = false
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                }
            }

            buildFeatures {
                dataBinding = true
            }

            target.configurations.all {
                resolutionStrategy.cacheChangingModulesFor(0, "minutes")
            }
        }
        target.dependencies.apply {
            add("implementation", CatalogUtil.getCatalog("moshi", CatalogUtil.Type.LIBRARY))
            add("implementation", CatalogUtil.getCatalog("moshi-kotlin", CatalogUtil.Type.LIBRARY))
            add("kapt", CatalogUtil.getCatalog("moshi-codegen", CatalogUtil.Type.LIBRARY))
            add("kapt", CatalogUtil.getCatalog("auto-service", CatalogUtil.Type.LIBRARY))
        }
    }

    private fun handleLibrary(target: Project, extension: LibraryExtension) {
        extension.apply {
            compileSdk = 34
            defaultConfig {
                minSdk = 24

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }

            setupKotlinOptions(this)

            buildTypes {
                release {
                    isMinifyEnabled = false
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                }
            }

            buildFeatures {
                dataBinding = true
            }

            target.configurations.all {
                resolutionStrategy.cacheChangingModulesFor(0, "minutes")
            }
        }
    }

    private fun setupKotlinOptions(e: BaseExtension) {
        val extensions = (e as ExtensionAware).extensions
        val kotlinOptions = extensions.getByName("kotlinOptions") as KotlinJvmOptions
        kotlinOptions.apply {
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }

}