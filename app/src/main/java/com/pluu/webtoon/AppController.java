package com.pluu.webtoon;

import android.app.Application;

import com.pluu.webtoon.di.DaggerNetworkComponent;
import com.pluu.webtoon.di.DaggerRealmHelperComponent;
import com.pluu.webtoon.di.NetworkComponent;
import com.pluu.webtoon.di.RealmHelperComponent;

import io.realm.Realm;

/**
 * Application Controller
 * Created by nohhs on 2015-03-17.
 */
public class AppController extends Application {

	private NetworkComponent networkComponent;
	private RealmHelperComponent realmComponent;

	@Override
	public void onCreate() {
		super.onCreate();

//		refWatcher = LeakCanary.install(this);
		initApplicationComponent();
		Realm.init(this);
	}

	private void initApplicationComponent() {
		this.networkComponent = DaggerNetworkComponent.builder()
				.build();

		this.realmComponent = DaggerRealmHelperComponent.builder()
				.build();
	}

	public NetworkComponent getNetworkComponent() {
		return networkComponent;
	}

	public RealmHelperComponent getRealmHelperComponent() {
		return realmComponent;
	}

//
//	public static RefWatcher getRefWatcher(Context context) {
//		AppController application = (AppController) context.getApplicationContext();
//		return application.refWatcher;
//	}
//
//	private RefWatcher refWatcher;

}
