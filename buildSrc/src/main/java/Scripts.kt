//// buildscript
//fun ScriptHandler.addScriptRepository() {
//    repositories {
//        addScriptDependencies()
//    }
//}
//
//// allprojects
//fun Project.addScriptRepository() {
//    repositories {
//        addScriptDependencies()
//    }
//}
//
//private fun RepositoryHandler.addScriptDependencies() {
//    google {
//        content {
//            includeGroupByRegex("com.android.*")
//            includeGroupByRegex("androidx.*")
//            includeGroupByRegex("com.google.*")
//        }
//    }
//    mavenCentral()
//    maven("https://plugins.gradle.org/m2/") {
//        content {
//            includeGroup("org.jlleitschuh.gradle")
//        }
//    }
//}
//
//fun ScriptHandlerScope.addScriptDependencies() {
//    dependencies {
//        classpath(Dep.GradlePlugin.android)
//        classpath(Dep.GradlePlugin.kotlin)
//        classpath(Dep.GradlePlugin.kotlinSerialization)
//        classpath(Dep.GradlePlugin.ktlint)
//        classpath(Dep.GradlePlugin.hilt)
//    }
//}
