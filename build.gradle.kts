buildscript {

    val KEY_KOTLIN = "kotlin_version"
    extra[KEY_KOTLIN] = "1.3.0"

    repositories {
        google()
        jcenter()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.0-alpha02")
        classpath("io.realm:realm-gradle-plugin:4.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${extra[KEY_KOTLIN]}")
        classpath("gradle.plugin.org.jlleitschuh.gradle:ktlint-gradle:5.1.0")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
