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

    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(Dep.Kotlin.coroutinesCore)
    // Jsoup
    implementation(Dep.jsoup)

    testImplementation(Dep.Test.junit)
}

apply(from = "../publish_local.gradle")