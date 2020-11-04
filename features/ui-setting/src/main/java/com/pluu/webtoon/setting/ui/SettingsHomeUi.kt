package com.pluu.webtoon.setting.ui

import android.content.Context
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import com.pluu.compose.ambient.PreferenceProviderAmbient
import com.pluu.compose.preference.ListPreference
import com.pluu.compose.preference.ListPreferenceItem
import com.pluu.compose.preference.Preference
import com.pluu.compose.runtime.rememberPreferenceState
import com.pluu.webtoon.data.pref.PrefConfig
import com.pluu.webtoon.setting.R

@Composable
fun SettingContentUi(
    modifier: Modifier = Modifier,
    context: Context = ContextAmbient.current,
    onBackPressed: () -> Unit,
    onOpenSourceClicked: () -> Unit
) {
    Column(modifier = modifier) {
        TopAppBar(
            title = {
                Text(text = "설정")
            },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(Icons.Filled.ArrowBack)
                }
            }
        )
        ScrollableColumn(modifier = Modifier.fillMaxSize()) {
            DefaultWebtoonUi(getPreItems(context))
            OpenSourceUi(onClick = onOpenSourceClicked)
        }
    }
}

@Composable
private fun DefaultWebtoonUi(
    items: List<ListPreferenceItem<String>>
) {
    val preferenceProvider = PreferenceProviderAmbient.current

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
                defaultValue = items[0].entryValue
            )
        )
    ) { receiver, item ->
        receiver.summary = summaryProvider(item)
    }

    ListPreference(
        items = items,
        preferenceState = defaultWeebtoon,
        title = "기본 웹툰",
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun OpenSourceUi(
    onClick: () -> Unit
) {
    Preference(
        title = "오픈소스 라이센스",
        modifier = Modifier.fillMaxSize()
            .clickable(onClick = onClick)
    )
}

private fun getPreItems(
    context: Context
): List<ListPreferenceItem<String>> = listOf(
    ListPreferenceItem(
        context.getString(R.string.title_naver),
        context.getString(R.string.title_naver_key)
    ),
    ListPreferenceItem(
        context.getString(R.string.title_daum),
        context.getString(R.string.title_daum_key)
    ),
    ListPreferenceItem(
        context.getString(R.string.title_olleh),
        context.getString(R.string.title_olleh_key)
    ),
    ListPreferenceItem(
        context.getString(R.string.title_kakao_page),
        context.getString(R.string.title_kakao_page_key)
    ),
    ListPreferenceItem(
        context.getString(R.string.title_nate),
        context.getString(R.string.title_nate_key)
    )
)