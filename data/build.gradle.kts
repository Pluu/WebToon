plugins {
    androidLibrary()
    kotlinAndroid()
    kotlinKapt()
}

android {
    setDefaultRoomConfig(project)

    buildTypes {
        getByName(BuildType.RELEASE) {
            isMinifyEnabled = BuildTypeRelease.isMinifyEnabled
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
            proguardFiles(file("proguard-rules.pro"))
        }
    }

    lintOptions {
        isAbortOnError = false
    }
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