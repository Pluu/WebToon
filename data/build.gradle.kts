plugins {
    id("plugins.android-library")
    kotlinKapt()
}

android {
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mapOf(
                    "room.schemaLocation" to "$projectDir/build/schemas",
                    "room.incremental" to "true"
                )
            }
        }
    }
}

dependencies {
    implementation(project(":core"))
    implementation(Dep.Kotlin.coroutinesAndroid)
    implementation(Dep.AndroidX.arch.common)
    implementation(Dep.AndroidX.arch.runtime)
    implementation(Dep.AndroidX.room.runtime)
    kapt(Dep.AndroidX.room.compiler)
    implementation(Dep.AndroidX.room.ktx)
    // OkHttp
    implementation(Dep.OkHttp.core)
}

apply(from = "../publish_local.gradle")