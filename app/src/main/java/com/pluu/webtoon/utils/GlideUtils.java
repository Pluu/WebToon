package com.pluu.webtoon.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.target.SquaringDrawable;

/**
 * Glide Image Utils
 * Created by PLUUSYSTEM-NEW on 2016-04-03.
 */
public class GlideUtils {

    /**
     * Get Bitmap
     * @see <a href="http://dalinaum.github.io/android/2015/07/07/retrieving-bitmap-of-glide.html">Base Code Url</a>
     * @param view ImageView
     * @return Bitmap
     */
    public static Bitmap loadGlideBitmap(ImageView view) {
        Bitmap bitmap = null;
        Drawable drawable = view.getDrawable();
        if (drawable instanceof GlideBitmapDrawable) {
            bitmap = ((GlideBitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof TransitionDrawable) {
            TransitionDrawable transitionDrawable = (TransitionDrawable) drawable;
            int length = transitionDrawable.getNumberOfLayers();
            for (int i = 0; i < length; ++i) {
                Drawable child = transitionDrawable.getDrawable(i);
                if (child instanceof GlideBitmapDrawable) {
                    bitmap = ((GlideBitmapDrawable) child).getBitmap();
                    break;
                } else if (child instanceof SquaringDrawable
                        && child.getCurrent() instanceof GlideBitmapDrawable) {
                    bitmap = ((GlideBitmapDrawable) child.getCurrent()).getBitmap();
                    break;
                }
            }
        } else if (drawable instanceof SquaringDrawable) {
            bitmap = ((GlideBitmapDrawable) drawable.getCurrent()).getBitmap();
        }
        return bitmap;
    }

}
