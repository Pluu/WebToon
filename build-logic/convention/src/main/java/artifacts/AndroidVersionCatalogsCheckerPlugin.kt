package artifacts

///////////////////////////////////////////////////////////////////////////
// Origin : https://github.com/ganadist/minimal-reproducible-example/blob/main/build-logic/convention/src/main/kotlin/AndroidVersionCatalogsCheckerPlugin.kt
///////////////////////////////////////////////////////////////////////////

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.util.internal.VersionNumber

class AndroidVersionCatalogsCheckerPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            @Suppress("UNCHECKED_CAST")
            val modules = rootProject.extra[AndroidVersionCatalogsLoaderPlugin.MODULE_EXTRA]
                as Map<String, String>

            configurations.all {
                val isIntermediatesConfiguration =
                    name.endsWith("RuntimeClasspath") ||
                        name.endsWith("CompileClasspath")
                resolutionStrategy.eachDependency {
                    val module = "${requested.group}:${requested.name}"
                    val needToCheck = isIntermediatesConfiguration ||
                        module !in CHECK_INTERMEDIATES_DEPENDENCIES_ONLY
                    if (needToCheck &&
                        module in modules &&
                        checkVersion(requested.version, modules[module])
                    ) {
                        throw GradleException(
                            "Requested library required higher version" +
                                "($module:${requested.version})\n" +
                                "than which is described at Version Catalogs" +
                                "($module:${modules[module]}).\n" +
                                "Please check your change is intended, " +
                                "then update Version Catalogs.\n"
                        )
                    }
                }
            }
        }
    }

    private fun checkVersion(version: String?, maxVersion: String?): Boolean {
        if (version == null) {
            return false
        }
        if (maxVersion == null || maxVersion == "unspecified") {
            return false
        }
        val versionNumber = VersionNumber.parse(version)
        val maxVersionNumber = VersionNumber.parse(maxVersion)
        return versionNumber > maxVersionNumber
    }

    companion object {
        // These modules are using different versions between Host and Intermediates
        // And we are describe versions for intermediates dependencies
        private val CHECK_INTERMEDIATES_DEPENDENCIES_ONLY = arrayOf(
            "org.slf4j:slf4j-api",
            // guava used on app runtime and build system,
            // and version scheme are different
            "com.google.guava:guava",
            "commons-io:commons-io",
        )
    }
}