buildscript {
    addScriptRepository()
    addScriptDependencies()
}

allprojects {
    addScriptRepository()

    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
        kotlinOptions.jvmTarget = ProjectConfigurations.javaVerString
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
