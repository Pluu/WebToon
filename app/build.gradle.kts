plugins {
    id("pluu.android.application")
    id("pluu.android.hilt")
}

android {
    namespace = "com.pluu.webtoon"

    defaultConfig {
        applicationId = "com.pluu.webtoon"
        versionCode = 73
        versionName = "1.7.6"
    }

    useLibrary("android.test.mock")

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    implementation(projects.coreAndroid)
    implementation(projects.dataLocal)
    implementation(projects.dataRemote)
    implementation(projects.uiIntro)
    implementation(projects.uiMainContainer)

    implementation(libs.timber)
    implementation(libs.errorprone)
}
