plugins {
    androidLibrary()
    kotlinAndroid()
}

apply(from = Scripts.androidGradlePath)

dependencies {
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.Kotlin.coroutinesCore)
    // Jsoup
    implementation(Dep.jsoup)
    testImplementation(Dep.Test.junit)
}
