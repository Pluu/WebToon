object Dep {
    object GradlePlugin {
        val android = "com.android.tools.build:gradle:3.5.0-alpha13"
        val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
        val realm = "io.realm:realm-gradle-plugin:4.1.1"
        val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:7.4.0"
    }

    object AndroidX {
        val annotation = "androidx.annotation:annotation:1.1.0-beta01"
        val activity = "androidx.activity:activity:1.0.0-alpha07"
        val activityKtx = "androidx.activity:activity-ktx:1.0.0-alpha07"
        val fragment = "androidx.fragment:fragment:1.1.0-alpha07"
        val fragmentKtx = "androidx.fragment:fragment-ktx:1.1.0-alpha07"
        val appcompat = "androidx.appcompat:appcompat:1.1.0-alpha04"
        val coreKtx = "androidx.core:core-ktx:1.1.0-alpha05"
        val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:2.1.0-alpha04"

        object UI {
            val recyclerview = "androidx.recyclerview:recyclerview:1.1.0-alpha04"
            val palette = "androidx.palette:palette:1.0.0"
            val cardview = "androidx.cardview:cardview:1.0.0"
            val preference = "androidx.preference:preference:1.1.0-alpha04"
            val browser = "androidx.browser:browser:1.0.0"
            val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0-alpha5"
            val material = "com.google.android.material:material:1.1.0-alpha05"
        }
    }

    object Kotlin {
        val version = "1.3.31"
        val stdlibJvm = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"

        private val coroutinesVersion = "1.2.1"
        val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
        val coroutinesAndroid =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
    }

    object Koin {
        private val version = "1.0.2"
        val android = "org.koin:koin-android:$version"
        val androidViewModel = "org.koin:koin-android-viewmodel:$version"
    }

    val jsoup = "org.jsoup:jsoup:1.11.3"

    object Glide {
        private val version = "4.9.0"
        val core = "com.github.bumptech.glide:glide:$version"
        val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object OkHttp {
        private val version = "3.14.1"
        val core = "com.squareup.okhttp3:okhttp:$version"
        val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object Test {
        val junit = "junit:junit:4.12"
        val assertJ = "org.assertj:assertj-core:3.11.1"
        val mockito = "org.mockito:mockito-core:2.23.0"
    }
}
