package com.pluu.webtoon.utils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

import com.pluu.webtoon.R;

/**
 * Display Utils
 * Created by PLUUSYSTEM-NEW on 2015-11-22.
 */
public class DisplayUtils {

	public static ValueAnimator animatorToolbarColor(final AppCompatActivity activity, int color) {
		return animatorToolbarColor(activity, color,
                animation -> {
                    Integer value = (Integer) animation.getAnimatedValue();
                    ActionBar actionBar = activity.getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setBackgroundDrawable(
                            new ColorDrawable(value));
                    }
                });
	}

	public static ValueAnimator animatorToolbarColor(final AppCompatActivity activity, int color,
													 ValueAnimator.AnimatorUpdateListener listener) {
		TypedValue value = new TypedValue();
		activity.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);

		ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), value.data, color);
		if (listener != null) {
			animator.addUpdateListener(listener);
		}
		animator.setInterpolator(new DecelerateInterpolator());
		return animator;
	}

	public static void setStatusBarColor(Activity activity, int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = activity.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(color);
		}
	}

	public static ValueAnimator animatorStatusBarColor(final Activity activity, int color) {
		TypedValue value = new TypedValue();
		activity.getTheme().resolveAttribute(R.attr.colorPrimaryDark, value, true);

		ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), value.data, color);
		animator.addUpdateListener(animation -> setStatusBarColor(activity, (Integer) animation.getAnimatedValue()));
		animator.setInterpolator(new DecelerateInterpolator());
		return animator;
	}

}
