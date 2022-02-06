import com.android.build.gradle.BaseExtension

configure<BaseExtension> {
    buildFeatures.compose = true

    composeOptions {
        kotlinCompilerExtensionVersion = Dep.AndroidX.Compose.version
    }
}

dependencies {
    add("implementation", Dep.AndroidX.Compose.material)
    add("implementation", Dep.AndroidX.Compose.toolingPreview)
    add("debugImplementation", Dep.AndroidX.Compose.tooling)
}