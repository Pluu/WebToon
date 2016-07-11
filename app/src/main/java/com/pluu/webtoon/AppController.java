package com.pluu.webtoon;

import android.app.Application;

import com.pluu.webtoon.di.DaggerNetworkComponent;
import com.pluu.webtoon.di.NetworkComponent;

/**
 * Application Controller
 * Created by nohhs on 2015-03-17.
 */
public class AppController extends Application {

	private NetworkComponent component;

	@Override
	public void onCreate() {
		super.onCreate();

//		refWatcher = LeakCanary.install(this);
		initApplicationComponent();
	}

	private void initApplicationComponent() {
		this.component = DaggerNetworkComponent.builder()
				.build();
	}

	public NetworkComponent getNetworkComponent() {
		return component;
	}
//
//	public static RefWatcher getRefWatcher(Context context) {
//		AppController application = (AppController) context.getApplicationContext();
//		return application.refWatcher;
//	}
//
//	private RefWatcher refWatcher;

}
