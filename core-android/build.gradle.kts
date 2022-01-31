plugins {
    id("android-library-convention")
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)

    implementation(Dep.AndroidX.Activity.ktx)
    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.Fragment.fragment)
    compileOnly(Dep.AndroidX.viewBinding)

    implementation(Dep.AndroidX.UI.recyclerview)

    testImplementation(Dep.AndroidX.Arch.testing)
    testImplementation(Dep.AndroidX.Lifecycle.runtimeTesting)
    testImplementation(Dep.Test.junit)
}