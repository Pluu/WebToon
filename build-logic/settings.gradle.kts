enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

apply(from = rootDir.toPath().parent.resolve("repositories.gradle.kts"))

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include(":convention")