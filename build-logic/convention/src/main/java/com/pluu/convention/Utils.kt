package com.pluu.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

typealias AndroidExtension = CommonExtension<*, *, *, *, *, *>

internal val Project.androidExtension: AndroidExtension
    get() = extensions.getByType(CommonExtension::class.java)

internal fun Project.android(block: AndroidExtension.() -> Unit) {
    androidExtension.block()
}