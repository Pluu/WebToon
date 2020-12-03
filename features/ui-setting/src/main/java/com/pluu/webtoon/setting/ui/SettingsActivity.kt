package com.pluu.webtoon.setting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.runtime.Providers
import androidx.compose.ui.platform.AmbientContext
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.pluu.compose.ambient.PreferenceProvider
import com.pluu.webtoon.navigator.SettingNavigator
import com.pluu.webtoon.ui.compose.FragmentComposeView
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        supportFragmentManager.commit {
            replace(
                android.R.id.content,
                SettingFragment()
            )
        }
    }
}

@AndroidEntryPoint
class SettingFragment : Fragment() {
    @Inject
    lateinit var preferenceProvider: PreferenceProvider

    @Inject
    lateinit var navigator: SettingNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentComposeView {
        ProvideWindowInsets {
            val context = AmbientContext.current
            Providers(AmbientPreferenceProvider provides preferenceProvider) {
                SettingContentUi(
                    onBackPressed = {
                        activity?.finish()
                    },
                    onOpenSourceClicked = {
                        navigator.openLicense(context)
                    }
                )
            }
        }
    }
}
