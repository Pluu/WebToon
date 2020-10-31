package com.pluu.webtoon.setting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Providers
import androidx.compose.ui.platform.ContextAmbient
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.pluu.compose.ambient.PreferenceProvider
import com.pluu.compose.ambient.PreferenceProviderAmbient
import com.pluu.webtoon.navigator.SettingNavigator
import com.pluu.webtoon.ui.compose.FragmentComposeView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val context = ContextAmbient.current
        Providers(
            PreferenceProviderAmbient provides preferenceProvider
        ) {
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
