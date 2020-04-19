plugins {
    androidLibrary()
    kotlinAndroid()
    kotlinKapt()
}

android {
    setDefaultConfig() {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mapOf(
                    "room.schemaLocation" to "$projectDir/build/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
    }
    setLibraryProguard(project)
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)

    implementation(project(":core"))
    implementation(Dep.Kotlin.coroutinesAndroid)
    implementation(Dep.AndroidX.arch.common)
    implementation(Dep.AndroidX.arch.runtime)
    implementation(Dep.AndroidX.room.runtime)
    kapt(Dep.AndroidX.room.compiler)
    implementation(Dep.AndroidX.room.ktx)
    // OkHttp
    implementation(Dep.OkHttp.core)

    testImplementation(Dep.Test.junit)
}

apply(from = "../publish_local.gradle")