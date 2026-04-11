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