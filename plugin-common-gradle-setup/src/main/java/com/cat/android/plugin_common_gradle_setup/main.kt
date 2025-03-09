package com.cat.android.plugin_common_gradle_setup

import org.tomlj.Toml
import java.io.File
import java.nio.file.Paths

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val root = System.getProperty("user.dir")
        val filePath = "${root}/gradle/libs.versions.toml"
        val source = Paths.get(filePath)
        val result = Toml.parse(source)
        val moshi_codegen = CatalogUtil.getCatalog("moshi-codegen", CatalogUtil.Type.LIBRARY)
        val moshi = CatalogUtil.getCatalog("moshi", CatalogUtil.Type.LIBRARY)

        val a = 1
    }
}
