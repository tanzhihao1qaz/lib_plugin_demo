// 此文件是自动生成，不用编辑它
package com.cat.android.lib_plugin_demo

import com.cat.android.plugin_navigation_runtime.NavData
import com.cat.android.plugin_navigation_runtime.NavDestination.NavType.Activity
import com.cat.android.plugin_navigation_runtime.NavDestination.NavType.Dialog
import com.cat.android.plugin_navigation_runtime.NavDestination.NavType.Fragment
import com.cat.android.plugin_navigation_runtime.NavDestination.NavType.None
import kotlin.collections.ArrayList
import kotlin.collections.List

public object NavRegistry {
  private val navList: ArrayList<NavData> = ArrayList<NavData>()


  init {
    navList.add(NavData(type = Activity,route = "/main",className =
        "com.cat.android.lib_plugin_demo.MainActivity",navGraphRoute = "",isStart = false,deeplink =
        ""))
         
  }

  public fun getList(): List<NavData> {
    val list = ArrayList<NavData>()
         list.addAll(navList)
         return list

  }
}
