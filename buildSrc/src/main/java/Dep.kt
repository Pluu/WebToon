@file:Suppress("ClassName")

object Dep {
    object GradlePlugin {
        const val android = "com.android.tools.build:gradle:4.1.0-alpha06"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
        const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:9.2.1"
    }

    object AndroidX {
        const val annotation = "androidx.annotation:annotation:1.1.0"

        private const val activityVersion = "1.2.0-alpha03"
        const val activity = "androidx.activity:activity:$activityVersion"
        const val activityKtx = "androidx.activity:activity-ktx:$activityVersion"

        object arch {
            private const val version = "2.1.0"
            const val common = "androidx.arch.core:core-common:$version"
            const val runtime = "androidx.arch.core:core-runtime:$version"
        }

        private const val fragmentVersion = "1.3.0-alpha03"
        const val fragment = "androidx.fragment:fragment:$fragmentVersion"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:$fragmentVersion"

        private const val lifecycleVersion = "2.3.0-alpha01"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel:$lifecycleVersion"
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
        const val liveData = "androidx.lifecycle:lifecycle-livedata:$lifecycleVersion"
        const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"

        object room {
            private const val roomVersion = "2.2.5"
            const val runtime = "androidx.room:room-runtime:$roomVersion"
            const val compiler = "androidx.room:room-compiler:$roomVersion"
            const val ktx = "androidx.room:room-ktx:$roomVersion"
        }

        const val appcompat = "androidx.appcompat:appcompat:1.2.0-beta01"
        const val coreKtx = "androidx.core:core-ktx:1.3.0-beta01"

        object UI {
            const val recyclerview = "androidx.recyclerview:recyclerview:1.2.0-alpha02"
            const val palette = "androidx.palette:palette:1.0.0"
            const val cardview = "androidx.cardview:cardview:1.0.0"
            const val preference = "androidx.preference:preference:1.1.0"
            const val browser = "androidx.browser:browser:1.3.0-alpha01"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0-beta4"
            const val viewPager = "androidx.viewpager2:viewpager2:1.0.0"
            const val material = "com.google.android.material:material:1.2.0-alpha05"
            const val swiperefreshlayout =
                "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0-beta01"
        }
    }

    object Kotlin {
        const val version = "1.3.72"
        const val stdlibJvm = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"

        private const val coroutinesVersion = "1.3.5"
        const val coroutinesCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
        const val coroutinesAndroid =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
    }

    object Koin {
        private const val version = "2.1.5"
        const val android = "org.koin:koin-android-viewmodel:$version"
    }

    const val jsoup = "org.jsoup:jsoup:1.13.1"

    object Glide {
        private const val version = "4.11.0"
        const val core = "com.github.bumptech.glide:glide:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object OkHttp {
        private const val version = "4.5.0"
        const val core = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    const val timber = "com.jakewharton.timber:timber:4.7.1"

    object Test {
        const val junit = "junit:junit:4.13"
        const val assertJ = "org.assertj:assertj-core:3.15.0"
        const val mockito = "org.mockito:mockito-core:3.3.3"
    }
}
