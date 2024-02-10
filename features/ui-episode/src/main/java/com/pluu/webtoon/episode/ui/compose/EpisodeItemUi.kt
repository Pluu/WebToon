package com.pluu.webtoon.episode.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.pluu.webtoon.episode.R
import com.pluu.webtoon.episode.compose.ImageInCircle
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.ui.compose.theme.AppTheme
import com.pluu.webtoon.ui.compose.theme.md_theme_light_primary
import com.pluu.webtoon.utils.applyAgent

@Composable
internal fun EpisodeItemUi(
    modifier: Modifier = Modifier,
    item: EpisodeInfo,
    isRead: Boolean,
    onClicked: (EpisodeInfo) -> Unit
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(item.image)
            .applyAgent()
            .crossfade(true)
            .build()
    )

    Box(
        modifier = modifier
            .padding(all = 2.dp)
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClicked(item) }
    ) {
        if (LocalInspectionMode.current) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(md_theme_light_primary)
            )
        } else {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        }

        when (painter.state) {
            is AsyncImagePainter.State.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = colorResource(com.pluu.webtoon.ui_common.R.color.progress_accent_color)
                )
            }

            is AsyncImagePainter.State.Error -> {
                Image(
                    modifier = Modifier.align(Alignment.Center),
                    painter = painterResource(com.pluu.webtoon.ui_common.R.drawable.ic_sentiment_very_dissatisfied_48),
                    contentDescription = null
                )
            }

            else -> {}
        }

        EpisodeItemUiOverlayUi(item = item, isRead = isRead)
    }
}

@Composable
private fun EpisodeItemUiOverlayUi(
    modifier: Modifier = Modifier,
    item: EpisodeInfo,
    isRead: Boolean
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (title, read, lock) = createRefs()

        Text(
            text = item.title,
            maxLines = 2,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .background(Color(0x66000000))
                .sizeIn(minHeight = 20.dp)
                .padding(horizontal = 6.dp, vertical = 2.dp)
                .constrainAs(title) {
                    width = Dimension.fillToConstraints
                    centerHorizontallyTo(parent)
                    linkTo(
                        top = lock.bottom,
                        bottom = parent.bottom,
                        bias = 1f
                    )
                }
        )

        if (isRead) {
            ImageInCircle(
                painter = painterResource(R.drawable.ic_check_white_24),
                circleColor = Color(0xCC222222),
                modifier = Modifier
                    .padding(5.dp)
                    .constrainAs(read) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )
        }

        if (item.isLock) {
            ImageInCircle(
                painter = painterResource(com.pluu.webtoon.ui_common.R.drawable.ic_lock_white_24),
                circleColor = Color(0xCC222222),
                modifier = Modifier
                    .padding(5.dp)
                    .constrainAs(lock) {
                        top.linkTo(read.bottom)
                        end.linkTo(parent.end)
                    }
            )
        }

    }
}

// first : 로그인 여부, second : 읽음 여부
internal class PreviewEpisodeItemProvider : PreviewParameterProvider<Pair<Boolean, Boolean>> {
    override val values = sequenceOf(
        true to true,
        true to false,
        false to true,
        false to false
    )
    override val count: Int = values.count()
}

@Preview
@Composable
private fun PreviewEpisodeItemUi(
    @PreviewParameter(PreviewEpisodeItemProvider::class) values: Pair<Boolean, Boolean>,
) {
    val item = EpisodeInfo(
        id = "0",
        toonId = "0",
        title = "Title",
        image = "",
        isLoginNeed = values.first
    )
    AppTheme {
        EpisodeItemUi(
            modifier = Modifier
                .width(280.dp)
                .heightIn(min = 150.dp),
            item = item,
            isRead = values.second,
            onClicked = {}
        )
    }
}

@Preview
@Composable
private fun PreviewEpisodeItemUiOverlayUi() {
    val item = EpisodeInfo(
        id = "0",
        toonId = "0",
        title = "Title",
        image = "",
        isLoginNeed = true
    )
    AppTheme {
        EpisodeItemUiOverlayUi(
            modifier = Modifier
                .width(200.dp)
                .background(md_theme_light_primary)
                .wrapContentHeight(unbounded = true),
            item = item,
            isRead = true
        )
    }
}