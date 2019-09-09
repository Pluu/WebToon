buildscript {
    addScriptRepository()
    addScriptDependencies()
}

allprojects {
    addScriptRepository()
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
