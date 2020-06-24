plugins {
    javaLibrary()
    kotlinJvm()
}

java {
    sourceCompatibility = ProjectConfigurations.javaVer
    targetCompatibility = ProjectConfigurations.javaVer
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)
}