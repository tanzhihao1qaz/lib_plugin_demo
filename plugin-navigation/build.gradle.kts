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
            groupId = "com.jiali.android"
            artifactId = "plugin-navigation"
            version = "1.1-SNAPSHOT"
        }
    }
    repositories {
        maven {
            isAllowInsecureProtocol = true
            credentials {
                username = "admin"
                password = "jl123456"
            }
            url = uri("http://192.168.101.123:8081/repository/jiali/")
        }
    }
}