pluginManagement {
    repositories {
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://192.168.101.123:8081/repository/android-repos-google/")
            credentials {
                username = "admin"
                password = "jl123456"
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        repositories {
            maven {
                isAllowInsecureProtocol = true
                url = uri("http://192.168.101.123:8081/repository/android-repos-google/")
                credentials {
                    username = "admin"
                    password = "jl123456"
                }
            }
        }
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://192.168.101.123:8081/repository/jiali/")
            credentials {
                username = "admin"
                password = "jl123456"
            }
        }
    }
}

rootProject.name = "lib_plugin_demo"
include(":app")
include(":plugin-common-gradle-setup")
include(":plugin-version-catalog")
include(":plugin-navigation")
include(":plugin-navigation-runtime")
