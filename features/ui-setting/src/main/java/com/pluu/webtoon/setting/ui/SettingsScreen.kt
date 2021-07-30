package com.pluu.webtoon.setting.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.pluu.compose.ambient.LocalPreference
import com.pluu.compose.ambient.ProvidePreference
import com.pluu.compose.preference.ListPreference
import com.pluu.compose.preference.ListPreferenceItem
import com.pluu.compose.preference.Preference
import com.pluu.compose.preference.rememberPreferenceState
import com.pluu.webtoon.data.pref.PrefConfig
import com.pluu.webtoon.setting.R

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onOpenSourceClicked: () -> Unit
) {
    val context: Context = LocalContext.current
    val items: List<ListPreferenceItem<String>> = remember { getPreItems(context).toMutableList() }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(text = "설정")
            },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            modifier = modifier
                .background(MaterialTheme.colors.primarySurface)
                .statusBarsPadding(),
            backgroundColor = MaterialTheme.colors.primarySurface,
            elevation = 0.dp
        )
        Column(modifier = Modifier.fillMaxSize()) {
            DefaultWebtoonUi(items)
            OpenSourceUi(onClick = onOpenSourceClicked)
        }
    }
}

@Composable
private fun DefaultWebtoonUi(
    items: List<ListPreferenceItem<String>>
) {
    val preferenceProvider = LocalPreference.current

    val preferenceKey = PrefConfig.KEY_DEFAULT_WEBTOON
    val summaryProvider: (String) -> String = { value ->
        items.first {
            it.entryValue == value
        }.entry
    }

    val defaultWebtoon = rememberPreferenceState(
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
        preferenceState = defaultWebtoon,
        title = "기본 웹툰",
        painter = null
    )
}

@Composable
private fun OpenSourceUi(
    onClick: () -> Unit
) {
    Preference(
        modifier = Modifier
            .clickable(onClick = onClick),
        title = "오픈소스 라이센스"
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
    )
)

@Preview(
    heightDp = 240,
    showBackground = true, backgroundColor = 0xFFF
)
@Composable
fun PreviewSettingContentUi() {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LocalContext.current)
    ProvideWindowInsets {
        ProvidePreference(sharedPreferences) {
            SettingsScreen(
                onBackPressed = {},
                onOpenSourceClicked = {}
            )
        }
    }
}