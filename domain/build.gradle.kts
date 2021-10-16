plugins {
    id("android-library-convention")
}

dependencies {
    implementation(projects.core)
    implementation(projects.data)
    implementation(projects.model)

    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.Dagger.dagger)

    testImplementation(Dep.Test.junit)
}

//apply(from = "../publish_local.gradle")
