import com.pluu.convention.implementation
import com.pluu.convention.ksp
import com.pluu.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
                apply("dagger.hilt.android.plugin")
            }

            dependencies {
                // Hilt
                implementation(libs.findLibrary("dagger-hilt-android"))
                ksp(libs.findLibrary("dagger-hilt-compiler"))
            }
        }
    }
}