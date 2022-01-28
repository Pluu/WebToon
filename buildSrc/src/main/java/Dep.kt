@file:Suppress("ClassName")

object Dep {
    object GradlePlugin {
        const val androidStudioGradlePluginVersion = "7.1.0"
        const val android = "com.android.tools.build:gradle:$androidStudioGradlePluginVersion"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
        const val kotlinSerialization = "org.jetbrains.kotlin:kotlin-serialization:${Kotlin.version}"
        const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:10.2.0"
        const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Dagger.version}"
    }

    object AndroidX {
        object activity {
            const val activityVersion = "1.5.0-alpha01"
            const val activity = "androidx.activity:activity:$activityVersion"
            const val ktx = "androidx.activity:activity-ktx:$activityVersion"
        }

        object arch {
            const val testing = "androidx.arch.core:core-testing:2.1.0"
        }

        const val appcompat = "androidx.appcompat:appcompat:1.4.1"
        const val coreKtx = "androidx.core:core-ktx:1.8.0-alpha03"
        const val splashScreen = "androidx.core:core-splashscreen:1.0.0-beta01"

        object fragment {
            private const val fragmentVersion = "1.5.0-alpha01"
            const val fragment = "androidx.fragment:fragment:$fragmentVersion"
            const val ktx = "androidx.fragment:fragment-ktx:$fragmentVersion"
        }

        object lifecycle {
            const val lifecycleVersion = "2.5.0-alpha01"
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
            const val runtimeTesting = "androidx.lifecycle:lifecycle-runtime-testing:$lifecycleVersion"
        }

        object room {
            private const val roomVersion = "2.4.1"
            const val runtime = "androidx.room:room-runtime:$roomVersion"
            const val compiler = "androidx.room:room-compiler:$roomVersion"
            const val ktx = "androidx.room:room-ktx:$roomVersion"
            const val testing = "androidx.room:room-testing:$roomVersion"
        }

        object UI {
            const val browser = "androidx.browser:browser:1.4.0"
            const val material = "com.google.android.material:material:1.5.0"
            const val palette = "androidx.palette:palette:1.0.0"
            const val preference = "androidx.preference:preference:1.2.0-alpha02"
            const val recyclerview = "androidx.recyclerview:recyclerview:1.3.0-alpha01"
        }

        object Paging {
            const val runtime = "androidx.paging:paging-runtime:3.1.0"
            const val compose = "androidx.paging:paging-compose:1.0.0-alpha14"
        }

        object Compose {
            const val version = "1.2.0-alpha02"

            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val ui = "androidx.compose.ui:ui:${version}"
            const val material = "androidx.compose.material:material:${version}"
            const val tooling = "androidx.compose.ui:ui-tooling:${version}"
            const val livedata = "androidx.compose.runtime:runtime-livedata:$version"
            const val animation = "androidx.compose.animation:animation:$version"

            const val activity = "androidx.activity:activity-compose:${AndroidX.activity.activityVersion}"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.0.0"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${AndroidX.lifecycle.lifecycleVersion}"

            const val materialAdapter = "com.google.android.material:compose-theme-adapter:1.1.3"
        }

        const val viewBinding =
            "androidx.databinding:viewbinding:${GradlePlugin.androidStudioGradlePluginVersion}"
    }

    object Accompanist {
        private const val version = "0.24.1-alpha"
        const val insets = "com.google.accompanist:accompanist-insets:$version"
        const val pager = "com.google.accompanist:accompanist-pager:$version"
        const val pagerIndicators = "com.google.accompanist:accompanist-pager-indicators:$version"
    }

    object Dagger {
        const val version = "2.40.5"
        const val dagger = "com.google.dagger:dagger:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"

        object Hilt {
            const val android = "com.google.dagger:hilt-android:$version"
            const val compiler = "com.google.dagger:hilt-compiler:$version"
        }
    }

    object Kotlin {
        const val version = "1.6.10"
        const val stdlibJvm = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"

        object coroutines {
            private const val coroutinesVersion = "1.6.0"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
            const val android =
                "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion"
        }

        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2"
    }

    const val jsoup = "org.jsoup:jsoup:1.14.3"

    object Coil {
        private const val version = "1.4.0"
        const val core = "io.coil-kt:coil:$version"
        const val compose = "io.coil-kt:coil-compose:$version"
    }

    object OkHttp {
        private const val version = "4.9.3"
        const val core = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    const val timber = "com.jakewharton.timber:timber:5.0.1"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.8.1"

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val assertJ = "org.assertj:assertj-core:3.22.0"
        const val mockito = "org.mockito:mockito-core:3.12.4"
        const val androidJunit = "androidx.test.ext:junit:1.1.3"
        const val espressoCore = "androidx.test.espresso:espresso-core:3.4.0"
    }
}
