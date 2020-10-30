package com.pluu.webtoon.setting.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.pluu.compose.ambient.PreferenceProvider
import com.pluu.compose.ambient.PreferenceProviderAmbient
import com.pluu.compose.preference.ListPreference
import com.pluu.compose.preference.ListPreferenceItem
import com.pluu.compose.preference.Preference
import com.pluu.compose.runtime.rememberPreferenceState
import com.pluu.webtoon.data.pref.PrefConfig
import com.pluu.webtoon.navigator.SettingNavigator
import com.pluu.webtoon.setting.R
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
                APreferenceFragment()
            )
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}

@AndroidEntryPoint
class APreferenceFragment : Fragment() {
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
        Providers(PreferenceProviderAmbient provides preferenceProvider) {
            ScrollableColumn(modifier = Modifier.fillMaxSize()) {
                DefaultWebtoonUi(getPreItems(context))
                OpenSourceUi {
                    navigator.openLicense(context)
                }
            }
        }
    }
}

@Composable
private fun DefaultWebtoonUi(
    items: List<ListPreferenceItem<String>>
) {
    val preferenceProvider = PreferenceProviderAmbient.current
    val context = ContextAmbient.current

    val preferenceKey = PrefConfig.KEY_DEFAULT_WEBTOON
    val summaryProvider: (String) -> String = { value ->
        items.first {
            it.entryValue == value
        }.entry
    }

    val defaultWeebtoon = rememberPreferenceState(
        key = preferenceKey,
        preferences = preferenceProvider.preferences,
        initialValue = items[0].entryValue,
        initialSummary = summaryProvider(
            preferenceProvider.getValue(
                key = preferenceKey,
                defaultValue = items[0].entry
            )
        )
    ) { receiver, item ->
        receiver.summary = summaryProvider(item)
    }

    ListPreference(
        items = items,
        preferenceState = defaultWeebtoon,
        title = context.getString(R.string.label_default_webtoon),
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun OpenSourceUi(
    context: Context = ContextAmbient.current,
    onClick: () -> Unit
) {
    Preference(
        title = context.getString(R.string.label_license),
        modifier = Modifier.fillMaxSize()
            .clickable(onClick = onClick)
    )
}

private fun getPreItems(
    context: Context
): List<ListPreferenceItem<String>> = listOf(
    ListPreferenceItem(
        context.getString(R.string.title_naver), context.getString(R.string.title_naver_key)
    ),
    ListPreferenceItem(
        context.getString(R.string.title_daum), context.getString(R.string.title_daum_key)
    ),
    ListPreferenceItem(
        context.getString(R.string.title_olleh), context.getString(R.string.title_olleh_key)
    ),
    ListPreferenceItem(
        context.getString(R.string.title_kakao_page),
        context.getString(R.string.title_kakao_page_key)
    ),
    ListPreferenceItem(
        context.getString(R.string.title_nate), context.getString(R.string.title_nate_key)
    )
)
