plugins {
    androidLibrary()
    kotlinAndroid()
    kotlinKapt()
}

android {
    setDefaultConfig()
    useDefaultBuildTypes()

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

    useJava_1_8()
    compileOptions {
        kotlinOptions {
            jvmTarget = ProjectConfigurations.kotlinJvmTarget
        }
    }
}

dependencies {
    implementation(project(":core"))
    implementation(Dep.Kotlin.stdlibJvm)
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
