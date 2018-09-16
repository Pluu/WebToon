buildscript {

    val KEY_KOTLIN = "kotlin_version"
    extra[KEY_KOTLIN] = "1.2.70"

    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.3.0-alpha10")
        classpath("io.realm:realm-gradle-plugin:4.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${extra[KEY_KOTLIN]}")
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
