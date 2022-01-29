package com.pluu.webtoon.episode.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.pluu.webtoon.episode.R
import com.pluu.webtoon.episode.compose.ImageInCircle
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.utils.applyAgent

@OptIn(ExperimentalCoilApi::class)
@Composable
internal fun EpisodeItemUi(
    modifier: Modifier = Modifier,
    item: EpisodeInfo,
    isRead: Boolean,
    onClicked: (EpisodeInfo) -> Unit
) {
    val painter = rememberImagePainter(
        data = item.image,
        builder = {
            applyAgent()
            crossfade(true)
        }
    )

    Surface(
        modifier = modifier
            .padding(all = 2.dp)
            .clickable { onClicked(item) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )

            when (painter.state) {
                is ImagePainter.State.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = colorResource(com.pluu.webtoon.ui_common.R.color.progress_accent_color)
                    )
                }
                is ImagePainter.State.Error -> {
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
}

@Composable
private fun EpisodeItemUiOverlayUi(
    modifier: Modifier = Modifier,
    item: EpisodeInfo,
    isRead: Boolean
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (title, read, space, lock) = createRefs()

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
                    bottom.linkTo(parent.bottom)
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
                painter = painterResource(R.drawable.ic_lock_white_24),
                circleColor = Color(0xCC222222),
                modifier = Modifier
                    .padding(5.dp)
                    .constrainAs(lock) {
                        top.linkTo(space.bottom)
                        end.linkTo(parent.end)
                    }
            )
        }

    }
}

@Preview
@Composable
private fun PreviewEpisodeItemUi() {
    val item = EpisodeInfo(
        id = "0",
        toonId = "0",
        title = "Title",
        image = ""
    )
    EpisodeItemUi(
        item = item,
        isRead = true,
        onClicked = {}
    )
}