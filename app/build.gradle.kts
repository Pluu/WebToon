import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsFeature

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("org.jlleitschuh.gradle.ktlint")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "com.pluu.webtoon"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 53
        versionName = "1.5.7"
        vectorDrawables.useSupportLibrary = true
    }

    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
            proguardFiles(file("proguard-rules.pro"))
        }
    }

    buildFeatures {
        viewBinding = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    lintOptions {
        check("Interoperability")
        disable("ContentDescription")
    }

    packagingOptions {
        exclude("META-INF/atomicfu.kotlin_module")
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
    implementation(Dep.AndroidX.lifecycleExtensions)
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
    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.Kotlin.coroutinesCore)
    implementation(Dep.Kotlin.coroutinesAndroid)

    testImplementation(Dep.Test.junit)
    testImplementation(Dep.Test.assertJ)
    testImplementation(Dep.Test.mockito)
}

kapt {
    useBuildCache = true
}

androidExtensions {
    isExperimental = true
    features = setOf(AndroidExtensionsFeature.PARCELIZE.featureName)
}

ktlint {
    android.set(true)
    debug.set(true)
    verbose.set(true)
    reporters.set(listOf(ReporterType.CHECKSTYLE))
    ignoreFailures.set(true)
}
