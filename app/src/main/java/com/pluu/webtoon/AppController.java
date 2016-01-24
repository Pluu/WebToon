package com.pluu.webtoon;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.RefWatcher;

/**
 * Application Controller
 * Created by nohhs on 2015-03-17.
 */
public class AppController extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

//		refWatcher = LeakCanary.install(this);
	}

	public static RefWatcher getRefWatcher(Context context) {
		AppController application = (AppController) context.getApplicationContext();
		return application.refWatcher;
	}

	private RefWatcher refWatcher;

}
