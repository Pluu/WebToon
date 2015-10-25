package com.pluu.webtoon.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences
 * Created by nohhs on 2015-03-25.
 */
public class PrefConfig {
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

}
