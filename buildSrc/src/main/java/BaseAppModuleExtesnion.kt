import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.android.build.gradle.internal.dsl.DefaultConfig
import org.gradle.api.Project

fun BaseAppModuleExtension.setAppConfig() {
    compileSdkVersion(ProjectConfigurations.compileSdk)
    buildToolsVersion(ProjectConfigurations.buildTools)

    defaultConfig {
        minSdkVersion(ProjectConfigurations.minSdk)
        targetSdkVersion(ProjectConfigurations.targetSdk)
    }

    compileOptions {
        sourceCompatibility = ProjectConfigurations.javaVer
        targetCompatibility = ProjectConfigurations.javaVer
    }
}

fun BaseExtension.setDefaultConfig(
    addDefaultConfigAction: DefaultConfig.() -> Unit = {}
) {
    compileSdkVersion(ProjectConfigurations.compileSdk)
    buildToolsVersion(ProjectConfigurations.buildTools)

    defaultConfig {
        minSdkVersion(ProjectConfigurations.minSdk)
        targetSdkVersion(ProjectConfigurations.targetSdk)
        addDefaultConfigAction()
    }

    compileOptions {
        sourceCompatibility = ProjectConfigurations.javaVer
        targetCompatibility = ProjectConfigurations.javaVer
    }

    lintOptions {
        isAbortOnError = false
    }
}

fun BaseExtension.setLibraryProguard(project: Project) {
    buildTypes {
        getByName(BuildType.RELEASE) {
            isMinifyEnabled = BuildTypeRelease.isMinifyEnabled
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
            proguardFiles(project.file("proguard-rules.pro"))
        }
    }
}
