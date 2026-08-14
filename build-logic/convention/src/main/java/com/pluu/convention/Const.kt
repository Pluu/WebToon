package com.pluu.convention

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object Const {
    const val compileSdk = 37
    const val compileSdkMinorApiLevel = 1
    const val minSdk = 24
    const val targetSdk = 37
    val JAVA_VERSION = JavaVersion.VERSION_11
    val JVM_TARGET = JvmTarget.JVM_11
}