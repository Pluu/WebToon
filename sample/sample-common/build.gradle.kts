plugins {
    id("android-library-convention")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

dependencies {
    api(projects.core)
    api(projects.coreAndroid)
    implementation(projects.data)
    implementation(projects.dataLocal)
    implementation(projects.dataRemote)
    implementation(projects.domain)
    api(projects.uiCommon)

    // Hilt
    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    // OkHttp
    implementation(Dep.OkHttp.loggingInterceptor)

    // Kotlin
    implementation(Dep.Kotlin.coroutines.android)

    api(Dep.timber)
}

kapt {
    correctErrorTypes = true
}

hilt {
    enableAggregatingTask = true
}
