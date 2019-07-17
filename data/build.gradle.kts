plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("realm-android")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(29)
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
            proguardFiles(file("proguard-rules.pro"))
        }
    }
}

dependencies {
    api(project(":core"))
    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.Kotlin.coroutinesCore)
    // OkHttp
    implementation(Dep.OkHttp.core)
    testImplementation(Dep.Test.junit)
}
