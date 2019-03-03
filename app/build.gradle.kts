import com.android.build.gradle.AppExtension
import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsExtension
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
        versionCode = 44
        versionName = "1.4.0"
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

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    lintOptions {
        check("Interoperability")
    }

    packagingOptions {
        exclude("META-INF/main.kotlin_module")
    }

    useLibrary("android.test.mock")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("androidx.annotation:annotation:1.0.2")
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("androidx.recyclerview:recyclerview:1.0.0")
    implementation("androidx.palette:palette:1.0.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.preference:preference:1.0.0")
    implementation("androidx.browser:browser:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.core:core-ktx:1.0.1")
    implementation("com.google.android.material:material:1.0.0")

    val lifecycle_version = "2.0.0"
    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycle_version")

    // DI
    implementation("org.koin:koin-android:1.0.2")
    implementation("org.koin:koin-android-viewmodel:1.0.2")

    // Jsoup
    implementation("org.jsoup:jsoup:1.11.3")
    // Glide
    implementation("com.github.bumptech.glide:glide:4.8.0")
    kapt("com.github.bumptech.glide:compiler:4.8.0")
    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:3.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.11.0")
    // kotlin
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.1.1")

    testImplementation("junit:junit:4.12")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("org.mockito:mockito-core:2.23.0")
}

configure<KaptExtension> {
    useBuildCache = true
}

androidExtensions {
    // isExperimental Bug
    configure(delegateClosureOf<AndroidExtensionsExtension> {
        isExperimental = true
    })
}

configure<KtlintExtension> {
    android.set(true)
    debug.set(true)
    verbose.set(true)
    reporters.set(listOf(ReporterType.CHECKSTYLE))
    ignoreFailures.set(true)
}
