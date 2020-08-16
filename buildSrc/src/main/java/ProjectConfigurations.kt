import org.gradle.api.JavaVersion

object ProjectConfigurations {
    const val compileSdk = 30
    const val minSdk = 21
    const val targetSdk = 30
    const val buildTools = "30.0.2"

    val javaVer = JavaVersion.VERSION_1_8
    const val javaVerString = "1.8"
}