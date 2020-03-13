plugins {
    id("plugins.android-library")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(Dep.Kotlin.coroutinesCore)
    // Jsoup
    implementation(Dep.jsoup)
}

apply(from = "../publish_local.gradle")