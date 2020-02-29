plugins {
    androidLibrary()
    kotlinAndroid()
    kotlinAndroidExtensions()
}

android {
    setDefaultConfig()
    useDefaultBuildTypes()

    useJava_1_8()
    compileOptions {
        kotlinOptions {
            jvmTarget = ProjectConfigurations.kotlinJvmTarget
        }
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(Dep.Kotlin.stdlibJvm)
    testImplementation(Dep.Test.junit)
}

androidExtensions {
    isExperimental = true
}
