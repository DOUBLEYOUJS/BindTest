package com.js.lib_api

import android.app.Activity
import android.view.View
import java.lang.reflect.InvocationTargetException

/**
 *
 * @ClassName:      BindApi
 * @Author:         Double
 * @CreateDate:     19-10-9 下午10:19
 * @Description:这里类是去调用编译出来的文件里面的方法，帮助实现绑定等内容
 */
object BindApi {

    //类似ButterKnife方法
    fun bind(target: Activity) {
        val sourceView = target.window.decorView
        createBinding(target, sourceView)
    }

    private fun createBinding(target: Activity, source: View) {
        val targetClass = target::class.java
        var className = targetClass.name
        try {
            //获取类名
            val bindingClass = targetClass.classLoader!!.loadClass(className + "_BindTest")
            //获取构造方法
            val constructor = bindingClass.getConstructor(targetClass, View::class.java)
            //向方法中传入数据activity和view
            constructor.newInstance(target, source)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }
}