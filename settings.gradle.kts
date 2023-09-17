@file:Suppress("UnstableApiUsage", "UNCHECKED_CAST")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")

    apply("repositories.gradle.kts")
    repositories {
        gradlePluginPortal()
        val configureSharedRepositories = extra["configureSharedRepositories"] as RepositoryHandler.() -> Unit
        configureSharedRepositories(this)
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    apply("repositories.gradle.kts")
    val configureSharedRepositories = extra["configureSharedRepositories"] as RepositoryHandler.() -> Unit
    configureSharedRepositories(repositories)
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
includeProject(":ui-main-container", "features")
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
