package com.jiali.android.lib_plugin_demo
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "name")
    val name: String
)