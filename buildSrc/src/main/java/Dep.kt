@file:Suppress("ClassName")

object Dep {
    object GradlePlugin {
        const val androidStudioVersion = "4.2.0-alpha02"
        const val android = "com.android.tools.build:gradle:$androidStudioVersion"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
        const val kotlinSerialization = "org.jetbrains.kotlin:kotlin-serialization:${Kotlin.version}"
        const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:9.2.1"
        const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Hilt.version}"
    }

    object AndroidX {
        const val annotation = "androidx.annotation:annotation:1.2.0-alpha01"

        object activity {
            private const val activityVersion = "1.2.0-alpha06"
            const val activity = "androidx.activity:activity:$activityVersion"
            const val ktx = "androidx.activity:activity-ktx:$activityVersion"
        }

        const val appcompat = "androidx.appcompat:appcompat:1.3.0-alpha01"
        const val coreKtx = "androidx.core:core-ktx:1.5.0-alpha01"

        object fragment {
            private const val fragmentVersion = "1.3.0-alpha06"
            const val fragment = "androidx.fragment:fragment:$fragmentVersion"
            const val ktx = "androidx.fragment:fragment-ktx:$fragmentVersion"
        }

        object lifecycle {
            private const val lifecycleVersion = "2.3.0-alpha04"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel:$lifecycleVersion"
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
            const val liveData = "androidx.lifecycle:lifecycle-livedata:$lifecycleVersion"
            const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
        }

        object room {
            private const val roomVersion = "2.3.0-alpha01"
            const val runtime = "androidx.room:room-runtime:$roomVersion"
            const val compiler = "androidx.room:room-compiler:$roomVersion"
            const val ktx = "androidx.room:room-ktx:$roomVersion"
            const val testing = "androidx.room:room-testing:$roomVersion"
        }

        object UI {
            const val browser = "androidx.browser:browser:1.3.0-alpha03"
            const val cardview = "androidx.cardview:cardview:1.0.0"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0-beta6"
            const val drawerlayout = "androidx.drawerlayout:drawerlayout:1.1.0-rc01"
            const val material = "com.google.android.material:material:1.3.0-alpha01"
            const val swiperefreshlayout =
                "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0-rc01"
            const val palette = "androidx.palette:palette:1.0.0"
            const val preference = "androidx.preference:preference:1.1.1"
            const val recyclerview = "androidx.recyclerview:recyclerview:1.2.0-alpha03"
            const val viewPager = "androidx.viewpager2:viewpager2:1.1.0-alpha01"
        }

        const val viewBinding = "androidx.databinding:viewbinding:${GradlePlugin.androidStudioVersion}"
    }

    object Dagger {
        private const val version = "2.28.1"
        const val dagger = "com.google.dagger:dagger:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"
    }

    object Hilt {
        const val version = "2.28-alpha"
        const val android = "com.google.dagger:hilt-android:${version}"
        const val hilt_compiler = "com.google.dagger:hilt-android-compiler:${version}"
        const val testing = "com.google.dagger:hilt-android-testing:${version}"

        private const val androidX = "1.0.0-alpha01"
        const val hilt_common = "androidx.hilt:hilt-common:${androidX}"
        const val viewModel = "androidx.hilt:hilt-lifecycle-viewmodel:${androidX}"
        const val android_hilt_compiler = "androidx.hilt:hilt-compiler:${androidX}"
    }

    object Kotlin {
        const val version = "1.3.72"
        const val stdlibJvm = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"

        object coroutines {
            private const val coroutinesVersion = "1.3.7"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
            const val android =
                "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion"
        }

        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0"
    }

    const val jsoup = "org.jsoup:jsoup:1.13.1"

    object Glide {
        private const val version = "4.11.0"
        const val core = "com.github.bumptech.glide:glide:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object OkHttp {
        private const val version = "4.7.2"
        const val core = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.3"

    object Test {
        const val junit = "junit:junit:4.13"
        const val assertJ = "org.assertj:assertj-core:3.16.1"
        const val mockito = "org.mockito:mockito-core:3.3.3"
        const val androidJunit = "androidx.test.ext:junit:1.1.2-rc01"
        const val espressoCore = "androidx.test.espresso:espresso-core:3.3.0-rc01"
    }
}
