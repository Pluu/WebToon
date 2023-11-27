plugins {
    id("pluu.android.library")
    id("pluu.android.hilt")
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
