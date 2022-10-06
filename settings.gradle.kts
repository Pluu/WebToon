@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
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
        mavenCentral()
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
