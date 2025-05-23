plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
    id("maven-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(gradleApi())
    implementation(kotlin("stdlib"))
    implementation(libs.bundles.nav.plugin)
    implementation(project(":plugin-navigation-runtime"))
//    implementation("com.jiali.android:plugin-navigation-runtime:1.0-SNAPSHOT")
}

publishing {
    publications {
        create<MavenPublication>("plugin-navigation") {
            from(components["java"])
            groupId = "com.cat.android"
            artifactId = "plugin-navigation"
            version = "1.0.1"
        }
    }
    repositories {
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
        }
        maven {
            isAllowInsecureProtocol = true
            credentials {
                username = "admin"
                password = "123456"
            }
            url = uri("http://localhost:8081/repository/jiali/")
        }*/
    }
}