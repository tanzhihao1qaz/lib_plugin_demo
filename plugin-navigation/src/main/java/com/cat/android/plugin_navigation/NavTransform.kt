package com.cat.android.plugin_navigation

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import com.cat.android.plugin_navigation_runtime.NavData
import com.cat.android.plugin_navigation_runtime.NavDestination
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import org.apache.commons.io.FileUtils
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AnnotationNode
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipFile


/**
 * @作者 志浩
 * @时间 2023/10/9 10:27
 * @描述 TODO
 */
class NavTransform(private val project: Project) : Transform() {
    private val TAG = "NavTransform"
    private val navDataList = mutableListOf<NavData>()

    companion object {
        const val NAV_RUNTIME_DESTINATION = "Lcom/cat/android/plugin_navigation_runtime/NavDestination;"
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

    override fun getName(): String {
        return TAG
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        // 只对class文件做处理
        return TransformManager.CONTENT_CLASS
    }

    // 插件有效作用域
    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        // 这个项目编译都用到，所以选full_project
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return false
    }

    @Throws(TransformException::class, InterruptedException::class, IOException::class)
    override fun transform(transformInvocation: TransformInvocation?) {
        super.transform(transformInvocation)
        // 1. 对inputs -》 directory -》 class 文件进行遍历
        // 1. 对inputs -》 jar -》 class 文件进行遍历
        val inputs = transformInvocation?.inputs ?: return
        val outputProvider = transformInvocation.outputProvider
        inputs.forEach {
            it.directoryInputs.forEach { directoryInput ->
                handleDirectoryClasses(directoryInput.file)
                val outputDir = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                // 这个是build/intermediates目录
                println("$TAG DIR = ${directoryInput.file.path}")
                if (directoryInput.file.isFile) {
                    FileUtils.copyFile(directoryInput.file, outputDir)
                } else {
                    FileUtils.copyDirectory(directoryInput.file, outputDir)
                }
            }
            it.jarInputs.forEach { jarInput ->
                handleJarClasses(jarInput.file)
                val outputDir = outputProvider.getContentLocation(jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                if (jarInput.file.isFile) {
                    FileUtils.copyFile(jarInput.file, outputDir)
                } else {
                    FileUtils.copyDirectory(jarInput.file, outputDir)
                }
            }
        }
        // 解析完注解后，开始生成NavGraph
        generateNavRegistry()
    }

    private fun generateNavRegistry() {
        val runtimeProject = project.rootProject.findProject(CREATE_NAV_CODE_MODULE_NAME)
        assert(runtimeProject == null) {
            throw GradleException("找不到${CREATE_NAV_CODE_MODULE_NAME}此模块")
        }
        val android = runtimeProject!!.extensions.getByName("android") as BaseAppModuleExtension


        // 如果是java module，可以直接findByName("sourceSets")拿到java.srcDir，但是android module就不行了，android的sourceSets是空了，不能这样直接获取
        /*println("$TAG runtimeProject路径:${runtimeProject!!.projectDir.path}")
        val sourceSet = runtimeProject!!.extensions.findByName("sourceSets") as SourceSetContainer
        val outputFileDir = sourceSet.first().java.srcDirs.first().absoluteFile
        fileSpec.writeTo(outputFileDir)*/

        var fileSpec: FileSpec? = null
        android.applicationVariants.all {
            val extension = project.extensions.getByType(NavPluginExtension::class.java)
            println("NavRegistry file path:${project.rootDir.path}${File.separator}${extension.navRegistryPath}")
            println("NavRegistry package name:${extension.navRegistryPackageName}")

            val packageName = extension.navRegistryPackageName
            if (fileSpec == null) {
//                fileSpec = createFileSpec(android.defaultConfig.applicationId!!)
                fileSpec = createFileSpec(packageName)
            }
            // 这个是生成在buildConfig的输出目录下
//            val outputFileDir = File("${project.buildDir}/generated/source/buildConfig/${it.dirName}/")
            val outputFileDir = File("${project.rootDir.path}${File.separator}${extension.navRegistryPath}${File.separator}")
            // 这个是生成在main/java/包名（准确的说是android的build.gradle里的sourceSet定义的main路径）
//            val outputFileDir = android.sourceSets.getByName("main").java.srcDirs.first().absoluteFile
            fileSpec?.writeTo(outputFileDir)
        }

        // 这个是生成在app的build目录下
        /*val outputFileDir = runtimeProject!!.buildDir.absoluteFile
        fileSpec.writeTo(outputFileDir)*/
    }

    private fun createFileSpec(appId: String): FileSpec {
        // 利用kotlinPoet生成NavRegistry.kt文件，存放在nav_plugin_runtime模块下
        // 用于记录项目中所有的路由节点数据
        val navData = ClassName(NAV_RUNTIME_PKG_NAME, NAV_RUNTIME_DATA_CLASS_NAME)
        val arrayList = ClassName("kotlin.collections", "ArrayList")
        val list = ClassName("kotlin.collections", "List")
        val arrayListOfNavData = arrayList.parameterizedBy(navData)
        val listOfNavData = list.parameterizedBy(navData)

        val statements = StringBuffer()
        navDataList.forEach {
            statements.append("navList.add(NavData(type = ${it.type},route = \"${it.route}\",className = \"${it.className}\",navGraphRoute = \"${it.navGraphRoute}\",isStart = ${it.isStart},deeplink = \"${it.deeplink}\"))")
            statements.append("\n ")
        }

        val property = PropertySpec
            .builder(NAV_RUNTIME_NAV_LIST, arrayListOfNavData, KModifier.PRIVATE)
            .initializer(CodeBlock.builder().addStatement("ArrayList<NavData>()").build())
            .build()
        val function = FunSpec
            .builder("getList")
            .returns(listOfNavData)
            .addCode(CodeBlock.builder().addStatement("val list = ArrayList<NavData>()\n list.addAll(navList)\n return list\n").build())
            .build()
        val type = TypeSpec
            .objectBuilder(NAV_RUNTIME_REGISTRY_CLASS_NAME)
            .addProperty(property)
            .addInitializerBlock(CodeBlock.builder().addStatement(statements.toString()).build())
            .addFunction(function)
            .build()

        // 这里的
        println("appId = $appId")
        // 参数1是包名
        return FileSpec.builder(appId, NAV_RUNTIME_REGISTRY_CLASS_NAME)
            .addComment("此文件是自动生成，不用编辑它")
            .addType(type)
            .addImport(NavDestination.NavType::class.java, "Fragment", "Activity", "Dialog", "None")
            .addImport(NavData::class.java, "")
            .build()
    }

    private fun handleDirectoryClasses(file: File) {
        if (file.isDirectory) {
            file.listFiles()?.forEach {
                handleDirectoryClasses(it)
            }
        } else if (file.extension.endsWith("class")) {
//            println("$TAG handleDirectoryClasses-dirEntry:${file.name}")
            val inputStream = FileInputStream(file)
            visitClass(inputStream)
            inputStream.close()
        }
    }

    private fun handleJarClasses(file: File) {
//        println("$TAG handlerJarClasses:${file.name}")
        val zipFile = ZipFile(file)
        zipFile.stream().forEach {
            if (it.name.endsWith("class")) {
//                println("$TAG handlerJarClasses-zipEntry:${it.name}")
                val inputStream = zipFile.getInputStream(it)
                visitClass(inputStream)
                inputStream.close()
            }
        }
        zipFile.close()
    }

    private fun visitClass(inputStream: InputStream) {
        val classReader = ClassReader(inputStream)
        val classVisitor = object : ClassVisitor(Opcodes.ASM9) {
            override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
//                return super.visitAnnotation(descriptor, visible)
                if (descriptor != NAV_RUNTIME_DESTINATION) {
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
                }
            }
        }
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
    }
}