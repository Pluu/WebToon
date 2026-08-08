import com.android.build.api.dsl.ApplicationExtension
import com.pluu.convention.configureAndroid
import com.pluu.convention.configureApplication
import com.pluu.convention.configureJetifier
import com.pluu.convention.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
            }

            extensions.configure<ApplicationExtension> {
                configureApplication(this)
                configureAndroid(this)
                configureKotlin()
            }
            configureJetifier()
        }
    }
}