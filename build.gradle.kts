import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    addScriptRepository()
    addScriptDependencies()
}

val javaVersion = JavaVersion.VERSION_1_8

allprojects {
    addScriptRepository()

    tasks.withType<KotlinCompile>().configureEach {
        sourceCompatibility = javaVersion.name
        targetCompatibility = javaVersion.name

        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
