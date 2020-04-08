plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.3.71"
}

repositories {
    google()
    jcenter()
}

dependencies {
    implementation("com.android.tools.build:gradle:4.0.0-beta04")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.71")
    implementation(kotlin("stdlib-jdk8"))
}
