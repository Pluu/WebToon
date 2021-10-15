pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
    plugins {
        id("com.google.devtools.ksp") version "1.5.31-1.0.0"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}

rootProject.name = "WebToon"

include(
    ":app",
    ":data",
    ":core",
    ":core-android",
    ":model",
    ":domain",
    ":site",
    ":ui-common",
    ":ui-weekly",
    ":ui-episode",
    ":ui-setting",
    ":ui-detail",
    ":compose"
)
arrayOf(
    ":ui-common",
    ":ui-weekly",
    ":ui-setting",
    ":ui-episode",
    ":ui-detail"
).forEach { name ->
    project(name).projectDir = File(rootDir, "features/${name.substring(startIndex = 1)}")
}
