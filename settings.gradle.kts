pluginManagement {
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
    repositories {
        google()
        mavenCentral()
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
