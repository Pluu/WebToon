import com.android.build.gradle.BaseExtension

configure<BaseExtension> {
    buildFeatures.compose = true

    composeOptions {
        kotlinCompilerExtensionVersion = Dep.AndroidX.Compose.version
    }
}
