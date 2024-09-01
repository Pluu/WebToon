package buildTime

import org.gradle.api.Project

abstract class MeasureBuildTimeConfig(val project: Project) {

    /**
     * Enable or disable the plugin.
     */
    var enable: Boolean = false

    var outputPath: String = project.rootProject.path
}