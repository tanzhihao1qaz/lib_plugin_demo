pluginManagement {
    /*repositories {
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://192.168.101.123:8081/repository/android-repos-google/")
            credentials {
                username = "admin"
                password = "jl123456"
            }
        }
    }*/
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    /*repositories {
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://192.168.101.123:8081/repository/android-repos-google/")
            credentials {
                username = "admin"
                password = "jl123456"
            }
        }
    }*/
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io/") }
    }

   /* versionCatalogs {
        create("myLibs") {
            from("com.jiali.android:catalog:1.1.6-SNAPSHOT")
        }
    }*/
}

rootProject.name = "lib_plugin_demo"
include(":app")
include(":plugin-common-gradle-setup")
include(":plugin-version-catalog")
include(":plugin-navigation")
include(":plugin-navigation-runtime")
