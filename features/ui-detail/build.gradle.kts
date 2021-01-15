plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

listOf(
    "commonConfiguration.gradle",
    "libraryConfiguration.gradle"
).forEach { file ->
    apply(from = "${rootProject.projectDir}/gradle/${file}")
}

android {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerVersion = Dep.Kotlin.version
        kotlinCompilerExtensionVersion = Dep.AndroidX.Compose.version
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core-android"))
    implementation(project(":domain"))
    implementation(project(":site"))
    implementation(project(":ui-common"))
    implementation(project(":compose"))

    implementation(Dep.Kotlin.stdlibJvm)

    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.fragment.ktx)
    implementation(Dep.AndroidX.lifecycle.viewModelKtx)

    // Compose
    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.tooling)
    implementation(Dep.AndroidX.Compose.livedata)

    implementation(Dep.Accompnist.glide)
    implementation(Dep.Accompnist.insets)

    // Hilt
    implementation(Dep.Hilt.dagger_android)
    kapt(Dep.Hilt.dagger_compiler)
    implementation(Dep.Hilt.androidx_viewModel)
    kapt(Dep.Hilt.androidx_compiler)

    implementation(Dep.timber)
}