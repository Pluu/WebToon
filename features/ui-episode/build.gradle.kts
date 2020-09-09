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
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core-android"))
    implementation(project(":domain"))
    implementation(project(":site"))
    implementation(project(":ui-common"))

    implementation(Dep.Kotlin.stdlibJvm)

    implementation(Dep.AndroidX.activity.ktx)
    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.fragment.ktx)

    // Android UI
    implementation(Dep.AndroidX.UI.cardview)
    implementation(Dep.AndroidX.UI.constraintLayout)
    implementation(Dep.AndroidX.UI.recyclerview)
    implementation(Dep.AndroidX.UI.swiperefreshlayout)

    // Hilt
    implementation(Dep.Hilt.android)
    kapt(Dep.Hilt.hilt_compiler)
    implementation(Dep.Hilt.viewModel)
    kapt(Dep.Hilt.android_hilt_compiler)
}
