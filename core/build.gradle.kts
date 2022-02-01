plugins {
    id("kotlin-library-convention")
}

dependencies {
    testImplementation(Dep.Test.junit)
}

//apply(from = "../publish_local.gradle")