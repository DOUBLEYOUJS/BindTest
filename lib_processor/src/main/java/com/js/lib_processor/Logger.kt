package com.js.lib_processor

import javax.annotation.processing.Messager
import javax.tools.Diagnostic

/**
 *
 * @ClassName:      Logger
 * @Author:         Double
 * @CreateDate:     19-10-9 下午10:05
 * @Description:
 */
class Logger(private val messager: Messager) {
    //建议使用Diagnostic.Kind.MANDATORY_WARNING 或者 Diagnostic.Kind.WARNING
    //JDK8 NOTE和OTHER可能无法打印
    //ERROR则会编译出错
    fun info(log: String) {
        messager.printMessage(Diagnostic.Kind.WARNING, log)
    }

    fun error(log: String) {
        messager.printMessage(Diagnostic.Kind.ERROR, log)
    }
}