import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.dsl.ScriptHandler
import org.gradle.kotlin.dsl.ScriptHandlerScope
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories

fun ScriptHandler.addScriptRepository() {
    repositories {
        addScriptDependencies()
    }
}

fun Project.addScriptRepository() {
    repositories {
        addScriptDependencies()
    }
}

private fun RepositoryHandler.addScriptDependencies() {
    google()
    jcenter()
    maven("https://plugins.gradle.org/m2/")
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

fun ScriptHandlerScope.addScriptDependencies() {
    dependencies {
        classpath(Dep.GradlePlugin.android)
        classpath(Dep.GradlePlugin.kotlin)
        classpath(Dep.GradlePlugin.ktlint)
    }
}
