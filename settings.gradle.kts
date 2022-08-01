@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    val agpVersion = "7.3.0-beta05"
    val kotlinVersion = "1.7.10"

    includeBuild("build-logic")

    repositories {
        gradlePluginPortal()
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("androidx.*")
                includeGroup("android.arch.lifecycle")
                includeGroup("android.arch.core")
                includeGroup("com.google.firebase")
                includeGroup("com.google.android.gms")
                includeGroup("com.google.android.material")
                includeGroup("com.google.gms")
                includeGroup("com.google.testing.platform")
                includeGroup("zipflinger")
            }
        }
        maven("https://plugins.gradle.org/m2/") {
            content {
                includeGroup("org.jlleitschuh.gradle")
            }
        }
        mavenCentral()
    }

    plugins {
        id("com.android.application") version agpVersion
        id("com.android.library") version agpVersion
        id("org.jetbrains.kotlin.android") version kotlinVersion
        id("org.jetbrains.kotlin.jvm") version kotlinVersion
        id("org.jetbrains.kotlin.kapt") version kotlinVersion
        id("com.google.devtools.ksp") version "${kotlinVersion}-1.0.6"
    }
    resolutionStrategy {
        eachPlugin {
            when (val id = requested.id.id) {
                "dagger.hilt.android.plugin" -> useModule("com.google.dagger:hilt-android-gradle-plugin:2.43")
                "org.jetbrains.kotlin.plugin.serialization" -> useModule("${id}:${id}.gradle.plugin:${kotlinVersion}")
            }
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("androidx.*")
                includeGroup("android.arch.lifecycle")
                includeGroup("android.arch.core")
                includeGroup("com.google.firebase")
                includeGroup("com.google.android.gms")
                includeGroup("com.google.android.material")
                includeGroup("com.google.gms")
                includeGroup("com.google.testing.platform")
                includeGroup("zipflinger")
            }
        }
        maven("https://plugins.gradle.org/m2/") {
            content {
                includeGroup("org.jlleitschuh.gradle")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "WebToon"

fun includeProject(moduleName: String, rootFolderName: String = "") {
    settings.include(moduleName)

    if (rootFolderName.isNotEmpty()) {
        project(moduleName).projectDir =
            File(rootDir, "${rootFolderName}/${moduleName.substring(startIndex = 1)}")
    }
}

///////////////////////////////////////////////////////////////////////////
// Modules
///////////////////////////////////////////////////////////////////////////

includeProject(":app")
includeProject(":data")
includeProject(":data-local")
includeProject(":data-remote")
includeProject(":core")
includeProject(":core-android")
includeProject(":model")
includeProject(":domain")
includeProject(":compose")
includeProject(":ui-theme")
includeProject(":ui-intro", "features")
includeProject(":ui-common", "features")
includeProject(":ui-weekly", "features")
includeProject(":ui-episode", "features")
includeProject(":ui-setting", "features")
includeProject(":ui-detail", "features")
includeProject(":sample-common", "sample")
includeProject(":sample-intro", "sample")
includeProject(":sample-weekly", "sample")
includeProject(":sample-episode", "sample")
includeProject(":sample-detail", "sample")
includeProject(":sample-setting", "sample")
