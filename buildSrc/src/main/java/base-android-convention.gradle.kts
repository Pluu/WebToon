import com.android.build.gradle.BaseExtension

configure<BaseExtension> {
    setCompileSdkVersion(ProjectConfigurations.compileSdk)

    defaultConfig {
        minSdk = ProjectConfigurations.minSdk
        setTargetSdkVersion(ProjectConfigurations.targetSdk)
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = ProjectConfigurations.javaVer
        targetCompatibility = ProjectConfigurations.javaVer
    }
}