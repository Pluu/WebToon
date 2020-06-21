include(
    ":app",
    ":data",
    ":core",
    ":core-android",
    ":domain",
    ":site",
    ":ui-feature-common"
)

arrayOf(
    ":ui-feature-common"
).forEach { name ->
    project(name).projectDir = File(rootDir, "features/${name.substring(startIndex = 1)}")
}