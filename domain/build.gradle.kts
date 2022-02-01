plugins {
    id("android-library-convention")
}

dependencies {
    implementation(projects.core)
    implementation(projects.model)

    implementation(Dep.Kotlin.Coroutines.core)
    implementation(Dep.Dagger.dagger)

    testImplementation(Dep.Test.junit)
}


//apply(from = "../publish_local.gradle")
