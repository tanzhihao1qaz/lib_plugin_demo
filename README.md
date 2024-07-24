# lib_plugin_demo

- # 整理这个模块之间相同的gradle配置，以及将version catalog插件化

- ## 插件版本信息
| gradle  | android gradle  | kotlin-gradle  |
|:----------|:----------|:----------|
| 7.6   | 7.4.0    | 1.8.10    |

- ## 其他版本信息
| compileSdk  | minSdk  | targetSdk |
|:----------|:----------|:----------|
| 34    | 24    | 33   |

- ## VersionCatalog插件使用方式
1. setting.gradle先导入version catalog插件

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
		// 设置仓库地址
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://192.168.101.123:8081/repository/jiali/")
            credentials {
                username = "admin"
                password = "jl123456"
            }
        }
    }
	// 加载toml文件，这里myLibs名字可以随便起
    versionCatalogs {
        create("myLibs") {
            from("com.jiali.android:catalog:1.0-SNAPSHOT")
        }
    }
}

```
2. 同步完之后，就可以正常使用，如：
```
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.jetbrains.kotlin.android)
	// 这里的myLibs就是前面导入toml文件的名字
    alias(myLibs.plugins.androidApplication)
    alias(myLibs.plugins.jetbrainsKotlinAndroid)
    id("common-gradle-setup")
}

android {
    namespace = "com.jiali.android.lib_plugin_demo"

    defaultConfig {
        applicationId = "com.jiali.android.lib_plugin_demo"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(myLibs.core.ktx)
    implementation(myLibs.appcompat)
    implementation(myLibs.material)
    implementation(myLibs.constraint.layout)
    testImplementation(myLibs.junit)
    androidTestImplementation(myLibs.ext.junit)
    androidTestImplementation(myLibs.espresso.core)
}
```

- ## common-gradle-plugin插件使用
1. 根目录下导入插件
```
buildscript {
    repositories {
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://192.168.101.123:8081/repository/jiali/")
            credentials {
                username = "admin"
                password = "jl123456"
            }
        }
    }
    dependencies {
        // 由于object的gradle插件地址有":"，versionLogcat不允许地址上有":"，所以只能用原来的方式集成
        classpath("com.jiali.android:common-gradle-setup:1.0-SNAPSHOT")
    }
}
```
2. 在需要用到的模块里添加此插件既可以
```
plugins {
    alias(myLibs.plugins.androidApplication)
    alias(myLibs.plugins.jetbrainsKotlinAndroid)
    id("common-gradle-setup") // 此处
}

// 使用了此插件的效果等同于自动完成了以下配置
android {
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

            kotlinOptions {
				jvmTarget = 11
			}

            buildTypes {
                release {
                    isMinifyEnabled = false
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                }
            }

            buildFeatures {
                dataBinding = true
            }

            configurations.all {
                resolutionStrategy.cacheChangingModulesFor(0, "minutes")
            }
        }

```
