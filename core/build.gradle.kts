plugins {
    id("com.android.library")
    kotlin("android")
}

apply(from = Scripts.androidGradlePath)

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)
}
