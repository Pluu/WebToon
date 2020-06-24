include(
    ":app",
    ":data",
    ":core",
    ":core-android",
    ":model",
    ":domain",
    ":site",
    ":ui-common",
    ":ui-weekly",
    ":ui-episode",
    ":ui-setting",
    ":ui-detail"
)

arrayOf(
    ":ui-common",
    ":ui-weekly",
    ":ui-setting",
    ":ui-episode",
    ":ui-detail"
).forEach { name ->
    project(name).projectDir = File(rootDir, "features/${name.substring(startIndex = 1)}")
}
