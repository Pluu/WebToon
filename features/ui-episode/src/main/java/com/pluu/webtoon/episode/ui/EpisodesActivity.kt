package com.pluu.webtoon.episode.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.commit
import com.pluu.compose.transition.colorStartToEndTransition
import com.pluu.compose.transition.colorStateKey
import com.pluu.compose.transition.colorTransitionDefinition
import com.pluu.core.utils.lazyNone
import com.pluu.utils.ThemeHelper
import com.pluu.utils.getRequiredParcelableExtra
import com.pluu.utils.getRequiredSerializableExtra
import com.pluu.utils.getThemeColor
import com.pluu.utils.result.setFragmentResult
import com.pluu.utils.viewbinding.viewBinding
import com.pluu.webtoon.Const
import com.pluu.webtoon.episode.R
import com.pluu.webtoon.episode.databinding.ActivityEpisodeBinding
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.ui.compose.graphics.toColor
import com.pluu.webtoon.ui.compose.setComposeContent
import com.pluu.webtoon.ui.model.PalletColor
import dagger.hilt.android.AndroidEntryPoint

/**
 * 에피소드 리스트 Activity
 * Created by pluu on 2017-05-09.
 */
@AndroidEntryPoint
class EpisodesActivity : AppCompatActivity(R.layout.activity_episode) {

    private val binding by viewBinding(ActivityEpisodeBinding::bind)

    private val viewModel by viewModels<EpisodeViewModel>()

    private val webToonItem by lazyNone {
        intent.getRequiredSerializableExtra<ToonInfoWithFavorite>(Const.EXTRA_EPISODE)
    }
    private val palletColor by lazyNone {
        intent.getRequiredParcelableExtra<PalletColor>(Const.EXTRA_PALLET)
    }

    private val colorDefinition by lazy {
        colorTransitionDefinition(
            startColor = getThemeColor(R.attr.colorPrimary).toColor(),
            endColor = palletColor.darkVibrantColor.toColor(),
            durationMillis = 1000
        )
    }

    private val infoTextColorDefinition by lazy {
        colorTransitionDefinition(
            startColor = getThemeColor(android.R.attr.textColorPrimary).toColor(),
            endColor = if (ThemeHelper.isLightTheme(this)) {
                palletColor.darkMutedColor.toColor()
            } else {
                palletColor.lightMutedColor.toColor()
            },
            durationMillis = 1000
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initEpisodeTopUi()

        supportFragmentManager.commit {
            replace(
                R.id.container,
                EpisodeFragment.newInstance(webToonItem, palletColor),
                Const.MAIN_FRAG_TAG
            )
        }

        initEpisodeInfoUi()
    }

    private fun initEpisodeTopUi() {
        binding.toolbar.setComposeContent {
            val isFavorite by viewModel.favorite.observeAsState(false)
            val transition = colorStartToEndTransition(colorDefinition)

            EpisodeTopUi(
                title = webToonItem.info.title,
                backgroundColor = transition[colorStateKey],
                onBackPressed = {
                    finish()
                },
                isFavorite = isFavorite,
                onFavoriteClicked = { currentFavorite ->
                    viewModel.favorite(currentFavorite.not())
                }
            )
        }
    }

    private fun initEpisodeInfoUi() {
        val info = webToonItem.info
        binding.bottom.setComposeContent {
            val bgTransition = colorStartToEndTransition(colorDefinition)
            val infoTextTransition = colorStartToEndTransition(infoTextColorDefinition)

            EpisodeInfoUi(
                name = info.writer,
                rate = info.rate,
                infoTextColor = infoTextTransition[colorStateKey],
                buttonBackgroundColor = bgTransition[colorStateKey],
                onFirstClicked = {
                    setFragmentResult(EpisodeConst.ShowFirst)
                }
            )
        }
    }
}
