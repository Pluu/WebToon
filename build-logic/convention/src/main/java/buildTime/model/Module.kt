package buildTime.model

import org.gradle.api.Project

data class Module(
    val path: String,
    val absoluteDir: String
) : java.io.Serializable {

    companion object {
        fun Project.toModule(): Module {
            return Module(
                path = path,
                absoluteDir = projectDir.absolutePath
            )
        }
    }
}