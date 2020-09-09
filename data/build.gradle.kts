plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

listOf(
    "commonConfiguration.gradle",
    "libraryConfiguration.gradle"
).forEach { file ->
    apply(from = "${rootProject.projectDir}/gradle/${file}")
}

android {
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets {
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
    }
}

dependencies {
    implementation(project(":core"))

    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.Kotlin.coroutines.android)

    implementation(Dep.AndroidX.room.runtime)
    kapt(Dep.AndroidX.room.compiler)
    implementation(Dep.AndroidX.room.ktx)
    implementation(Dep.AndroidX.UI.preference)

    // OkHttp
    implementation(Dep.OkHttp.core)
    implementation(Dep.OkHttp.loggingInterceptor)

    // Dagger Hilt
    implementation(Dep.Hilt.android)
    kapt(Dep.Hilt.hilt_compiler)

    testImplementation(Dep.Test.junit)
    androidTestImplementation(Dep.AndroidX.room.testing)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)
    androidTestImplementation(Dep.Kotlin.coroutines.test)
}

//apply(from = "../publish_local.gradle")