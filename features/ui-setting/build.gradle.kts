plugins {
    androidLibrary()
    kotlinAndroid()
    daggerHilt()
    kotlinKapt()
}

android {
    setDefaultConfig()
    setLibraryProguard(project)

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core-android"))
    implementation(project(":ui-common"))
    implementation(project(":data"))

    implementation(Dep.Kotlin.stdlibJvm)

    implementation(Dep.AndroidX.activity.ktx)
    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.fragment.ktx)

    // Android UI
    implementation(Dep.AndroidX.UI.browser)
    implementation(Dep.AndroidX.UI.material)
    implementation(Dep.AndroidX.UI.preference)
    implementation(Dep.AndroidX.UI.recyclerview)

    // Hilt
    implementation(Dep.Hilt.android)
    kapt(Dep.Hilt.hilt_compiler)
}