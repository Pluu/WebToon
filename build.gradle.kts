task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
