package com.js.lib_processor

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.*

/**
 *
 * @ClassName:      InjectInfo
 * @Author:         Double
 * @CreateDate:     19-10-10 下午9:02
 * @Description:注解内容存储实例，一个文件里的内容都存储在这个里面
 * 也可以说同一个TypeElement(类)内容都存在这里
 */
class InjectInfo(val element: TypeElement) {

    var mLogger: Logger? = null
    //类名
    val className: ClassName = element.asClassName()
    val viewClass: ClassName = ClassName("android.view", "View")
    //包名
    val packageName: String = getPackageName(element).qualifiedName.toString()

    //布局只有一个id
    var layoutId: Int = -1
    //View 注解数据可能有多个 注意是VariableElement
    val viewMap = hashMapOf<Int, VariableElement>()
    //点击事件 注解数据可能有多个 注意是ExecutableElement
    val clickListenerMap = hashMapOf<Int, ExecutableElement>()

    private fun getPackageName(element: Element): PackageElement {
        var e = element
        while (e.kind != ElementKind.PACKAGE) {
            e = e.enclosingElement
        }
        return e as PackageElement
    }

    fun getClassName(element: Element): ClassName {
        var elementType = element.asType().asTypeName()

        return elementType as ClassName
    }

    fun generateConstructor(): FunSpec {
        val builder = FunSpec.constructorBuilder().addParameter("target", className)
            .addParameter("view", viewClass)

        if (layoutId != -1) {
            builder.addStatement("target.setContentView(%L)", layoutId)
        }

        viewMap.forEach { (id, variableElement) ->
            builder.addStatement(
                "target.%N = view.findViewById(%L)",
                variableElement.simpleName,
                id
            )
        }

        clickListenerMap.forEach { (id, element) ->

            when (element.parameters.size) {
                //没有参数
                0 -> builder.addStatement(
                    "(view.findViewById(%L) as View).setOnClickListener{target.%N()}"
                    , id
                )
                //一个参数
                1 -> {
                    if (getClassName(element.parameters[0]) != viewClass) {
                        mLogger?.error("element.simpleName function parameter error")
                    }
                    builder.addStatement(
                        "(view.findViewById(%L) as View).setOnClickListener{target.%N(it)}"
                        , id, element.simpleName
                    )
                }
                //多个参数错误
                else -> mLogger?.error("element.simpleName function parameter error")
            }

        }

       return builder.build()
    }

}