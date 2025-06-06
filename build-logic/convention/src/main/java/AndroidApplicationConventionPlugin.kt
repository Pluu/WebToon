import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BasePlugin
import com.pluu.convention.configureAndroid
import com.pluu.convention.configureApplication
import com.pluu.convention.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            plugins.withType<BasePlugin>().configureEach {
                configureAndroid()
                configureKotlin()
            }
            plugins.withType<AppPlugin>().configureEach {
                configureApplication()
            }
        }
    }
}