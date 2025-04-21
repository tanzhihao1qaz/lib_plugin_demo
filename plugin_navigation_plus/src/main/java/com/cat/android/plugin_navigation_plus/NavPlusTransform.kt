package com.cat.android.plugin_navigation_plus

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.cat.android.plugin_navigation_runtime.NavData
import com.cat.android.plugin_navigation_runtime.NavDestination
import org.objectweb.asm.ClassVisitor

abstract class NavPlusTransform : AsmClassVisitorFactory<InstrumentationParameters.None> {

    override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
        return NavClassVisitor(nextClassVisitor)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return classData.classAnnotations.contains(NavDestination::class.java.canonicalName)
    }
}