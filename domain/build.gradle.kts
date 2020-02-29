plugins {
    androidLibrary()
    kotlinAndroid()
    kotlinAndroidExtensions()
}

apply(from = Scripts.androidGradlePath)

dependencies {
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(Dep.Kotlin.stdlibJvm)
    testImplementation(Dep.Test.junit)
}

androidExtensions {
    isExperimental = true
}
