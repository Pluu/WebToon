package com.pluu.webtoon.weekly.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.core.graphics.toColorInt
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.pluu.compose.foundation.backgroundCorner
import com.pluu.webtoon.model.Status
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.utils.applyAgent
import com.pluu.webtoon.weekly.R

@OptIn(ExperimentalMaterialApi::class, ExperimentalCoilApi::class)
@Composable
internal fun WeeklyItemUi(
    modifier: Modifier = Modifier,
    item: ToonInfo,
    isFavorite: Boolean,
    onClicked: (ToonInfo) -> Unit
) {
    val painter = rememberImagePainter(
        data = item.image,
        builder = {
            applyAgent()
            crossfade(true)
        }
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp)
            .height(100.dp),
        onClick = { onClicked(item) }
    ) {
        val backgroundModifier = when (painter.state) {
            is ImagePainter.State.Success -> {
                if (item.backgroundColor.isNotEmpty()) {
                    Modifier.background(color = Color(item.backgroundColor.toColorInt()))
                } else {
                    Modifier
                }
            }
            else -> Modifier
        }

        Box(modifier = backgroundModifier) {
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
                        color = MaterialTheme.colors.secondary
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
        val isAdultLimit = item.isAdult
        val isBreak = item.status == Status.BREAK
        val isStatusShow = isUpdate || isAdultLimit || isBreak

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
                isAdultLimit = isAdultLimit,
                isRest = isBreak
            )
        }

        if (item.updateDate.isNotBlank()) {
            Text(
                text = item.updateDate,
                color = Color.White,
                modifier = Modifier
                    .backgroundCorner(color = Color(0x66000000), size = 4.dp)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
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
        colorFilter = ColorFilter.tint(Color(0xFFF44336)),
        modifier = modifier,
        contentDescription = null
    )
}

@Composable
private fun WeeklyStatusUi(
    modifier: Modifier = Modifier,
    isUpdate: Boolean,
    isAdultLimit: Boolean,
    isRest: Boolean
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.End) {
        if (isUpdate) {
            Text(
                text = "UP",
                color = Color.White,
                modifier = Modifier
                    .backgroundCorner(color = Color.Red, size = 4.dp)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
        if (isAdultLimit) {
            Text(
                text = "19", color = Color.White,
                modifier = Modifier
                    .padding(
                        top = if (isUpdate) {
                            3.dp
                        } else {
                            0.dp
                        }
                    )
                    .backgroundCorner(color = Color.Red, size = 4.dp)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
        if (isRest) {
            Text(
                text = "휴재",
                color = Color.White,
                modifier = Modifier
                    .padding(
                        top = if (isUpdate or isAdultLimit) {
                            3.dp
                        } else {
                            0.dp
                        }
                    )
                    .backgroundCorner(color = Color(0x66000000), size = 4.dp)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

class FakeWeeklyItemProvider : PreviewParameterProvider<ToonInfoWithFavorite> {
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
                isAdult = true
            ), true
        )
    )
    override val count: Int = values.count()
}

@Preview(
    group = "Weekly Component",
    widthDp = 240,
    showBackground = true, backgroundColor = 0xFFFFFFFF
)
@Composable
private fun PreviewWeeklyItemUi(
    @PreviewParameter(FakeWeeklyItemProvider::class) item: ToonInfoWithFavorite,
) {
    WeeklyItemUi(
        item = item.info,
        isFavorite = item.isFavorite,
        onClicked = { }
    )
}

@Preview(
    "Weekly Status Component",
    showBackground = true, backgroundColor = 0xFFFFFFFF
)
@Composable
private fun PreviewWeeklyStatusUi() {
    WeeklyStatusUi(isUpdate = true, isAdultLimit = true, isRest = true)
}
