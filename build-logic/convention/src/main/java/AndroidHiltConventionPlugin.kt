import com.pluu.convention.findVersionCatalog
import com.pluu.convention.implementation
import com.pluu.convention.kapt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("kotlin-kapt")
                apply("dagger.hilt.android.plugin")
            }

            val libs = findVersionCatalog()

            dependencies {
                // Hilt
                implementation(libs.findLibrary("dagger-hilt-android"))
                kapt(libs.findLibrary("dagger-hilt-compiler"))
            }

            kapt {
                correctErrorTypes = true
            }
        }
    }
}