import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    addScriptRepository()
    addScriptDependencies()
}

allprojects {
    addScriptRepository()

    tasks.withType(KotlinCompile::class).configureEach {
        kotlinOptions.jvmTarget = ProjectConfigurations.javaVerString
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
