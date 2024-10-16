@file:Suppress("UnstableApiUsage", "UNCHECKED_CAST")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    apply("${settingsDir.parent}/repositories.gradle.kts")
    (extra["repos"] as (RepositoryHandler) -> Unit)(repositories)

    repositories {
        gradlePluginPortal()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include(":convention")