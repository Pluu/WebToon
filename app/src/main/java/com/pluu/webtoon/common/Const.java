package com.pluu.webtoon.common;

import android.content.Intent;

import com.pluu.support.BaseApiImpl;
import com.pluu.support.naver.NaverApi;

/**
 * Created by nohhs on 2015-03-02.
 */
public class Const {
	public static final String LOG_TAG = "PluuToon";
	public static final String EXTRA_API = "EXTRA_API";
	public static final String EXTRA_URL = "EXTRA_URL";
	public static final String EXTRA_EPISODE = "EXTRA_EPISODE";
	public static final String EXTRA_MAIN_COLOR = "EXTRA_MAIN_COLOR";
	public static final String EXTRA_STATUS_COLOR = "EXTRA_STATUS_COLOR";
	public static final String ARG_API = "api";

	private static final String RATE_FORMAT = "평점 : %.2f";

	public static final String MAIN_FRAG_TAG = "main_frag_tag";

	public static BaseApiImpl getServiceApi(Intent intent) {
		BaseApiImpl serviceApi;
		if (intent == null || !intent.hasExtra(EXTRA_API)) {
			serviceApi = new NaverApi();
		} else {
			try {
				Class<BaseApiImpl> target = (Class<BaseApiImpl>) intent.getSerializableExtra(EXTRA_API);
				serviceApi = getServiceApi(target);
			} catch (Exception e) {
				e.printStackTrace();
				serviceApi = new NaverApi();
			}
		}
		return serviceApi;
	}

	public static BaseApiImpl getServiceApi(Class<? extends BaseApiImpl> target) {
		BaseApiImpl serviceApi;
		try {
			serviceApi = target.getConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			serviceApi = new NaverApi();
		}
		return serviceApi;
	}

	public static String getRateNameByRate(String data) {
		return String.format(RATE_FORMAT, Double.valueOf(data));
	}

}
