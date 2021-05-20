package com.pluu.webtoon.episode.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.imageloading.ImageLoadState
import com.pluu.webtoon.episode.R
import com.pluu.webtoon.episode.compose.ImageInCircle
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.utils.toAgentGlideUrl

@Composable
fun EpisodeItemUi(
    modifier: Modifier = Modifier,
    item: EpisodeInfo,
    isRead: Boolean,
    onClicked: (EpisodeInfo) -> Unit
) {
    val painter = rememberGlidePainter(
        request = item.image.toAgentGlideUrl(),
        fadeIn = true,
        previewPlaceholder = R.drawable.ic_check_white_24
    )

    Card(
        modifier = modifier
            .clickable(onClick = { onClicked(item) })
            .padding(all = 2.dp)
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

            when (painter.loadState) {
                is ImageLoadState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colors.secondary
                    )
                }
                is ImageLoadState.Error -> {
                    Image(
                        modifier = Modifier.align(Alignment.Center),
                        painter = painterResource(R.drawable.ic_sentiment_very_dissatisfied_48),
                        contentDescription = null
                    )
                }
            }

            EpisodeItemUiOverlayUi(item = item, isRead = isRead)
        }
    }
}

@Composable
fun EpisodeItemUiOverlayUi(
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
fun PreviewEpisodeItemUi() {
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