plugins {
    id("kotlin-library-convention")
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)
    testImplementation(Dep.Test.junit)
}

//apply(from = "../publish_local.gradle")