package com.pluu.webtoon.detail.ui

import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * 상세화면 Activity
 *
 * - 상단 UI : Value Animation
 * - 하단 UI : TransitionDefinition + TransitionState
 *
 * Created by pluu on 2017-05-09.
 */
@AndroidEntryPoint
class DetailActivity : FragmentActivity() {

//    private val viewModel by viewModels<DetailViewModel>()
//
//    private val palletColor by lazyNone {
//        intent.getRequiredSerializableExtra<PalletColor>(Const.EXTRA_PALLET)
//    }
//
//    private val featureColor by lazyNone {
//        FeatureColor(
//            themeColor = palletColor.darkMutedColor,
//            webToonColor = palletColor.darkVibrantColor
//        )
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//
//        activityComposeView {
//            val systemUiController = rememberSystemUiController()
//            val isDarkTheme = true
//            SideEffect {
//                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = !isDarkTheme)
//            }
//
//            WebToonTheme(useDarkColors = isDarkTheme) {
//                ProvideWindowInsets {
//                    DetailUi(
//                        viewModel = viewModel,
//                        featureColor = featureColor,
//                        errorAction = ::showError,
//                        shareAction = ::showShare
//                    ) { uiEvent ->
//                        when (uiEvent) {
//                            DetailUiEvent.OnBackPressed -> {
//                                finish()
//                            }
//                            DetailUiEvent.OnNextPressed -> {
//                                viewModel.moveNext()
//                            }
//                            DetailUiEvent.OnPrevPressed -> {
//                                viewModel.movePrev()
//                            }
//                            DetailUiEvent.OnSharedPressed -> {
//                                viewModel.requestShare()
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    override fun finish() {
//        setResult(Activity.RESULT_OK)
//        super.finish()
//    }
//
//    // /////////////////////////////////////////////////////////////////////////
//    // 추가 처리
//    // /////////////////////////////////////////////////////////////////////////
//
//    private fun showError(event: DetailEvent.ERROR) {
//        AlertDialog.Builder(this@DetailActivity)
//            .setMessage(event.errorType.getMessage(this))
//            .setCancelable(false)
//            .setPositiveButton(android.R.string.ok) { _, _ ->
//                supportFragmentManager.findFragmentByTag(Const.DETAIL_FRAG_TAG) ?: finish()
//            }
//            .show()
//    }
//
//    private fun showShare(item: ShareItem) {
//        startActivity(
//            Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
//                type = "text/plain"
//                putExtras(
//                    bundleOf(
//                        Intent.EXTRA_SUBJECT to item.title,
//                        Intent.EXTRA_TEXT to item.url
//                    )
//                )
//            }, "Share")
//        )
//    }
}
