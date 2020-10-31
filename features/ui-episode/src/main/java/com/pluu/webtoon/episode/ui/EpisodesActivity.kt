package com.pluu.webtoon.episode.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.commit
import com.pluu.core.utils.lazyNone
import com.pluu.utils.getRequiredParcelableExtra
import com.pluu.utils.getRequiredSerializableExtra
import com.pluu.utils.result.setFragmentResult
import com.pluu.utils.viewbinding.viewBinding
import com.pluu.webtoon.Const
import com.pluu.webtoon.episode.R
import com.pluu.webtoon.episode.databinding.ActivityEpisodeBinding
import com.pluu.webtoon.model.ToonInfoWithFavorite
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

            EpisodeTopUi(
                title = webToonItem.info.title,
                onBackPressed = {
                    finish()
                },
                isFavorite = isFavorite,
                onFavoriteClicked = { currentFavorite ->
                    viewModel.favorite(currentFavorite.not())
                },
            )
        }
    }

    private fun initEpisodeInfoUi() {
        val info = webToonItem.info
        binding.bottom.setComposeContent {
            EpisodeInfoUi(
                name = info.writer,
                rate = info.rate,
                onFirstClicked = {
                    setFragmentResult(EpisodeConst.ShowFirst)
                }
            )
        }
    }

//    private fun initView() {
//        val variantListener = ValueAnimator.AnimatorUpdateListener { animation ->
//            val value = animation.animatedValue as Int
//            toolbar.setBackgroundColor(value)
//            binding.btnFirst.backgroundTintList = ColorStateList.valueOf(value)
//            childTitle?.setBackgroundColor(value)
//
//            this@EpisodesActivity.setStatusBarColor(value)
//        }
//        animatorColor(
//            startColor = getThemeColor(R.attr.colorPrimary),
//            endColor = palletColor.darkVibrantColor,
//            listener = variantListener
//        ).apply {
//            duration = 1000L
//        }.start()
//
//        val writerListener = ValueAnimator.AnimatorUpdateListener { animation ->
//            val value = animation.animatedValue as Int
//            binding.tvName.setTextColor(value)
//            binding.tvRate.setTextColor(value)
//        }
//        animatorColor(
//            startColor = getThemeColor(android.R.attr.textColorPrimary),
//            endColor = if (ThemeHelper.isLightTheme(this)) palletColor.darkMutedColor else palletColor.lightMutedColor,
//            listener = writerListener
//        ).apply {
//            duration = 1000L
//        }.start()
//    }
//
//    private fun initFragment() {
//        val fragment = EpisodeFragment.newInstance(webToonItem, palletColor)
//
//        supportFragmentManager.commit {
//            replace(R.id.container, fragment, Const.MAIN_FRAG_TAG)
//        }
//    }
}
