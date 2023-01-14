// TODO: Remove once https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("pluu.android.library")
    id("pluu.android.hilt")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.pluu.webtoon.data"
}

dependencies {
    implementation(projects.core)
    implementation(projects.domain)
    implementation(projects.model)

    implementation(libs.kotlin.coroutine.core)

    testImplementation(libs.junit)
}

//apply(from = "../publish_local.gradle")
