plugins {
    androidLibrary()
    kotlinAndroid()
}

apply(from = Scripts.androidGradlePath)

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)
}
