package com.pluu.webtoon.ui.compose.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.FrameManager
import androidx.compose.runtime.onCommit
import androidx.compose.ui.Modifier
import androidx.compose.ui.WithConstraints
import androidx.compose.ui.graphics.ImageAsset
import androidx.compose.ui.graphics.asImageAsset
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ContextAmbient
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GlideImage(
    url: String,
    options: RequestOptions.() -> RequestOptions = { this },
    modifier: Modifier = Modifier.fillMaxSize(),
    contentScale: ContentScale = ContentScale.Crop,
    onReady: (ImageAsset) -> Unit = {},
) {
    WithConstraints {
        val image = rememberImageAsset { null }
        val drawable = rememberDrawable { null }
        val context = ContextAmbient.current

        onCommit(url) {
            val glide = Glide.with(context)
            var target: CustomTarget<Bitmap>? = null
            val job = CoroutineScope(Dispatchers.Main).launch {
                target = object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {
                        image.value = null
                        drawable.value = placeholder
                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        FrameManager.ensureStarted()
                        val imageAsset = resource.asImageAsset()
                        image.value = imageAsset
                        onReady.invoke(imageAsset)
                    }
                }

                glide
                    .asBitmap()
                    .load(url)
                    .apply(options(RequestOptions()))
                    .into(target!!)
            }

            onDispose {
                image.value = null
                drawable.value = null
                glide.clear(target)
                job.cancel()
            }
        }

        val theImage = image.value
        val theDrawable = drawable.value
        if (theImage != null) {
            Image(
                asset = theImage,
                modifier = modifier,
                contentScale = contentScale
            )
        } else if (theDrawable != null) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawIntoCanvas { theDrawable.draw(it.nativeCanvas) }
            }
        }
    }
}
