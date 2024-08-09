plugins {
    id("version-catalog")
    id("maven-publish")
}

catalog {
    versionCatalog {
        // 从文件中导入
        from(files("../gradle/libs.versions.toml"))
    }
}

publishing {
    publications {
        create<MavenPublication>("catalog-plugin") {
            from(components["versionCatalog"])
            groupId = "com.jiali.android"
            artifactId = "catalog"
            version = "1.0.0-SNAPSHOT"
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