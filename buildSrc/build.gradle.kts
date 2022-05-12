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
    implementation("com.android.tools.build:gradle:7.3.0-beta01")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.20")
    implementation("com.squareup:javapoet:1.13.0")
}
