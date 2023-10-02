import com.pluu.convention.Const
import com.pluu.convention.java
import com.pluu.convention.kotlin
import org.gradle.api.Plugin
import org.gradle.api.Project

class JavaLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("java-library")
                apply("org.jetbrains.kotlin.jvm")
            }

            java {
                sourceCompatibility = Const.JAVA_VERSION
                targetCompatibility = Const.JAVA_VERSION
            }

            kotlin {
                jvmToolchain(Const.JDK_VERSION)
            }
        }
    }
}