plugins {
    id("java-library")
    id("version-catalog")
    id("maven-publish")
}

catalog {
    versionCatalog {
        // 从文件中导入
        from(files("../gradle/libs.versions.toml"))
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

publishing {
    publications {
        create<MavenPublication>("catalog-plugin") {
            from(components["versionCatalog"])
            groupId = "com.cat.android"
            artifactId = "catalog"
            version = "1.0.0"
        }
    }
    repositories {
        // 本地仓库
        maven {
            url = uri("../repo")
        }
        // 提交到远程仓库
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