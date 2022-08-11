package com.pluu.convention

import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import java.util.Optional

internal fun <T : Any> DependencyHandlerScope.implementation(dependencyNotation: Optional<Provider<T>>) {
    dependencies.add("implementation", dependencyNotation.get())
}

internal fun <T : Any> DependencyHandlerScope.debugImplementation(dependencyNotation: Optional<Provider<T>>) {
    dependencies.add("debugImplementation", dependencyNotation.get())
}

internal fun <T : Any> DependencyHandlerScope.kapt(dependencyNotation: Optional<Provider<T>>) {
    dependencies.add("kapt", dependencyNotation.get())
}
