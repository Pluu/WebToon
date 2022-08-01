plugins {
    id("pluu.android.library")
}

android {
    namespace = "com.pluu.webtoon.domain"
}

dependencies {
    implementation(projects.core)
    implementation(projects.model)

    implementation(libs.kotlin.coroutine.core)
    implementation(libs.dagger)

    testImplementation(libs.junit)
}


//apply(from = "../publish_local.gradle")
