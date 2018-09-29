buildscript {

    val KEY_KOTLIN = "kotlin_version"
    extra[KEY_KOTLIN] = "1.2.71"

    repositories {
        google()
        jcenter()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.3.0-alpha12")
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
