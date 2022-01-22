plugins {
    id("android-library-convention")
}

dependencies {
    implementation(projects.core)
    implementation(projects.model)

    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.Kotlin.coroutines.core)
    implementation(Dep.Dagger.dagger)

    testImplementation(Dep.Test.junit)
}


//apply(from = "../publish_local.gradle")
