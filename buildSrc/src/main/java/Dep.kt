object Dep {
    object GradlePlugin {
        const val android = "com.android.tools.build:gradle:3.6.0-alpha02"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
        const val realm = "io.realm:realm-gradle-plugin:4.1.1"
        const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:7.4.0"
    }

    object AndroidX {
        const val annotation = "androidx.annotation:annotation:1.1.0-rc01"

        private const val activityVersion = "1.0.0-alpha08"
        const val activity = "androidx.activity:activity:$activityVersion"
        const val activityKtx = "androidx.activity:activity-ktx:$activityVersion"

        private const val fragmentVersion = "1.1.0-alpha09"
        const val fragment = "androidx.fragment:fragment:$fragmentVersion"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:$fragmentVersion"

        private const val viewModelVersion = "2.2.0-alpha01"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel:$viewModelVersion"
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$viewModelVersion"

        const val appcompat = "androidx.appcompat:appcompat:1.1.0-alpha05"
        const val coreKtx = "androidx.core:core-ktx:1.2.0-alpha01"
        const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:2.2.0-alpha01"

        object UI {
            const val recyclerview = "androidx.recyclerview:recyclerview:1.1.0-alpha05"
            const val palette = "androidx.palette:palette:1.0.0"
            const val cardview = "androidx.cardview:cardview:1.0.0"
            const val preference = "androidx.preference:preference:1.1.0-alpha05"
            const val browser = "androidx.browser:browser:1.0.0"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0-beta1"
            const val material = "com.google.android.material:material:1.1.0-alpha06"
        }
    }

    object Kotlin {
        const val version = "1.3.31"
        const val stdlibJvm = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"

        private const val coroutinesVersion = "1.2.1"
        const val coroutinesCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
        const val coroutinesAndroid =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
    }

    object Koin {
        private const val version = "1.0.2"
        const val android = "org.koin:koin-android:$version"
        const val androidViewModel = "org.koin:koin-android-viewmodel:$version"
    }

    const val jsoup = "org.jsoup:jsoup:1.12.1"

    object Glide {
        private const val version = "4.9.0"
        const val core = "com.github.bumptech.glide:glide:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object OkHttp {
        private const val version = "3.14.2"
        const val core = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object Test {
        const val junit = "junit:junit:4.12"
        const val assertJ = "org.assertj:assertj-core:3.11.1"
        const val mockito = "org.mockito:mockito-core:2.23.0"
    }
}
