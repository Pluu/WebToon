plugins {
    androidLibrary()
    kotlinAndroid()
    kotlinKapt()
}

android {
    setDefaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mutableMapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

    setLibraryProguard(project)

    sourceSets {
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
    }

    packagingOptions {
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)

    implementation(project(":core"))
    implementation(Dep.Kotlin.coroutines.android)
    implementation(Dep.AndroidX.arch.common)
    implementation(Dep.AndroidX.arch.runtime)
    implementation(Dep.AndroidX.room.runtime)
    kapt(Dep.AndroidX.room.compiler)
    implementation(Dep.AndroidX.room.ktx)

    // OkHttp
    implementation(Dep.OkHttp.core)

    testImplementation(Dep.Test.junit)
    androidTestImplementation(Dep.AndroidX.room.testing)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)
    androidTestImplementation(Dep.Kotlin.coroutines.test)
}

apply(from = "../publish_local.gradle")