plugins {
    id("kotlin-library-convention")
}

dependencies {
    testImplementation(libs.junit)
}

//apply(from = "../publish_local.gradle")