package plugins

import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsFeature

plugins {
    kotlin("android.extensions")
}

androidExtensions {
    isExperimental = true
    features = setOf(AndroidExtensionsFeature.PARCELIZE.featureName)
}
