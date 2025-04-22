plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
    id("maven-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

publishing {
    publications {
        create<MavenPublication>("plugin-navigation-runtime") {
            from(components["java"])
            groupId = "com.cat.android"
            artifactId = "plugin-navigation-runtime"
            version = "1.0.0"
        }
    }
    repositories {
        // 本地仓库
        /*maven {
            url = uri("../repo")
        }*/
        maven {
            isAllowInsecureProtocol = true
            credentials {
                username = "6531f582e7be53b98df4b4c5"
                password = "dr9hUm4uSc1q"
            }
            url = uri("https://packages.aliyun.com/6531f58acd1f19fc10797d87/maven/2426451-release-caz7rf")
        }
        /*maven {
            isAllowInsecureProtocol = true
            credentials {
                username = "admin"
                password = "jl123456"
            }
            url = uri("http://192.168.101.123:8081/repository/jiali/")
        }*/
    }
}