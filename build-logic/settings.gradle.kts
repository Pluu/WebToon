@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
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
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

include(":convention")