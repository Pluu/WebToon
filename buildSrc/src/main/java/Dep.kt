@file:Suppress("ClassName")

object Dep {
    object GradlePlugin {
        const val androidStudioGradlePluginVersion = "7.0.0-alpha14"
        const val android = "com.android.tools.build:gradle:$androidStudioGradlePluginVersion"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
        const val kotlinSerialization = "org.jetbrains.kotlin:kotlin-serialization:${Kotlin.version}"
        const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:10.0.0"
        const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Dagger.Hilt.version}"
    }

    object AndroidX {
        const val annotation = "androidx.annotation:annotation:1.3.0-alpha01"

        object activity {
            const val activityVersion = "1.3.0-alpha07"
            const val activity = "androidx.activity:activity:$activityVersion"
            const val ktx = "androidx.activity:activity-ktx:$activityVersion"
        }

        object arch {
            const val testing = "androidx.arch.core:core-testing:2.1.0"
        }

        const val appcompat = "androidx.appcompat:appcompat:1.3.0-rc01"
        const val coreKtx = "androidx.core:core-ktx:1.6.0-alpha02"

        object fragment {
            private const val fragmentVersion = "1.3.3"
            const val fragment = "androidx.fragment:fragment:$fragmentVersion"
            const val ktx = "androidx.fragment:fragment-ktx:$fragmentVersion"
        }

        object lifecycle {
            private const val lifecycleVersion = "2.3.1"
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
            const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
            const val runtimeTesting = "androidx.lifecycle:lifecycle-runtime-testing:$lifecycleVersion"
        }

        object room {
            private const val roomVersion = "2.4.0-alpha01"
            const val runtime = "androidx.room:room-runtime:$roomVersion"
            const val compiler = "androidx.room:room-compiler:$roomVersion"
            const val ktx = "androidx.room:room-ktx:$roomVersion"
            const val testing = "androidx.room:room-testing:$roomVersion"
        }

        object UI {
            const val browser = "androidx.browser:browser:1.3.0"
            const val material = "com.google.android.material:material:1.4.0-alpha01"
            const val palette = "androidx.palette:palette:1.0.0"
            const val preference = "androidx.preference:preference:1.1.1"
            const val recyclerview = "androidx.recyclerview:recyclerview:1.2.0"
            const val viewPager = "androidx.viewpager2:viewpager2:1.1.0-alpha01"
        }

        object Compose {
            const val version = "1.0.0-beta05"

            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val foundation = "androidx.compose.foundation:foundation:${version}"
            const val ui = "androidx.compose.ui:ui:${version}"
            const val layout = "androidx.compose.foundation:foundation-layout:${version}"
            const val material = "androidx.compose.material:material:${version}"
            const val materialAdapter = "com.google.android.material:compose-theme-adapter:${version}"
            const val tooling = "androidx.compose.ui:ui-tooling:${version}"
            const val livedata = "androidx.compose.runtime:runtime-livedata:$version"
            const val animation = "androidx.compose.animation:animation:$version"

            const val activity = "androidx.activity:activity-compose:${AndroidX.activity.activityVersion}"
            const val constraintlayout = "androidx.constraintlayout:constraintlayout-compose:1.0.0-alpha05"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha04"
        }

        const val viewBinding =
            "androidx.databinding:viewbinding:${GradlePlugin.androidStudioGradlePluginVersion}"
    }

    object Accompanist {
        private const val version = "0.8.0"
        const val glide = "com.google.accompanist:accompanist-glide:$version"
        const val insets = "com.google.accompanist:accompanist-insets:$version"
        const val pager = "com.google.accompanist:accompanist-pager:$version"
    }

    object Dagger {
        private const val version = "2.34.1"
        const val dagger = "com.google.dagger:dagger:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"

        object Hilt {
            const val version = "${Dagger.version}-beta"
            const val android = "com.google.dagger:hilt-android:$version"
            const val compiler = "com.google.dagger:hilt-compiler:$version"
            const val android_testing = "com.google.dagger:hilt-android-testing:$version"
        }
    }

    object Kotlin {
        const val version = "1.4.32"
        const val stdlibJvm = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"

        object coroutines {
            private const val coroutinesVersion = "1.4.3"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
            const val android =
                "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion"
        }

        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0"
    }

    const val jsoup = "org.jsoup:jsoup:1.13.1"

    object Glide {
        private const val version = "4.12.0"
        const val core = "com.github.bumptech.glide:glide:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object OkHttp {
        private const val version = "4.9.1"
        const val core = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.6"

    object Test {
        const val junit = "junit:junit:4.13.1"
        const val assertJ = "org.assertj:assertj-core:3.18.1"
        const val mockito = "org.mockito:mockito-core:3.7.0"
        const val androidJunit = "androidx.test.ext:junit:1.1.3-alpha03"
        const val espressoCore = "androidx.test.espresso:espresso-core:3.4.0-alpha03"
    }
}
