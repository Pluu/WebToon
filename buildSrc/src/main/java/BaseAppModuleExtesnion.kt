import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
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

fun BaseExtension.setDefaultConfig() {
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

fun BaseExtension.setDefaultRoomConfig(project: Project) {
    compileSdkVersion(ProjectConfigurations.compileSdk)
    buildToolsVersion(ProjectConfigurations.buildTools)

    defaultConfig {
        minSdkVersion(ProjectConfigurations.minSdk)
        targetSdkVersion(ProjectConfigurations.targetSdk)

        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mapOf(
                    "room.schemaLocation" to "${project.projectDir}/build/schemas",
                    "room.incremental" to "true"
                )
            }
        }
    }

    compileOptions {
        sourceCompatibility = ProjectConfigurations.javaVer
        targetCompatibility = ProjectConfigurations.javaVer
    }
}
