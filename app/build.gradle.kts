import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsFeature
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    androidApp()
    kotlinAndroid()
    kotlinAndroidExtensions()
    daggerHilt()
    kotlinKapt()
    ktlint()
}

android {
    setAppConfig()

    defaultConfig {
        applicationId = "com.pluu.webtoon"
        versionCode = 59
        versionName = "1.6.2"
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

        getByName(BuildType.RELEASE) {
            isMinifyEnabled = BuildTypeRelease.isMinifyEnabled
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
            proguardFiles(file("proguard-rules.pro"))
        }
    }

    buildFeatures {
        viewBinding = true
    }

    lintOptions {
        checkOnly("Interoperability")
        disable("ContentDescription")
        isAbortOnError = false
    }

    useLibrary("android.test.mock")
}

androidExtensions {
    isExperimental = true
    features = setOf(AndroidExtensionsFeature.PARCELIZE.featureName)
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)

    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":site"))

    implementation(Dep.AndroidX.annotation)
    implementation(Dep.AndroidX.activity.activity)
    implementation(Dep.AndroidX.activity.ktx)
    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.arch.common)
    implementation(Dep.AndroidX.arch.runtime)
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.fragment.fragment)
    implementation(Dep.AndroidX.fragment.ktx)
    implementation(Dep.AndroidX.lifecycle.viewModel)
    implementation(Dep.AndroidX.lifecycle.viewModelKtx)
    implementation(Dep.AndroidX.lifecycle.liveData)
    implementation(Dep.AndroidX.lifecycle.liveDataKtx)
    implementation(Dep.AndroidX.room.runtime)
    kapt(Dep.AndroidX.room.compiler)
    implementation(Dep.AndroidX.room.ktx)

    // Android UI
    implementation(Dep.AndroidX.UI.browser)
    implementation(Dep.AndroidX.UI.cardview)
    implementation(Dep.AndroidX.UI.constraintLayout)
    implementation(Dep.AndroidX.UI.drawerlayout)
    implementation(Dep.AndroidX.UI.material)
    implementation(Dep.AndroidX.UI.palette)
    implementation(Dep.AndroidX.UI.preference)
    implementation(Dep.AndroidX.UI.recyclerview)
    implementation(Dep.AndroidX.UI.swiperefreshlayout)
    implementation(Dep.AndroidX.UI.viewPager)

    // OkHttp
    implementation(Dep.OkHttp.core)
    implementation(Dep.OkHttp.loggingInterceptor)

    // Dagger Hilt
    implementation(Dep.Dagger.android)
    kapt(Dep.Dagger.hilt_compiler)
    implementation(Dep.Dagger.viewModel)
    kapt(Dep.Dagger.android_hilt_compiler)

    // Glide
    implementation(Dep.Glide.core)
    kapt(Dep.Glide.compiler)

    // kotlin
    implementation(Dep.Kotlin.coroutines.core)
    implementation(Dep.Kotlin.coroutines.android)

    implementation(Dep.timber)
//    implementation(Dep.leakCanary)

    testImplementation(Dep.Test.junit)
    testImplementation(Dep.Test.assertJ)
    testImplementation(Dep.Test.mockito)
}

kapt {
    useBuildCache = true
}

ktlint {
    debug.set(true)
    verbose.set(true)
    android.set(true)
    outputToConsole.set(true)
    outputColorName.set("RED")
    ignoreFailures.set(true)
    reporters {
        reporter(ReporterType.CHECKSTYLE)
    }
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}
