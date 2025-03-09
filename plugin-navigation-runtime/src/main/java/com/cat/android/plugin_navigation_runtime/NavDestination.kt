package com.cat.android.plugin_navigation_runtime

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class NavDestination(
    val type: NavType,
    val route: String,
    val navGraphRoute: String = "",
    val isStart: Boolean = false,
    val deeplink: String = "",
) {
    enum class NavType {
        Fragment, Activity, Dialog, None
    }
}