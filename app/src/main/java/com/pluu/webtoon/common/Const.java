package com.pluu.webtoon.common;

/**
 * Created by nohhs on 2015-03-02.
 */
public class Const {
	public static final String LOG_TAG = "PluuToon";
	public static final String EXTRA_API = "EXTRA_API";
	public static final String EXTRA_POS = "EXTRA_POS";
	public static final String EXTRA_EPISODE = "EXTRA_EPISODE";
	public static final String EXTRA_MAIN_COLOR = "EXTRA_MAIN_COLOR";
	public static final String EXTRA_STATUS_COLOR = "EXTRA_STATUS_COLOR";

	private static final String RATE_FORMAT = "평점 : %.2f";

	public static final String MAIN_FRAG_TAG = "main_frag_tag";

	public static String getRateNameByRate(String data) {
		return String.format(RATE_FORMAT, Double.valueOf(data));
	}

}
