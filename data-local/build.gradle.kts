plugins {
    id("pluu.android.library")
    id("pluu.android.hilt")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.pluu.webtoon.data.local"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets {
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.model)
    implementation(projects.data)

    implementation(libs.kotlin.coroutine.android)

    implementation(libs.androidX.room.runtime)
    ksp(libs.androidX.room.compiler)
    implementation(libs.androidX.room.ktx)
    implementation(libs.androidX.preference)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidX.room.testing)
    androidTestImplementation(libs.androidX.test.junit)
    androidTestImplementation(libs.androidX.test.espresso)
    androidTestImplementation(libs.kotlin.coroutine.test)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}

//apply(from = "../publish_local.gradle")
