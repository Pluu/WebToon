plugins {
    androidLibrary()
    kotlinAndroid()
}

android {
    setDefaultConfig()

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
    testImplementation(Dep.Test.junit)
}

apply(from = "../publish_local.gradle")