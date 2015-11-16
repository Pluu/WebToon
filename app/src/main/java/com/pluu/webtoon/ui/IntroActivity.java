package com.pluu.webtoon.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.pluu.webtoon.R;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.common.PrefConfig;
import com.pluu.webtoon.db.SqliteToRealm;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 인트로 화면 Activity
 * Created by nohhs on 15. 3. 25.
 */
public class IntroActivity extends Activity {
	private final String TAG = IntroActivity.class.getSimpleName();

	@Bind(R.id.tvMsg)
	TextView tvMsg;
	@Bind(R.id.progressBar)
	ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		ButterKnife.bind(this);

		initWork();
	}

	private void initWork() {
		Observable.concat(getSqlite2Realm(), getIntro())
			.subscribeOn(Schedulers.newThread())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(getIntroSubscriber());
	}

//	@RxLogObservable
	private Observable<Object> getIntro() {
		return Observable
			.empty().delay(1, TimeUnit.SECONDS);
	}

//	@RxLogObservable
	private Observable<Object> getSqlite2Realm() {
		return Observable.create(new Observable.OnSubscribe<Object>() {
			@Override
			public void call(Subscriber<? super Object> subscriber) {
				final String keyMigrate = "MIGRATE";

				Context context = getBaseContext();
				SharedPreferences pref = PrefConfig.getPreferences(context, Const.CONFIG_NAME);
				if (!pref.getBoolean(keyMigrate, false)) {
					SqliteToRealm migrate = new SqliteToRealm();
					migrate.migrateToon(context);
					migrate.migrateEpisode(context);
					migrate.complete(context);
					pref.edit().putBoolean(keyMigrate, true).apply();
				}
				subscriber.onNext(null);
				subscriber.onCompleted();
			}
		});
	}

	private Subscriber<Object> getIntroSubscriber() {
		return new Subscriber<Object>() {
			@Override
			public void onCompleted() {
				Log.i(TAG, "Login Process Complete");
				tvMsg.setText(R.string.msg_intro_complete);
				progressBar.setVisibility(View.INVISIBLE);

				startActivity(new Intent(IntroActivity.this, MainActivity.class));
				finish();
			}

			@Override
			public void onError(Throwable e) { }

			@Override
			public void onNext(Object o) { }
		};
	}

}
