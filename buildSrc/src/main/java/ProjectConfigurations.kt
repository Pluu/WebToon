import org.gradle.api.JavaVersion

object ProjectConfigurations {
    const val compileSdk = 32
    const val minSdk = 21
    const val targetSdk = 32
    const val buildTools = "32.0.0"

    val javaVer = JavaVersion.VERSION_1_8
}