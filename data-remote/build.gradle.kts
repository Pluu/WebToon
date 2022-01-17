plugins {
    kotlin("plugin.serialization")
    id("android-library-convention")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreAndroid)
    implementation(projects.model)
    implementation(projects.data)

    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.Kotlin.serialization)
    implementation(Dep.Kotlin.coroutines.core)

    // Jsoup
    implementation(Dep.jsoup)

    // OkHttp
    implementation(Dep.OkHttp.core)
    implementation(Dep.OkHttp.loggingInterceptor)

    // Dagger Hilt
    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    testImplementation(Dep.Test.junit)
}

kapt {
    correctErrorTypes = true
}

//apply(from = "../publish_local.gradle")