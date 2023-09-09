package com.pluu.webtoon.setting.ui

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager
import com.pluu.compose.ambient.LocalPreference
import com.pluu.compose.ambient.ProvidePreference
import com.pluu.compose.preference.ListPreference
import com.pluu.compose.preference.ListPreferenceItem
import com.pluu.compose.preference.Preference
import com.pluu.compose.preference.rememberPreferenceState
import com.pluu.compose.ui.tooling.preview.DayNightPreview
import com.pluu.webtoon.model.KeyContract
import com.pluu.webtoon.ui.compose.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onOpenSourceClicked: () -> Unit
) {
    val context: Context = LocalContext.current
    val items: List<ListPreferenceItem<String>> = remember { getPreItems(context).toMutableList() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "설정") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            )
        },
        modifier = modifier.fillMaxWidth()
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            DefaultWebtoonUi(items)
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = (0.5).dp)
            OpenSourceUi(onClick = onOpenSourceClicked)
        }
    }
}

@Composable
private fun DefaultWebtoonUi(
    items: List<ListPreferenceItem<String>>
) {
    val preferenceProvider = LocalPreference.current

    val preferenceKey = KeyContract.KEY_DEFAULT_WEBTOON
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
        context.getString(com.pluu.webtoon.ui_common.R.string.title_naver),
        context.getString(com.pluu.webtoon.ui_common.R.string.title_naver_key)
    ),
    ListPreferenceItem(
        context.getString(com.pluu.webtoon.ui_common.R.string.title_daum),
        context.getString(com.pluu.webtoon.ui_common.R.string.title_daum_key)
    ),
    ListPreferenceItem(
        context.getString(com.pluu.webtoon.ui_common.R.string.title_olleh),
        context.getString(com.pluu.webtoon.ui_common.R.string.title_olleh_key)
    ),
    ListPreferenceItem(
        context.getString(com.pluu.webtoon.ui_common.R.string.title_kakao_page),
        context.getString(com.pluu.webtoon.ui_common.R.string.title_kakao_page_key)
    )
)

@DayNightPreview
@Composable
private fun PreviewSettingContentUi() {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LocalContext.current)
    AppTheme {
        ProvidePreference(sharedPreferences) {
            SettingsScreen(
                onBackPressed = {},
                onOpenSourceClicked = {}
            )
        }
    }
}