package com.pluu.webtoon.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pluu.webtoon.AppController;
import com.pluu.webtoon.R;
import com.pluu.webtoon.db.RealmHelper;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 인트로 화면 Activity
 * Created by nohhs on 15. 3. 25.
 */
public class IntroActivity extends Activity {
	private final String TAG = IntroActivity.class.getSimpleName();

	@BindView(R.id.tvMsg) TextView tvMsg;
	@BindView(R.id.progressBar) ProgressBar progressBar;
	@Inject RealmHelper realmHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		ButterKnife.bind(this);
		((AppController) getApplicationContext()).getRealmHelperComponent().inject(this);

		initWork();
	}

	private void initWork() {
		Single.fromCallable(() -> "")
                .delay(1, TimeUnit.SECONDS)
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

}
