import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project

fun BaseAppModuleExtension.setDefaultSigningConfigs(project: Project) = signingConfigs {
    getByName("debug") {
        storeFile = project.rootProject.file("debug.keystore")
        storePassword = "android"
        keyAlias = "androiddebugkey"
        keyPassword = "android"
    }
}