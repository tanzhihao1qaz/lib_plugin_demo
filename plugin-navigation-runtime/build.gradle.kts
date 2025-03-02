plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
    id("maven-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

publishing {
    publications {
        create<MavenPublication>("plugin-navigation-runtime") {
            from(components["java"])
            groupId = "com.jiali.android"
            artifactId = "plugin-navigation-runtime"
            version = "1.0.0-SNAPSHOT"
        }
    }
    /*repositories {
        maven {
            isAllowInsecureProtocol = true
            credentials {
                username = "admin"
                password = "jl123456"
            }
            url = uri("http://192.168.101.123:8081/repository/jiali/")
        }
        maven {
            isAllowInsecureProtocol = true
            credentials {
                username = "admin"
                password = "123456"
            }
            url = uri("http://localhost:8081/repository/jiali/")
        }
    }*/
}