plugins {
    id("android-library-convention")
    id("com.google.devtools.ksp")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets {
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.model)
    implementation(projects.data)

    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.Kotlin.Coroutines.android)

    implementation(Dep.AndroidX.Room.runtime)
    ksp(Dep.AndroidX.Room.compiler)
    implementation(Dep.AndroidX.Room.ktx)
    implementation(Dep.AndroidX.UI.preference)

    // Dagger Hilt
    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    testImplementation(Dep.Test.junit)
    androidTestImplementation(Dep.AndroidX.Room.testing)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)
    androidTestImplementation(Dep.Kotlin.Coroutines.test)
}

kapt {
    correctErrorTypes = true
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}

//apply(from = "../publish_local.gradle")
