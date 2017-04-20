package com.pluu.webtoon.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.pluu.support.impl.NAV_ITEM;

/**
 * SharedPreferences
 * Created by nohhs on 2015-03-25.
 */
public class PrefConfig {

	public static final String KEY_DEFAULT_WEBTOON = "default_webtoon";

	/**
	 * SharedPreferences 취득
	 * @param context Context
	 * @param name Name
	 * @return SharedPreferences
	 */
	public static SharedPreferences getPreferences(Context context, String name) {
		return context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	public static SharedPreferences.Editor getEditor(Context context, String name) {
		return context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
	}

	/**
	 * SharedPreferences 취득
	 * @param context Context
	 * @return SharedPreferences
	 */
	public static SharedPreferences getPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static SharedPreferences.Editor getEditor(Context context) {
		return getPreferences(context).edit();
	}

	public static NAV_ITEM getDefaultWebToon(Context context) {
		String name = getPreferences(context).getString(KEY_DEFAULT_WEBTOON,
				NAV_ITEM.getDefault().name());
		return NAV_ITEM.valueOf(name.toUpperCase());
	}
}
