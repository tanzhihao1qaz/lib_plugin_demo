package com.cat.android.plugin_common_gradle_setup

import org.tomlj.Toml
import org.tomlj.TomlParseResult
import org.tomlj.TomlTable
import java.nio.file.Paths

object CatalogUtil {
    private val result: TomlParseResult

    init {
        val root = System.getProperty("user.dir")
        val filePath = "${root}/gradle/libs.versions.toml"
        val source = Paths.get(filePath)
        result = Toml.parse(source)
    }

    enum class Type {
        VERSION,
        LIBRARY,
        PLUGIN,
        BUNDLE,
    }

    fun getCatalog(dependencyName: String, type: Type): String {
        when (type) {
            Type.VERSION -> {

            }

            Type.LIBRARY -> {
                val group = result.getTable("libraries")?.getTable(dependencyName)?.getString("group")
                val name = result.getTable("libraries")?.getTable(dependencyName)?.getString("name")
                val version = result.getTable("libraries")?.getTable(dependencyName)?.get("version")
                if (version is TomlTable) {
                    val ref = version.getString("ref")
                    val versionStr = result.getTable("versions")?.get(ref)
                    return "${group}:${name}:${versionStr}"
                } else {
                    return "${group}:${name}:${version}"
                }
            }

            Type.PLUGIN -> {

            }

            Type.BUNDLE -> {

            }
        }
        return ""
    }
}