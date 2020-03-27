import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("plugins.android-app")
    id("plugins.kotlin-android-extensions")
    kotlinKapt()
    ktlint()
}

android {
    defaultConfig {
        applicationId = "com.pluu.webtoon"
        versionCode = 54
        versionName = "1.5.8"
        vectorDrawables.useSupportLibrary = true
    }

    signingConfigs {
        getByName(BuildType.DEBUG) {
            storeFile = project.rootProject.file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        getByName(BuildType.DEBUG) {
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    buildFeatures {
        viewBinding = true
    }
//    viewBinding {
//        isEnabled = true
//    }

    lintOptions {
        check("Interoperability")
        disable("ContentDescription")
    }

    useLibrary("android.test.mock")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":site"))

    implementation(Dep.AndroidX.annotation)
    implementation(Dep.AndroidX.activity)
    implementation(Dep.AndroidX.activityKtx)
    implementation(Dep.AndroidX.arch.common)
    implementation(Dep.AndroidX.arch.runtime)
    implementation(Dep.AndroidX.fragment)
    implementation(Dep.AndroidX.fragmentKtx)
    implementation(Dep.AndroidX.viewModel)
    implementation(Dep.AndroidX.viewModelKtx)
    implementation(Dep.AndroidX.liveData)
    implementation(Dep.AndroidX.liveDataKtx)

    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.room.runtime)
    kapt(Dep.AndroidX.room.compiler)
    implementation(Dep.AndroidX.room.ktx)

    // Android UI
    implementation(Dep.AndroidX.UI.recyclerview)
    implementation(Dep.AndroidX.UI.palette)
    implementation(Dep.AndroidX.UI.cardview)
    implementation(Dep.AndroidX.UI.preference)
    implementation(Dep.AndroidX.UI.browser)
    implementation(Dep.AndroidX.UI.constraintLayout)
    implementation(Dep.AndroidX.UI.viewPager)
    implementation(Dep.AndroidX.UI.material)
    implementation(Dep.AndroidX.UI.swiperefreshlayout)

    // DI
    implementation(Dep.Koin.android)
    implementation(Dep.Koin.androidViewModel)

    // Glide
    implementation(Dep.Glide.core)
    kapt(Dep.Glide.compiler)
    // OkHttp
    implementation(Dep.OkHttp.loggingInterceptor)
    // kotlin
    implementation(Dep.Kotlin.coroutinesCore)
    implementation(Dep.Kotlin.coroutinesAndroid)

    testImplementation(Dep.Test.assertJ)
    testImplementation(Dep.Test.mockito)
}

kapt {
    useBuildCache = true
}

ktlint {
    android.set(true)
    debug.set(true)
    verbose.set(true)
    reporters.set(listOf(ReporterType.CHECKSTYLE))
    ignoreFailures.set(true)
}
