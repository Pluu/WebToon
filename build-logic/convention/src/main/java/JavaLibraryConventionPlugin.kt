import com.pluu.convention.Const
import com.pluu.convention.java
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

class JavaLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("java-library")
                apply("org.jetbrains.kotlin.jvm")
            }

            java {
                sourceCompatibility = Const.JAVA_VERSION
                targetCompatibility = Const.JAVA_VERSION
            }

        }

        project.plugins.forEach { plugin ->
            if (plugin is KotlinBasePluginWrapper) {
                project.afterEvaluate {
                    project.tasks.withType(KotlinJvmCompile::class.java).configureEach {
                        compilerOptions.jvmTarget.set(Const.JVM_TARGET)
                    }
                }
            }
        }
    }
}