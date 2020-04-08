package plugins

import BuildType
import BuildTypeRelease
import Dep
import ProjectConfigurations

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdkVersion(ProjectConfigurations.compileSdk)
    buildToolsVersion(ProjectConfigurations.buildTools)

    defaultConfig {
        minSdkVersion(ProjectConfigurations.minSdk)
        targetSdkVersion(ProjectConfigurations.targetSdk)
    }

    buildTypes {
        getByName(BuildType.RELEASE) {
            isMinifyEnabled = BuildTypeRelease.isMinifyEnabled
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
            proguardFiles(file("proguard-rules.pro"))
        }
    }

    compileOptions {
        sourceCompatibility = ProjectConfigurations.javaVer
        targetCompatibility = ProjectConfigurations.javaVer
    }

    kotlinOptions {
        jvmTarget = ProjectConfigurations.javaVer.toString()
    }

    lintOptions {
        isAbortOnError = false
    }
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)
    testImplementation(Dep.Test.junit)
}