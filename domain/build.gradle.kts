plugins {
    id("android-library-convention")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":model"))

    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.Dagger.dagger)

    testImplementation(Dep.Test.junit)
}

//apply(from = "../publish_local.gradle")