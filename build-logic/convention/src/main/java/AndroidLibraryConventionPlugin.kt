import com.android.build.gradle.LibraryExtension
import com.pluu.convention.configureAndroid
import com.pluu.convention.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            configureAndroid()

            extensions.configure<LibraryExtension> {
                configureKotlin(this)

                lint {
                    checkOnly.add("Interoperability")
                    disable.add("ContentDescription")
                    abortOnError = false
                }
            }
        }
    }
}