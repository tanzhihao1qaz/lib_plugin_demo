plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
    `kotlin-dsl`
    id("maven-publish") // 这个是发布本地用的
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(gradleApi())
    // 解析toml文件
    implementation("org.tomlj:tomlj:1.1.1")
    //
    implementation("com.android.tools.build:gradle:7.4.0")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
}

publishing {
    publications {
        create<MavenPublication>("common-gradle-setup") {
            from(components["java"])
            groupId = "com.jiali.android"
            artifactId = "common-gradle-setup"
            version = "1.9-SNAPSHOT"
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