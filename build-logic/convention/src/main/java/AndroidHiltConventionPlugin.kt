import com.pluu.convention.implementation
import com.pluu.convention.kapt
import dagger.hilt.android.plugin.HiltExtension
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("kotlin-kapt")
                apply("dagger.hilt.android.plugin")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                // Hilt
                implementation(libs.findLibrary("dagger-hilt-android"))
                kapt(libs.findLibrary("dagger-hilt-compiler"))
            }

            kapt {
                correctErrorTypes = true
            }

            hilt {
                enableAggregatingTask = true
            }
        }
    }
}

private fun Project.`hilt`(
    configure: Action<HiltExtension>
) {
    (this as ExtensionAware).extensions.configure("hilt", configure)
}