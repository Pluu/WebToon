@file:Suppress("RemoveRedundantBackticks")

package com.pluu.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("UnstableApiUsage")
internal fun Project.configureApplication() {
    extensions.configure<ApplicationExtension> {
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
                postprocessing {
                    isRemoveUnusedCode = true
                    isRemoveUnusedResources = true
                    isOptimizeCode = true
                    isObfuscate = true
                    proguardFile("proguard-rules.pro")
                }
            }
        }
    }
}

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureAndroid() {
    android {
        compileSdk = Const.compileSdk

        defaultConfig {
            minSdk = Const.minSdk

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables.useSupportLibrary = true
        }

        compileOptions {
            sourceCompatibility = Const.JAVA_VERSION
            targetCompatibility = Const.JAVA_VERSION
        }

        packaging {
            resources {
                excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            }
        }

        lint {
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
