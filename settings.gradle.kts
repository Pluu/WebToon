include(
    ":app",
    ":data",
    ":core",
    ":core-android",
    ":domain",
    ":site",
    ":ui-common",
    ":ui-weekly",
    ":ui-episode",
    ":ui-setting"
)

arrayOf(
    ":ui-common",
    ":ui-weekly",
    ":ui-setting",
    ":ui-episode"
).forEach { name ->
    project(name).projectDir = File(rootDir, "features/${name.substring(startIndex = 1)}")
}
