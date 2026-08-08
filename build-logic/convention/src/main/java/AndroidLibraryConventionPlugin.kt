
import com.android.build.api.dsl.LibraryExtension
import com.pluu.convention.configureAndroid
import com.pluu.convention.configureJetifier
import com.pluu.convention.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("versions.checker")
            }

            extensions.configure<LibraryExtension> {
                configureAndroid(this)
                configureKotlin()
            }
            configureJetifier()
        }
    }
}