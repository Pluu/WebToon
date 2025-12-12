fun RepositoryHandler.configureSharedRepositories() {
    google {
        content {
            includeGroupByRegex("com\\.android.*")
            includeGroupByRegex("com\\.google.*")
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

pluginManagement.repositories.configureSharedRepositories()
pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories.configureSharedRepositories()
}