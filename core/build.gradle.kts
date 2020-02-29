plugins {
    androidLibrary()
    kotlinAndroid()
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
    implementation(Dep.Kotlin.stdlibJvm)
}
