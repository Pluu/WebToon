package com.pluu.webtoon.weekly.ui.day

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
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
import com.pluu.compose.ui.tooling.preview.DayNightWrapPreview
import com.pluu.webtoon.model.Status
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.ui.compose.theme.AppTheme
import com.pluu.webtoon.ui.compose.theme.themeRed
import com.pluu.webtoon.utils.glideUrl
import com.pluu.webtoon.weekly.R
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
internal fun WeeklyItemUi(
    modifier: Modifier = Modifier,
    item: ToonInfo,
    isFavorite: Boolean,
    onClicked: (ToonInfo) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp)
            .height(100.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClicked(item) }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            GlideImage(
                imageModel = {
                    item.image.glideUrl()
                },
                modifier = Modifier.fillMaxSize(),
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                previewPlaceholder = painterResource(id = com.pluu.compose.R.drawable.ic_baseline_android_24),
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .matchParentSize()
                            .wrapContentSize(),
                        color = themeRed
                    )
                },
                failure = {
                    Image(
                        modifier = Modifier.align(Alignment.Center),
                        painter = painterResource(com.pluu.webtoon.ui_common.R.drawable.ic_sentiment_very_dissatisfied_48),
                        contentDescription = null
                    )
                }
            )
            WeeklyItemOverlayUi(
                item = item,
                isFavorite = isFavorite
            )
        }
    }
}

@Composable
internal fun WeeklyItemOverlayUi(
    modifier: Modifier = Modifier,
    item: ToonInfo,
    isFavorite: Boolean
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (title, status,
            regDate, favorite
        ) = createRefs()

        val isUpdate = item.status == Status.UPDATE
        val isLocked = item.isLocked
        val isBreak = item.status == Status.BREAK
        val isStatusShow = isUpdate || isLocked || isBreak

        Text(
            text = item.title,
            maxLines = 1,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .background(
                    color = Color(0x66000000),
                    shape = if (isStatusShow) {
                        RoundedCornerShape(bottomEnd = 4.dp)
                    } else {
                        RectangleShape
                    }
                )
                .padding(horizontal = 6.dp, vertical = 2.dp)
                .wrapContentWidth(align = Alignment.Start)
                .constrainAs(title) {
                    width = Dimension.fillToConstraints
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(
                        anchor = if (isStatusShow) {
                            status.start
                        } else {
                            parent.end
                        },
                        margin = if (isStatusShow) {
                            8.dp
                        } else {
                            0.dp
                        }
                    )
                }
        )

        if (isStatusShow) {
            WeeklyStatusUi(
                modifier = Modifier.constrainAs(status) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
                isUpdate = isUpdate,
                isLocked = isLocked,
                isRest = isBreak
            )
        }

        if (item.updateDate.isNotBlank()) {
            WeeklyStatusBadge(
                text = item.updateDate,
                backgroundColor = Color(0x66000000),
                modifier = Modifier
                    .constrainAs(regDate) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }

        if (isFavorite) {
            WeeklyItemFavoriteUi(
                modifier = Modifier.constrainAs(favorite) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
            )
        }
    }
}

@Composable
private fun WeeklyItemFavoriteUi(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.ic_favorite_black_36),
        colorFilter = ColorFilter.tint(themeRed),
        modifier = modifier,
        contentDescription = null
    )
}

@Composable
private fun WeeklyStatusUi(
    modifier: Modifier = Modifier,
    isUpdate: Boolean,
    isLocked: Boolean,
    isRest: Boolean
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.End) {
        if (isUpdate) {
            WeeklyStatusBadge(
                text = "UP",
                backgroundColor = Color.Red
            )
        }
        if (isLocked) {
            Image(
                modifier = Modifier.padding(4.dp),
                painter = painterResource(com.pluu.webtoon.ui_common.R.drawable.ic_lock_white_24),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                contentDescription = null,
            )
        }
        if (isRest) {
            WeeklyStatusBadge(
                text = "휴재",
                backgroundColor = Color(0x66000000),
                modifier = Modifier
                    .padding(
                        top = if (isUpdate or isLocked) {
                            3.dp
                        } else {
                            0.dp
                        }
                    )
            )
        }
    }
}

internal class FakeWeeklyItemProvider : PreviewParameterProvider<ToonInfoWithFavorite> {
    override val values = sequenceOf(
        ToonInfoWithFavorite(
            ToonInfo(
                id = "",
                title = "타이틀",
                image = "",
                updateDate = "1234.56.78",
                status = Status.UPDATE,
            ), false
        ),
        ToonInfoWithFavorite(
            ToonInfo(
                id = "",
                title = "타이틀 타이틀 타이틀 타이틀 타이틀 타이틀 타이틀 타이틀",
                image = "",
                updateDate = "1234.56.78",
                status = Status.BREAK,
                isLocked = true
            ), true
        )
    )
    override val count: Int = values.count()
}

@DayNightWrapPreview
@Composable
private fun PreviewWeeklyItemUi(
    @PreviewParameter(FakeWeeklyItemProvider::class) item: ToonInfoWithFavorite,
) {
    AppTheme {
        WeeklyItemUi(
            item = item.info,
            isFavorite = item.isFavorite,
            onClicked = { }
        )
    }
}

@Preview("Weekly Status Component")
@Composable
private fun PreviewWeeklyStatusUi() {
    AppTheme {
        WeeklyStatusUi(isUpdate = true, isLocked = true, isRest = true)
    }
}