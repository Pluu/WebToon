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
}