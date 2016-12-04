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

import com.pluu.webtoon.R;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.common.PrefConfig;
import com.pluu.webtoon.db.SqliteToRealm;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 인트로 화면 Activity
 * Created by nohhs on 15. 3. 25.
 */
public class IntroActivity extends Activity {
	private final String TAG = IntroActivity.class.getSimpleName();

	@BindView(R.id.tvMsg)
	TextView tvMsg;
	@BindView(R.id.progressBar)
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
				.subscribe(o -> {
                    Log.i(TAG, "Login Process Complete");
                    tvMsg.setText(R.string.msg_intro_complete);
                    progressBar.setVisibility(View.INVISIBLE);

                    startActivity(new Intent(IntroActivity.this, MainActivity.class));
                    finish();
                });
	}

//	@RxLogObservable
	private Observable<Object> getIntro() {
		return Observable.empty().delay(1, TimeUnit.SECONDS);
	}

//	@RxLogObservable
	private Observable<Object> getSqlite2Realm() {
		return Observable.fromCallable(() -> {
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
            return Observable.empty();
        });
	}

}
