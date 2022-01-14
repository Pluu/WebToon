plugins {
    id("android-library-convention")
    id("com.google.devtools.ksp")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
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
    implementation(projects.core)

    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.Kotlin.coroutines.android)

    implementation(Dep.AndroidX.room.runtime)
    ksp(Dep.AndroidX.room.compiler)
    implementation(Dep.AndroidX.room.ktx)
    implementation(Dep.AndroidX.UI.preference)

    // OkHttp
    implementation(Dep.OkHttp.core)
    implementation(Dep.OkHttp.loggingInterceptor)

    // Dagger Hilt
    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    testImplementation(Dep.Test.junit)
    androidTestImplementation(Dep.AndroidX.room.testing)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)
    androidTestImplementation(Dep.Kotlin.coroutines.test)
}

kapt {
    correctErrorTypes = true
}

//apply(from = "../publish_local.gradle")
