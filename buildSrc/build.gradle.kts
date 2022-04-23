plugins {
    `kotlin-dsl`
}

repositories {
    google {
        content {
            includeGroupByRegex("com\\.android.*")
            includeGroupByRegex("androidx.*")
            includeGroup("com.google.testing.platform")
        }
    }
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.2.0-beta04")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.20")
    implementation("com.squareup:javapoet:1.13.0")
}
