import com.android.build.gradle.AppExtension
import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("realm-android")
    id("org.jlleitschuh.gradle.ktlint")
}

configure<AppExtension> {
    compileSdkVersion(28)

    defaultConfig {
        applicationId = "com.pluu.webtoon"
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 45
        versionName = "1.5.0"
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    lintOptions {
        check("Interoperability")
    }

    packagingOptions {
        exclude("META-INF/atomicfu.kotlin_module")
    }

    useLibrary("android.test.mock")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("androidx.annotation:annotation:1.1.0-beta01")
    implementation("androidx.appcompat:appcompat:1.1.0-alpha04")
    implementation("androidx.core:core-ktx:1.1.0-alpha05")

    implementation("androidx.recyclerview:recyclerview:1.1.0-alpha04")
    implementation("androidx.palette:palette:1.0.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.preference:preference:1.1.0-alpha04")
    implementation("androidx.browser:browser:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-alpha5")
    implementation("com.google.android.material:material:1.1.0-alpha05")
    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-extensions:2.1.0-alpha04")

    // DI
    implementation("org.koin:koin-android:1.0.2")
    implementation("org.koin:koin-android-viewmodel:1.0.2")

    // Jsoup
    implementation("org.jsoup:jsoup:1.11.3")
    // Glide
    implementation("com.github.bumptech.glide:glide:4.9.0")
    kapt("com.github.bumptech.glide:compiler:4.9.0")
    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:3.14.1")
    implementation("com.squareup.okhttp3:logging-interceptor:3.14.1")
    // kotlin
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.2.1")

    testImplementation("junit:junit:4.12")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("org.mockito:mockito-core:2.23.0")
}

configure<KaptExtension> {
    useBuildCache = true
}

androidExtensions {
    isExperimental = true
}

configure<KtlintExtension> {
    android.set(true)
    debug.set(true)
    verbose.set(true)
    reporters.set(listOf(ReporterType.CHECKSTYLE))
    ignoreFailures.set(true)
}
