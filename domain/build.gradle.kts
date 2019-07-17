plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
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
    implementation(project(":data"))
    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.Kotlin.coroutinesCore)

    implementation(Dep.OkHttp.core)
    // Jsoup
    implementation(Dep.jsoup)

    testImplementation(Dep.Test.junit)
}

androidExtensions {
    isExperimental = true
}