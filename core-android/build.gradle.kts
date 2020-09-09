plugins {
    id("com.android.library")
    kotlin("android")
}

listOf(
    "commonConfiguration.gradle",
    "libraryConfiguration.gradle"
).forEach { file ->
    apply(from = "${rootProject.projectDir}/gradle/${file}")
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)

    implementation(Dep.AndroidX.activity.ktx)
    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.fragment.ktx)
    compileOnly(Dep.AndroidX.viewBinding)

    implementation(Dep.AndroidX.UI.recyclerview)
}