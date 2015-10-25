package com.pluu.webtoon.api;

/**
 * Created by nohhs on 2015-03-25.
 */
public class LoginResultInfo {
	public String key;
	public boolean isError;
	public String errorMsg;

	@Override
	public String toString() {
		return "LoginResultInfo{" +
			"key='" + key + '\'' +
			", isError=" + isError +
			", errorMsg='" + errorMsg + '\'' +
			'}';
	}
}
