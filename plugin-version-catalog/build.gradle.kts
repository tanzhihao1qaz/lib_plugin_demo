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
        /*maven {
            url = uri("../repo")
        }*/
        // 提交到远程仓库
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
                password = "123456"
            }
            url = uri("http://localhost:8081/repository/jiali/")
        }*/
    }
}