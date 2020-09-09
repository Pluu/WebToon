plugins {
    id("java-library")
    kotlin("jvm")
}

java {
    sourceCompatibility = ProjectConfigurations.javaVer
    targetCompatibility = ProjectConfigurations.javaVer
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)
    testImplementation(Dep.Test.junit)

    api(Dep.Dagger.dagger)
}

//apply(from = "../publish_local.gradle")