package com.pluu.webtoon.common;

import android.util.Log;

import java.io.IOException;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Created by PLUUSYSTEM-NEW on 2015-10-29.
 */
public class LoggingInterceptor implements Interceptor {
	@Override public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();

		long t1 = System.nanoTime();
		Log.i(Const.LOG_TAG, String.format("Sending requestApi %s on %s%n%s",
										 request.url(), chain.connection(), request.headers()));

		Response response = chain.proceed(request);

		long t2 = System.nanoTime();
		Log.i(Const.LOG_TAG, String.format("Received response for %s in %.1fms%n%s",
							response.request().url(), (t2 - t1) / 1e6d, response.headers()));

		return response;
	}
}
