package com.pluu.support.impl;

import android.os.Bundle;
import android.support.annotation.ColorRes;

import com.pluu.webtoon.R;
import com.pluu.webtoon.common.Const;

/**
 * Service Const Class
 * Created by PLUUSYSTEM-NEW on 2015-11-01.
 */
public class ServiceConst {

	public enum NAV_ITEM {
		NAVER(true, R.color.naver_color, R.color.naver_color_dark),
		DAUM(true, R.color.daum_color, R.color.daum_color_dark),
		OLLEH(true, R.color.olleh_color, R.color.olleh_color_dark),
		KAKAOPAGE(true, R.color.kakao_color, R.color.kakao_color_dark),
		NATE(true, R.color.nate_color, R.color.nate_color_dark),
		T_STORE(true, R.color.t_store_color, R.color.t_store_color_dark),
		SEPARATOR(false, 0, 0),			// Separator
		INVALID(false, 0, 0);			// Only Temp

		public final boolean isSelect;
		public final int color;
		public final int bgColor;

		NAV_ITEM(boolean isSelect, @ColorRes int color, @ColorRes int bgColor) {
			this.isSelect = isSelect;
			this.color = color;
			this.bgColor = bgColor;
		}

		public static NAV_ITEM getDefault() {
			return NAVER;
		}
	}

	// titles for navdrawer items (indices must correspond to the above)
	public static final int[] NAVDRAWER_TITLE_RES_ID = new int[]{
		R.string.title_naver,
		R.string.title_daum,
		R.string.title_olleh,
		R.string.title_kakao_page,
		R.string.title_nate,
		R.string.title_t_store,
	};

	// icons for navdrawer items (indices must correspond to above array)
	public static final int[] NAVDRAWER_ICON_RES_ID = new int[] {
		0,		// NAVER
		0,		// DAUM
		0,		// OLLEH
		0, 		// Kakao Page
		0, 		// Nate
		0, 		// T Store
	};

	public static NAV_ITEM getApiType(Bundle bundle) {
		NAV_ITEM service;
		if (bundle != null) {
			service = (NAV_ITEM) bundle.getSerializable(Const.EXTRA_API);
		} else {
			service = NAV_ITEM.getDefault();
		}
		return service;
	}

}
