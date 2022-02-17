@file:Suppress("ClassName")

object Dep {
    object GradlePlugin {
        const val androidStudioGradlePluginVersion = "7.2.0-beta02"
        const val android = "com.android.tools.build:gradle:$androidStudioGradlePluginVersion"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
        const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:10.2.0"
        const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Dagger.version}"
    }

    object AndroidX {
        object Activity {
            const val activityVersion = "1.5.0-alpha02"
            const val activity = "androidx.activity:activity:$activityVersion"
            const val ktx = "androidx.activity:activity-ktx:$activityVersion"
        }

        object Arch {
            const val testing = "androidx.arch.core:core-testing:2.1.0"
        }

        const val appcompat = "androidx.appcompat:appcompat:1.4.1"
        const val coreKtx = "androidx.core:core-ktx:1.8.0-alpha04"
        const val splashScreen = "androidx.core:core-splashscreen:1.0.0-beta01"

        object Fragment {
            private const val fragmentVersion = "1.5.0-alpha02"
            const val fragment = "androidx.fragment:fragment:$fragmentVersion"
            const val ktx = "androidx.fragment:fragment-ktx:$fragmentVersion"
        }

        object Lifecycle {
            const val lifecycleVersion = "2.5.0-alpha02"
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
            const val runtimeTesting = "androidx.lifecycle:lifecycle-runtime-testing:$lifecycleVersion"
        }

        object Room {
            private const val roomVersion = "2.4.1"
            const val runtime = "androidx.room:room-runtime:$roomVersion"
            const val compiler = "androidx.room:room-compiler:$roomVersion"
            const val ktx = "androidx.room:room-ktx:$roomVersion"
            const val testing = "androidx.room:room-testing:$roomVersion"
        }

        object UI {
            const val browser = "androidx.browser:browser:1.4.0"
            const val palette = "androidx.palette:palette:1.0.0"
            const val preference = "androidx.preference:preference:1.2.0"
            const val recyclerview = "androidx.recyclerview:recyclerview:1.3.0-alpha01"
            const val window = "androidx.window:window:1.0.0"
        }

        object Paging {
            const val runtime = "androidx.paging:paging-runtime:3.1.0"
            const val compose = "androidx.paging:paging-compose:1.0.0-alpha14"
        }

        object Hilt {
            const val compose = "androidx.hilt:hilt-navigation-compose:1.0.0"
        }

        object Navigation {
            const val compose = "androidx.navigation:navigation-compose:2.5.0-alpha02"
        }

        object Compose {
            const val version = "1.2.0-alpha03"

            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val ui = "androidx.compose.ui:ui:${version}"
            const val material = "androidx.compose.material3:material3:1.0.0-alpha05"
            const val livedata = "androidx.compose.runtime:runtime-livedata:$version"
            const val animation = "androidx.compose.animation:animation:$version"
            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val tooling = "androidx.compose.ui:ui-tooling:${version}"
            const val toolingPreview = "androidx.compose.ui:ui-tooling-preview:${version}"

            const val activity = "androidx.activity:activity-compose:${AndroidX.Activity.activityVersion}"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.0.0"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${AndroidX.Lifecycle.lifecycleVersion}"
        }

        const val viewBinding =
            "androidx.databinding:viewbinding:${GradlePlugin.androidStudioGradlePluginVersion}"
    }

    object Accompanist {
        private const val version = "0.24.2-alpha"
        const val insets = "com.google.accompanist:accompanist-insets:$version"
        const val pager = "com.google.accompanist:accompanist-pager:$version"
        const val pagerIndicators = "com.google.accompanist:accompanist-pager-indicators:$version"
        const val systemUi = "com.google.accompanist:accompanist-systemuicontroller:$version"
    }

    object Dagger {
        const val version = "2.41"
        const val dagger = "com.google.dagger:dagger:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"

        object Hilt {
            const val android = "com.google.dagger:hilt-android:$version"
            const val compiler = "com.google.dagger:hilt-compiler:$version"
        }
    }

    object Kotlin {
        const val version = "1.6.10"

        object Coroutines {
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

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val assertJ = "org.assertj:assertj-core:3.22.0"
        const val mockito = "org.mockito:mockito-core:3.12.4"
        const val androidJunit = "androidx.test.ext:junit:1.1.3"
        const val espressoCore = "androidx.test.espresso:espresso-core:3.4.0"
    }
}
