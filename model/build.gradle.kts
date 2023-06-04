plugins {
    id("pluu.java.library")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(libs.kotlin.serialization)
}