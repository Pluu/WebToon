//import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("android-application-convention")
    id("android-compose-convention")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
//    id("org.jlleitschuh.gradle.ktlint")
}

android {
    defaultConfig {
        applicationId = "com.pluu.webtoon"
        versionCode = 70
        versionName = "1.7.3"
        vectorDrawables.useSupportLibrary = true
    }

    lint {
        checkOnly.add("Interoperability")
        disable.add("ContentDescription")
//        isAbortOnError = false
        xmlReport = true
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
    implementation(Dep.AndroidX.UI.browser)
    implementation(Dep.AndroidX.Navigation.compose)
    implementation(Dep.AndroidX.Hilt.compose)

    // Compose
    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.tooling)

    implementation(Dep.Coil.compose)
    implementation(Dep.Accompanist.insets)
    implementation(Dep.Accompanist.systemUi)

    // OkHttp
    implementation(Dep.OkHttp.loggingInterceptor)

    // Hilt
    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    // kotlin
    implementation(Dep.Kotlin.Coroutines.core)
    implementation(Dep.Kotlin.Coroutines.android)

    implementation(Dep.timber)
//    implementation(Dep.leakCanary)
    implementation(Dep.Coil.core)

    testImplementation(Dep.Test.junit)
    testImplementation(Dep.Test.assertJ)
    testImplementation(Dep.Test.mockito)
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
