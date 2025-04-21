package com.cat.android.plugin_navigation_plus

import com.cat.android.plugin_navigation_runtime.NavData
import com.cat.android.plugin_navigation_runtime.NavDestination
import org.gradle.api.GradleException
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AnnotationNode


class NavClassVisitor(nextVisitor: ClassVisitor) : ClassVisitor(Opcodes.ASM5, nextVisitor) {
    companion object {
        private const val NAV_RUNTIME_DESTINATION = "Lcom/cat/android/plugin_navigation_runtime/NavDestination;"
        private const val KEY_ROUTE = "route"  // NavDestination注解里定义的route成员变量
        private const val KEY_TYPE = "type" // NavDestination注解里定义的type成员变量
        private const val KEY_NAV_GRAPH_ROUTE = "navGraphRoute" // NavDestination注解里定义的navGroupRoute成员变量
        private const val KEY_IS_STARTER = "isStart" // NavDestination注解里定义的isStart成员变量
        private const val KEY_DEEPLINK = "deeplink" // NavDestination注解里定义的deeplink成员变量

        private const val NAV_RUNTIME_PKG_NAME = "com.cat.android.plugin_navigation_runtime"
        private const val NAV_RUNTIME_REGISTRY_CLASS_NAME = "NavRegistry" // 自定的
        private const val NAV_RUNTIME_DATA_CLASS_NAME = "NavData" // 自定的
        private const val NAV_RUNTIME_NAV_LIST = "navList" // 自定的

        //                private const val NAV_RUNTIME_MODULE_NAME = "nav-plugin-runtime"
        private const val CREATE_NAV_CODE_MODULE_NAME = "app" // 要在哪个module生成代码
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        return super.visitAnnotation(descriptor, visible)
        /*if (descriptor != NAV_RUNTIME_DESTINATION) {
            return object : AnnotationNode(Opcodes.ASM9, "") {}
        } else {
            val annotationVisitor = object : AnnotationNode(Opcodes.ASM9, "") {
                var route = ""
                var type = NavDestination.NavType.None
                var isStart = false
                var navGraphRoute = ""
                var deeplink = ""
                override fun visit(name: String?, value: Any?) {
                    super.visit(name, value)
                    when (name) {
                        KEY_ROUTE -> route = value as String
                        KEY_IS_STARTER -> isStart = value as Boolean
                        KEY_DEEPLINK -> deeplink = value as String
                        KEY_NAV_GRAPH_ROUTE -> navGraphRoute = value as String
                    }
                }

                override fun visitEnum(name: String?, descriptor: String?, value: String?) {
                    super.visitEnum(name, descriptor, value)
                    if (name == KEY_TYPE) {
                        assert(value == null) {
                            throw GradleException("NavDestination \$type must be of Fragment or Activity or Dialog or None")
                        }
                        type = NavDestination.NavType.valueOf(value!!)
                    }
                }

                override fun visitEnd() {
                    super.visitEnd()
                    val navData = NavData(
                        type = type,
                        route = route,
                        className = classReader.className.replace("/", "."),
                        navGraphRoute = navGraphRoute,
                        isStart = isStart,
                        deeplink = deeplink
                    )
                    navDataList.add(navData)
                }
            }
            return annotationVisitor
        }*/
    }
}