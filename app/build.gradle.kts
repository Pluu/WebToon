plugins {
    id("pluu.android.application")
    id("pluu.android.hilt")
}

android {
    namespace = "com.pluu.webtoon"

    defaultConfig {
        applicationId = "com.pluu.webtoon"
        versionCode = 72
        versionName = "1.7.5"
    }

    useLibrary("android.test.mock")

    buildTypes {
        getByName("release") {
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
