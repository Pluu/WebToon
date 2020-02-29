import org.gradle.kotlin.dsl.kotlin
import org.gradle.plugin.use.PluginDependenciesSpec

fun PluginDependenciesSpec.androidApp() =
    id("com.android.application")

fun PluginDependenciesSpec.androidLibrary() =
    id("com.android.library")

fun PluginDependenciesSpec.kotlinAndroid() =
    kotlin("android")

fun PluginDependenciesSpec.kotlinAndroidExtensions() =
    kotlin("android.extensions")

fun PluginDependenciesSpec.kotlinKapt() =
    kotlin("kapt")

fun PluginDependenciesSpec.ktlint() =
    id("org.jlleitschuh.gradle.ktlint")