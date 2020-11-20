import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsFeature
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("org.jlleitschuh.gradle.ktlint")
}

apply(from = "${rootProject.projectDir}/gradle/commonConfiguration.gradle")

android {
    defaultConfig {
        applicationId = "com.pluu.webtoon"
        versionCode = 62
        versionName = "1.6.5"
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
        compose = true
    }

    composeOptions {
        kotlinCompilerVersion = Dep.Kotlin.version
        kotlinCompilerExtensionVersion = Dep.AndroidX.Compose.version
    }

    lintOptions {
        checkOnly("Interoperability")
        disable("ContentDescription")
        isAbortOnError = false
    }

    useLibrary("android.test.mock")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core-android"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":site"))
    implementation(project(":compose"))
    implementation(project(":ui-common"))
    implementation(project(":ui-weekly"))
    implementation(project(":ui-episode"))
    implementation(project(":ui-detail"))
    implementation(project(":ui-setting"))

    implementation(Dep.Kotlin.stdlibJvm)

    implementation(Dep.AndroidX.annotation)
    implementation(Dep.AndroidX.activity.ktx)
    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.fragment.ktx)
    implementation(Dep.AndroidX.lifecycle.viewModelKtx)
    implementation(Dep.AndroidX.lifecycle.liveDataKtx)

    // Android UI
    implementation(Dep.AndroidX.UI.constraintLayout)
    implementation(Dep.AndroidX.UI.drawerlayout)
    implementation(Dep.AndroidX.UI.material)
    implementation(Dep.AndroidX.UI.preference)
    implementation(Dep.AndroidX.UI.swiperefreshlayout)
    implementation(Dep.AndroidX.UI.viewPager)

    // Compose
    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.tooling)
    implementation(Dep.AndroidX.Compose.livedata)

    // OkHttp
    implementation(Dep.OkHttp.loggingInterceptor)

    // Hilt
    implementation(Dep.Hilt.android)
    kapt(Dep.Hilt.hilt_compiler)
    implementation(Dep.Hilt.viewModel)
    kapt(Dep.Hilt.android_hilt_compiler)

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
