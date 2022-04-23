//import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("android-application-convention")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
//    id("org.jlleitschuh.gradle.ktlint")
}

android {
    namespace = "com.pluu.webtoon"

    defaultConfig {
        applicationId = "com.pluu.webtoon"
        versionCode = 70
        versionName = "1.7.3"
    }

    lint {
        checkOnly.add("Interoperability")
        disable.add("ContentDescription")
//        isAbortOnError = false
        xmlReport = true
    }

    buildFeatures.compose = true

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }

    useLibrary("android.test.mock")
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreAndroid)
    implementation(projects.model)
    implementation(projects.data)
    implementation(projects.dataLocal)
    implementation(projects.dataRemote)
    implementation(projects.domain)
    implementation(projects.compose)
    implementation(projects.uiCommon)
    implementation(projects.uiIntro)
    implementation(projects.uiWeekly)
    implementation(projects.uiEpisode)
    implementation(projects.uiDetail)
    implementation(projects.uiSetting)

    // Android UI
    implementation(libs.androidX.browser)
    implementation(libs.androidX.navigation.compose)
    implementation(libs.androidX.hilt.navigation.compose)

    // Compose
    implementation(libs.bundles.androidX.compose)
    debugImplementation(libs.bundles.androidX.compose.preview)

    implementation(libs.coil.compose)
    implementation(libs.accompanist.systemUi)

    // OkHttp
    implementation(libs.okhttp.loggingInterceptor)

    // Hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    // kotlin
    implementation(libs.kotlin.coroutine.core)
    implementation(libs.kotlin.coroutine.android)

    implementation(libs.timber)
    implementation(libs.coil.core)

    testImplementation(libs.junit)
    testImplementation(libs.assertJ)
    testImplementation(libs.mockito)
}

kapt {
    useBuildCache = true
    correctErrorTypes = true
}

//ktlint {
//    debug.set(true)
//    verbose.set(true)
//    android.set(true)
//    outputToConsole.set(true)
//    outputColorName.set("RED")
//    ignoreFailures.set(true)
//    reporters {
//        reporter(ReporterType.CHECKSTYLE)
//    }
//    filter {
//        exclude("**/generated/**")
//        include("**/kotlin/**")
//    }
//}
