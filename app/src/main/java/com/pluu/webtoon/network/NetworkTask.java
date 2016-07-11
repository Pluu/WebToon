package com.pluu.webtoon.network;

import android.net.Uri;

import com.pluu.support.impl.IRequest;
import com.pluu.support.impl.NetworkSupportApi;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Network Request Task
 * <br/> 실제 Request 하는 로직
 * Created by PLUUSYSTEM-NEW on 2015-10-29.
 */
public class NetworkTask {

	private final OkHttpClient client;

	public NetworkTask(OkHttpClient client) {
		this.client = client;
	}

	public String requestApi(final IRequest request) throws Exception {
		Request.Builder builder = new Request.Builder();

		for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
			builder.addHeader(entry.getKey(), entry.getValue());
		}

		if (NetworkSupportApi.POST.equals(request.getMethod())) {
			// POST
			FormBody.Builder formBuilder = new FormBody.Builder();
			for (Map.Entry<String, String> entry : request.getParams().entrySet()) {
				formBuilder.add(entry.getKey(), entry.getValue());
			}
			RequestBody requestBody = formBuilder.build();
			builder.post(requestBody);
			builder.url(request.getUrl());
		} else {
			// GET
			Uri.Builder uriBuilder = new Uri.Builder();
			uriBuilder.encodedPath(request.getUrl());
			for (Map.Entry<String, String> entry : request.getParams().entrySet()) {
				uriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
			}
			builder.url(uriBuilder.build().toString());
		}

		return requestApi(builder.build());
	}

	public String requestApi(Request request) throws Exception {
		Response response = client.newCall(request).execute();
		return response.body().string();
	}

}
