package com.pluu.webtoon.episode.ui.compose

import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class EpisodeTopUiTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun t1() {
        composeTestRule.setContent {
            EpisodeTopUi(
                title = "Test",
                backgroundColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                isFavorite = false,
                onBackPressed = {},
                onFavoriteClicked = {}
            )
        }

        composeTestRule
            .onNodeWithText("Test")
            .assertExists()
    }
}