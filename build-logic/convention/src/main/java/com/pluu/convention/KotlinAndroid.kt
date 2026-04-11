@file:Suppress("RemoveRedundantBackticks")

package com.pluu.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("UnstableApiUsage")
internal fun Project.configureApplication(
    applicationExtension: ApplicationExtension
) {
    applicationExtension.apply {
        defaultConfig {
            targetSdk = Const.targetSdk
        }

        androidResources {
            localeFilters += listOf("en", "ko")
        }

        signingConfigs {
            getByName("debug") {
                storeFile = project.rootProject.file("debug.keystore")
                storePassword = "android"
                keyAlias = "androiddebugkey"
                keyPassword = "android"
            }
        }

        buildTypes {
            debug {
                signingConfig = signingConfigs.getByName("debug")
                applicationIdSuffix = ".debug"
            }

            release {
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
    }
}

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureAndroid(
    commonExtension: CommonExtension
) {
    commonExtension.apply {
        compileSdk {
            version = release(Const.compileSdk)
        }

        defaultConfig.apply {
            minSdk = Const.minSdk

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables.useSupportLibrary = true
        }

        compileOptions.apply {
            sourceCompatibility = Const.JAVA_VERSION
            targetCompatibility = Const.JAVA_VERSION
        }

        packaging.apply {
            resources {
                excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            }
        }

        lint.apply {
            checkOnly.add("Interoperability")
            disable.add("ContentDescription")
            abortOnError = false
        }
    }
}

internal fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            // Set JVM target
            jvmTarget.set(Const.JVM_TARGET)
//            allWarningsAsErrors.set(true)

            optIn.addAll(
                "kotlin.RequiresOptIn",
                "kotlinx.coroutines.ExperimentalCoroutinesApi",
                "kotlinx.coroutines.FlowPreview",
            )
        }
    }
}

internal fun Project.`java`(
    configure: Action<JavaPluginExtension>
) {
    (this as ExtensionAware).extensions.configure("java", configure)
}

internal fun Project.`kotlin`(
    configure: Action<KotlinAndroidProjectExtension>
) {
    (this as ExtensionAware).extensions.configure("kotlin", configure)
}
