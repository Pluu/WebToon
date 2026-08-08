package com.pluu.convention

///////////////////////////////////////////////////////////////////////////
// Origin : https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:buildSrc/public/src/main/kotlin/androidx/build/VersionCatalogExtensions.kt
///////////////////////////////////////////////////////////////////////////

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.libs: VersionCatalog
    get() = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

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
