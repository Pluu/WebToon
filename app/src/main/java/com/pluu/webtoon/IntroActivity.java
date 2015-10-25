package com.pluu.webtoon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.pluu.support.ApiImpl;
import com.pluu.support.BaseApiImpl;
import com.pluu.support.DaumApi;
import com.pluu.support.NaverApi;
import com.pluu.support.OllehApi;
import com.pluu.webtoon.api.LoginResultInfo;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
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
		Observable.concat(getLoginObservable(), getIntroObservable())
				  .subscribeOn(AndroidSchedulers.mainThread())
				  .subscribe(new Subscriber() {
					  @Override
					  public void onCompleted() {
						  startActivity(new Intent(IntroActivity.this, MainActivity.class));
						  finish();
					  }

					  @Override
					  public void onError(Throwable e) { }

					  @Override
					  public void onNext(Object o) { }
				  });
	}

	/**
	 * Login 처리
	 * @return Observable
	 */
	private Observable<LoginResultInfo> getLoginObservable() {
		return Observable.merge(
			mockClient(DaumApi.class),
			mockClient(NaverApi.class),
			mockClient(OllehApi.class)
			)
						 .subscribeOn(Schedulers.newThread())
						 .observeOn(AndroidSchedulers.mainThread())
						 .finallyDo(new Action0() {
							 @Override
							 public void call() {
								 Log.i(TAG, "Login Process Complete");
								 tvMsg.setText("다됐어.. 이제 갈거야...");
								 progressBar.setVisibility(View.INVISIBLE);
							 }
						 });
	}

	/**
	 * Login 처리후 1초 Intro 화면 표시
	 * @return Observable
	 */
	private Observable getIntroObservable() {
		return Observable.empty().delay(1 * 1000, TimeUnit.MILLISECONDS);
	}

	/**
	 * Login Service Observable mock
	 * @param api Service API
	 * @return Observable
	 */
	public Observable<LoginResultInfo> mockClient(final Class<? extends BaseApiImpl> api) {
		return Observable.create(new Observable.OnSubscribe<LoginResultInfo>() {
			@Override
			public void call(Subscriber<? super LoginResultInfo> subscriber) {
				LoginResultInfo info = null;
				try {
					ApiImpl serviceApi = api.getConstructor().newInstance();
					serviceApi.loginDataInit(getBaseContext());
					info = serviceApi.autoLogin(getBaseContext());
				} catch (Exception e) {
					e.printStackTrace();
				}

				subscriber.onNext(info);
				subscriber.onCompleted();
			}
		}).subscribeOn(Schedulers.newThread());
	}

}
