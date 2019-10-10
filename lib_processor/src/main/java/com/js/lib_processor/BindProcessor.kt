package com.js.lib_processor

import com.google.auto.service.AutoService
import com.js.lib_annotations.BindLayout
import com.js.lib_annotations.BindView
import com.js.lib_annotations.OnClick
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

/**
 * @ClassName:      BindProcessor
 * @Author:         Double
 * @CreateDate:     19-10-9 下午9:22
 * @Description:
 */
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class BindProcessor : AbstractProcessor() {
    companion object {
        private const val PICK_END = "_BindTest"
    }

    private lateinit var mLogger: Logger
    //存储类文件数据
    private val mInjectMaps = hashMapOf<String, InjectInfo>()

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment
    ): Boolean {
        //里面就要生成我们需要的文件

        roundEnv.getElementsAnnotatedWith(BindLayout::class.java).forEach {
            bindLayout(it)
        }

        roundEnv.getElementsAnnotatedWith(BindView::class.java).forEach {
            bindView(it)
        }

        roundEnv.getElementsAnnotatedWith(OnClick::class.java).forEach {
            bindClickListener(it)
        }

        mInjectMaps.forEach { (name, info) ->

           val file= FileSpec.builder(info.packageName, info.className.simpleName + PICK_END)
                .addType(
                    TypeSpec.classBuilder(info.className.simpleName + PICK_END)
                        .primaryConstructor(info.generateConstructor()).build()
                ).build()

            file.writeFile()
        }

        return true
    }

    private fun FileSpec.writeFile() {
        //文件编译后位置
        val kaptKotlinGeneratedDir = processingEnv.options["kapt.kotlin.generated"]
        val outputFile = File(kaptKotlinGeneratedDir).apply {
            mkdirs()
        }
        writeTo(outputFile.toPath())
    }

    private fun bindLayout(element: Element) {
        //BindLayout注解的是Class，本身就是TypeElement
        val typeElement = element as TypeElement
        //一个类一个injectInfo
        val className = typeElement.qualifiedName.toString()
        var injectInfo = mInjectMaps[className]
        if (injectInfo == null) {
            injectInfo = InjectInfo(typeElement)
        }

        typeElement.getAnnotation(BindLayout::class.java).run {
            injectInfo.layoutId = value
        }

        mInjectMaps[className] = injectInfo
    }

    private fun bindView(element: Element) {
        //BindView注解的是变量，element就是VariableElement
        val variableElement = element as VariableElement
        val typeElement = element.enclosingElement as TypeElement
        //一个类一个injectInfo
        val className = typeElement.qualifiedName.toString()
        var injectInfo = mInjectMaps[className]
        if (injectInfo == null) {
            injectInfo = InjectInfo(typeElement)
        }

        variableElement.getAnnotation(BindView::class.java).run {
            injectInfo.viewMap[value] = variableElement
        }

        mInjectMaps[className] = injectInfo
    }

    private fun bindClickListener(element: Element) {
        //OnClick注解的是方法，element就是VariableElement
        val variableElement = element as ExecutableElement
        val typeElement = element.enclosingElement as TypeElement
        //一个类一个injectInfo
        val className = typeElement.qualifiedName.toString()
        var injectInfo = mInjectMaps[className]
        if (injectInfo == null) {
            injectInfo = InjectInfo(typeElement)
        }

        variableElement.getAnnotation(OnClick::class.java).run {
            values.forEach {
                injectInfo.clickListenerMap[it] = variableElement
            }
        }

        mInjectMaps[className] = injectInfo
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(
            BindLayout::class.java.canonicalName,
            BindView::class.java.canonicalName,
            OnClick::class.java.canonicalName
        )
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        mLogger = Logger(processingEnv.messager)
        mLogger.info("processor init")
    }
}