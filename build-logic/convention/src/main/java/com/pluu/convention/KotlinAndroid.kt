package com.pluu.convention

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.BaseExtension
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureAndroid() {
    extensions.configure<BaseExtension> {
        compileSdkVersion(ProjectConfigurations.compileSdk)

        defaultConfig {
            minSdk = ProjectConfigurations.minSdk
            targetSdk = ProjectConfigurations.targetSdk

            testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
            vectorDrawables.useSupportLibrary = true
            resourceConfigurations.addAll(listOf("en", "ko"))
        }

        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android.txt"),
                    "proguard-rules.pro"
                )
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }
}

internal fun Project.configureKotlin(
    commonExtension: CommonExtension<*, *, *, *>
) {
    commonExtension.kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xskip-prerelease-check",
            "-Xjvm-default=all"
        )

        // Set JVM target to 1.8
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

internal fun Project.`kapt`(
    configure: Action<KaptExtension>
) {
    (this as ExtensionAware).extensions.configure("kapt", configure)
}

internal fun CommonExtension<*, *, *, *>.kotlinOptions(
    block: KotlinJvmOptions.() -> Unit
) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}