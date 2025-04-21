package com.cat.android.plugin_navigation_plus

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin

open class NavPluginPlusExtension {
    // 文件生成路径，是相对根目录的路径，一般写到java目录，如：app/src/main/java，效果是：根目录/app/src/main.java
    var navRegistryPath: String = ""
    // 文件生成的包名，是在上面的path基础下的包名，如：com.jiali.xx，效果是：根目录/app/src/main.java/com/jiali/xx
    var navRegistryPackageName: String = ""
}

class NavPluginPlus : Plugin<Project> {
    override fun apply(project: Project) {
        println("NavPluginPlus apply yeah!!!")
        project.extensions.create("NavFilePath", NavPluginPlusExtension::class.java)
        val applicationPlugin = project.plugins.findPlugin(ApplicationPlugin::class.java)
        // 这个目的是检查插件是否只在application模块下被加载，此插件不能给module模块加载，因为插件只要应用一次，在打包阶段就能收集全部的class文件
        assert(applicationPlugin == null) {
            throw GradleException("nav-plugin can only be applied to the module where the application plugin is located")
        }
        // 注册AsmClass
        project.extensions.getByType(AndroidComponentsExtension::class.java).onVariants {
            it.instrumentation.transformClassesWith(NavPlusTransform::class.java,InstrumentationScope.ALL){}
            it.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
        }
    }
}

