@file:Suppress("UnstableApiUsage", "unused")

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.pluu.convention.configureAndroid
import com.pluu.convention.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            configureAndroid()

            extensions.configure<BaseAppModuleExtension> {
                configureKotlin(this)

                signingConfigs {
                    getByName("debug") {
                        storeFile = project.rootProject.file("debug.keystore")
                        storePassword = "android"
                        keyAlias = "androiddebugkey"
                        keyPassword = "android"
                    }
                }

                buildTypes {
                    getByName("debug") {
                        signingConfig = signingConfigs.getByName("debug")
                        applicationIdSuffix = ".debug"
                    }
                }

                packagingOptions {
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
    }
}