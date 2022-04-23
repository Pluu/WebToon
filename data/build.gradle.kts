plugins {
    id("android-library-convention")
    id("com.google.devtools.ksp")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.pluu.webtoon.data"
}

dependencies {
    implementation(projects.core)
    implementation(projects.domain)
    implementation(projects.model)

    implementation(libs.kotlin.coroutine.core)

    // Dagger Hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    testImplementation(libs.junit)
}

kapt {
    correctErrorTypes = true
}

//apply(from = "../publish_local.gradle")
