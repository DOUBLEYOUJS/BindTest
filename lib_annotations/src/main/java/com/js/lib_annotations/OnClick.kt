package com.js.lib_annotations

/**
 *
 * @ClassName:      OnClick
 * @Author:         Double
 * @CreateDate:     19-10-10 下午8:58
 * @Description: 点击事件注解，这里只是单击事件，其他事件需要重新写代码
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
annotation class OnClick (vararg val values:Int)