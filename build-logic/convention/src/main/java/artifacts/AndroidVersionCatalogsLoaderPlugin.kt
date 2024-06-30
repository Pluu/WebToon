package artifacts

///////////////////////////////////////////////////////////////////////////
// Origin : https://github.com/ganadist/minimal-reproducible-example/blob/main/build-logic/convention/src/main/kotlin/AndroidVersionCatalogsLoaderPlugin.kt
///////////////////////////////////////////////////////////////////////////

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import java.io.File

class AndroidVersionCatalogsLoaderPlugin : Plugin<Project> {
    private val modules: MutableMap<String, String> = mutableMapOf()
    override fun apply(target: Project) {
        with(target) {
            for (filename in VERSION_CATALOGS_FILES) {
                val loader = TomlLoader(file(filename), modules)
                loader.load()
            }
            extra[MODULE_EXTRA] = modules
        }
    }

    companion object {
        private val VERSION_CATALOGS_FILES = arrayOf(
            "gradle/libs.versions.toml"
        )

        const val MODULE_EXTRA = "build.modules.map"
    }
}

internal class TomlLoader(
    private val file: File,
    private val modules: MutableMap<String, String>
) {
    private val versions: MutableMap<String, String> = mutableMapOf()

    internal enum class State {
        NONE,
        VERSIONS,
        LIBRARIES,
        PLUGINS,
        BUNDLES
    }

    private var state: State = State.NONE

    fun load() {
        file.inputStream().use {
            it.reader().forEachLine { line ->
                val trimmed = line.trim()
                if (line.isNotBlank() and !line.startsWith('#')) {
                    if (trimmed in SECTIONS) {
                        state = when (trimmed) {
                            VERSIONS -> State.VERSIONS
                            LIBRARIES -> State.LIBRARIES
                            PLUGINS -> State.PLUGINS
                            BUNDLES -> State.BUNDLES
                            else -> State.NONE
                        }
                    } else if (state == State.VERSIONS) {
                        loadVersionsLine(trimmed)
                    } else if (state == State.LIBRARIES) {
                        loadLibrariesLine(trimmed)
                    }
                }
            }
        }
    }

    private fun loadVersionsLine(line: String) {
        val result = VERSION_PATTERN.find(line)
        if (result != null) {
            val name = result.groups["NAME"]!!.value
            val version = result.groups["VERSION"]!!.value
            versions[name] = version
        }
    }

    private fun loadLibrariesLine(line: String) {
        val result = MODULE_PATTERN1.matchEntire(line)
            ?: MODULE_PATTERN2.matchEntire(line)
            ?: MODULE_PATTERN3.matchEntire(line)
        if (result != null) {
            val group = result.groups["MODULEGROUP"]!!.value
            val name = result.groups["MODULENAME"]!!.value
            val preparedVersion = result.groups["VERSION"]!!.value
            val version = versions.getOrDefault(preparedVersion, preparedVersion)
            modules["$group:$name"] = version
        }
    }

    companion object {
        private const val VERSIONS = "[versions]"
        private const val PLUGINS = "[plugins]"
        private const val LIBRARIES = "[libraries]"
        private const val BUNDLES = "[bundles]"
        private val SECTIONS = arrayOf(VERSIONS, PLUGINS, LIBRARIES, BUNDLES)

        private const val NAME_REGEX = "[a-zA-Z0-9\\-]+"
        private const val BLANK_REGEX = "\\s*"
        private const val VERSION_VALUE_REGEX = "[0-9a-zA-Z\\\\.\\\\-]+"
        private const val MODULE_GROUP_REGEX = "[0-9a-zA-Z\\.\\-]+"
        private const val MODULE_NAME_REGEX = "[0-9a-zA-Z\\-_]+"
        private val VERSION_REGEX = arrayOf(
            "(?<NAME>$NAME_REGEX)",
            "=",
            "[\"'](?<VERSION>$VERSION_VALUE_REGEX)[\"']"
        ).joinToString(BLANK_REGEX)

        /*
         regex for following format
         $name = { module = "$modulegroup:$modulename", version.ref = "$version" }
         */
        private val MODULE_REGEX1 = arrayOf(
            "(?<NAME>$NAME_REGEX)",
            "=",
            "\\{",
            "module", "=",
            "\"(?<MODULEGROUP>$MODULE_GROUP_REGEX):(?<MODULENAME>$MODULE_NAME_REGEX)\"",
            ",",
            "version.ref", "=",
            "\"(?<VERSION>$NAME_REGEX)\"",
            "\\}"
        ).joinToString(BLANK_REGEX)

        /*
         regex for following format
         $name = { group = "$modulegroup", name = "$modulename", version.ref = "$version" }
         */
        private val MODULE_REGEX2 = arrayOf(
            "(?<NAME>$NAME_REGEX)",
            "=",
            "\\{",
            "group", "=", "\"(?<MODULEGROUP>$MODULE_GROUP_REGEX)\"",
            ",",
            "name", "=", "\"(?<MODULENAME>$MODULE_NAME_REGEX)\"",
            ",",
            "version.ref", "=",
            "\"(?<VERSION>$NAME_REGEX)\"",
            "\\}"
        ).joinToString(BLANK_REGEX)

        /*
         regex for following format
         $name = "$modulegroup:$modulename:"$version"
        */
        private val MODULE_REGEX3 = arrayOf(
            "(?<NAME>$NAME_REGEX)",
            "=",
            "\"(?<MODULEGROUP>$MODULE_GROUP_REGEX):(?<MODULENAME>$MODULE_NAME_REGEX):(?<VERSION>$VERSION_VALUE_REGEX)\""
        ).joinToString(BLANK_REGEX)

        private val VERSION_PATTERN = Regex(VERSION_REGEX)
        private val MODULE_PATTERN1 = Regex(MODULE_REGEX1)
        private val MODULE_PATTERN2 = Regex(MODULE_REGEX2)
        private val MODULE_PATTERN3 = Regex(MODULE_REGEX3)
    }
}