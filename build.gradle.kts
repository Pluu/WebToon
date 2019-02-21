buildscript {

    val KEY_KOTLIN = "kotlin_version"
    extra[KEY_KOTLIN] = "1.3.21"

    repositories {
        google()
        jcenter()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.0-alpha04")
        classpath(kotlin("gradle-plugin", version = "${extra[KEY_KOTLIN]}"))
        classpath("io.realm:realm-gradle-plugin:4.1.1")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:7.1.0")
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
