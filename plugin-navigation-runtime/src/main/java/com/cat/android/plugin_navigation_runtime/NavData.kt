package com.cat.android.plugin_navigation_runtime

/**
 * @作者 志浩
 * @时间 2023/10/9 14:35
 * @描述 TODO
 */
data class NavData(
    val type: NavDestination.NavType,
    val route: String,
    val className: String,
    val navGraphRoute: String = "",
    val isStart: Boolean = false,
    val deeplink: String = ""
)