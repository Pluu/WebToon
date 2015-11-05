package com.pluu.support.impl;

import android.os.Bundle;

import com.pluu.webtoon.R;
import com.pluu.webtoon.common.Const;

/**
 * Service Const Class
 * Created by PLUUSYSTEM-NEW on 2015-11-01.
 */
public class ServiceConst {

	public enum NAV_ITEM {
		NAVER(true),
		DAUM(true),
		OLLEH(true),
		KAKAOPAGE(true),
		NATE(true),
		T_STORE(true),
		SEPARATOR(false),		// Separator
		INVALID(false);			// Only Temp

		public final boolean isSelect;

		NAV_ITEM(boolean isSelect) {this.isSelect = isSelect;}
		public static NAV_ITEM getDefault() {
			return NAVER;
		}
	}

	// titles for navdrawer items (indices must correspond to the above)
	public static final int[] NAVDRAWER_TITLE_RES_ID = new int[]{
		R.string.title_naver,
		R.string.title_duam,
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
