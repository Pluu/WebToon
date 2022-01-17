plugins {
    id("android-library-convention")
    id("com.google.devtools.ksp")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(projects.core)
    implementation(projects.domain)
    implementation(projects.model)

    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.Kotlin.coroutines.core)

    // Dagger Hilt
    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    testImplementation(Dep.Test.junit)
}

kapt {
    correctErrorTypes = true
}

//apply(from = "../publish_local.gradle")
