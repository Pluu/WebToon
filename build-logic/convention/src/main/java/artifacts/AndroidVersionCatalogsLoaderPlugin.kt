package artifacts

///////////////////////////////////////////////////////////////////////////
// Origin : https://github.com/ganadist/minimal-reproducible-example/blob/main/build-logic/convention/src/main/kotlin/AndroidVersionCatalogsLoaderPlugin.kt
///////////////////////////////////////////////////////////////////////////

import com.pluu.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.kotlin.dsl.extra

class AndroidVersionCatalogsLoaderPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extra[MODULE_EXTRA] = libs.loadLibrary()
        }
    }

    companion object {
        const val MODULE_EXTRA = "build.modules.map"
    }
}

internal fun VersionCatalog.loadLibrary(): Map<String, String> {
    val versions = mutableMapOf<String, String>()
    libraryAliases.forEach { alias ->
        val dep = findLibrary(alias).get().get()
        if (dep.version != null) {
            versions["${dep.group}:${dep.name}"] = dep.version.toString()
        }
    }

    return versions
}
