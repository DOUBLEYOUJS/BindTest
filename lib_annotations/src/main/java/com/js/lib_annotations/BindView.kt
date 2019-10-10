package com.js.lib_annotations

/**
 *
 * @ClassName:      BindView
 * @Author:         Double
 * @CreateDate:     19-10-9 下午9:19
 * @Description:
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class BindView (val value:Int = -1)