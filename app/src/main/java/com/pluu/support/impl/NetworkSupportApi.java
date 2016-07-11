package com.pluu.support.impl;

import android.content.Context;

import com.pluu.webtoon.AppController;
import com.pluu.webtoon.network.NetworkTask;

import java.util.Collections;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Network Support API Class
 * Created by PLUUSYSTEM-NEW on 2015-10-29.
 */
public abstract class NetworkSupportApi implements IRequest {

	@Inject
	OkHttpClient client;
	public static final String POST = "POST";
	public static final String GET = "GET";

	public NetworkSupportApi(Context context) {
		((AppController) context.getApplicationContext()).getNetworkComponent().inject(this);
	}

	public abstract String getMethod();

	public abstract String getUrl();

	@Override
	public Map<String, String> getParams() {
		return Collections.emptyMap();
	}

	@Override
	public Map<String, String> getHeaders() {
		return Collections.emptyMap();
	}

	protected String requestApi() throws Exception {
		return new NetworkTask(client).requestApi(this);
	}

	protected String requestApi(Request request) throws Exception {
		return new NetworkTask(client).requestApi(request);
	}

}
