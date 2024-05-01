package buildTime.lifecycle

import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters

abstract class ConfigurationPhaseObserver : ValueSource<Boolean, ValueSourceParameters.None> {
    override fun obtain(): Boolean {
        return configurationExecuted.also {
            configurationExecuted = false
        }
    }

    companion object {
        private var configurationExecuted = false
        fun init() {
            configurationExecuted = true
        }
    }
}