plugins {
    id("plugins.android-library")
    id("plugins.kotlin-android-extensions")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":data"))
}

apply(from = "../publish_local.gradle")