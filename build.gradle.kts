plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt) apply false
}

subprojects {
    afterEvaluate {
        configurations.all {
            resolutionStrategy.eachDependency {
                // minimum version of GMS Libraries which is migrated with AndroidX
                val minGooglePlayBaseVersion = "17.3.0"
                if (requested.group == "com.google.android.gms" &&
                    (requested.name == "play-services-basement" ||
                            requested.name == "play-services-base") &&
                    requested.version!! < minGooglePlayBaseVersion
                ) {
                    useVersion(minGooglePlayBaseVersion)
                }

                if (target.group.startsWith("com.android.support") ||
                    target.group.startsWith("android.arch")
                ) {
                    throw GradleException("$target library was included")
                }
            }
        }
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
