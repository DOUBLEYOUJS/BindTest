package com.js.lib_annotations

/**
 *
 * @ClassName:      BindLayout
 * @Author:         Double
 * @CreateDate:     19-10-10 下午8:57
 * @Description:
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class BindLayout(val value: Int = -1)