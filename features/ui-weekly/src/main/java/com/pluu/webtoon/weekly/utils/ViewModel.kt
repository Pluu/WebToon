package com.pluu.webtoon.weekly.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pluu.utils.findActivity
import com.pluu.webtoon.weekly.ui.WeeklyDayViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
internal interface ViewModelFactoryProvider {
    fun weeklyViewModelFactory(): WeeklyDayViewModel.Factory
}

@Composable
internal fun createWeeklyDayViewModel(
    key: String? = null,
    context: Context = LocalContext.current,
    position: Int
): WeeklyDayViewModel {
    val activity = checkNotNull(context.findActivity()) {
        "Expected an activity context for creating a HiltViewModelFactory for a " +
                "NavBackStackEntry but instead found: Local Context"
    }
    val factory = EntryPointAccessors.fromActivity(
        activity,
        ViewModelFactoryProvider::class.java
    ).weeklyViewModelFactory()

    return viewModelOf(key) {
        factory.create(position)
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
internal inline fun <reified T : ViewModel> viewModelOf(
    key: String? = null,
    crossinline viewModelInstanceCreator: () -> T
): T = viewModel(
    modelClass = T::class.java,
    key = key,
    factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return viewModelInstanceCreator() as T
        }
    }
)

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> viewModelProviderFactoryOf(
    crossinline assistedFactory: () -> T,
): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return assistedFactory() as T
    }
}