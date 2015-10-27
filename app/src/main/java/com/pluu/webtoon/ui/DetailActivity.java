package com.pluu.webtoon.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.pluu.support.BaseApiImpl;
import com.pluu.webtoon.AppController;
import com.pluu.webtoon.R;
import com.pluu.webtoon.api.Detail;
import com.pluu.webtoon.api.DetailView;
import com.pluu.webtoon.api.Episode;
import com.pluu.webtoon.api.ShareItem;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.db.InjectDB;
import com.squareup.sqlbrite.BriteDatabase;

/**
 * 상세화면 Activity
 * Created by nohhs on 15. 3. 2.
 */
public class DetailActivity extends AppCompatActivity {
	private final String TAG = DetailActivity.class.getSimpleName();

	@Bind(R.id.webview)
	WebView webview;
	@Bind(R.id.toolbar_actionbar)
	Toolbar toolbar;
	@Bind(R.id.btnPrev)
	Button btnPrev;
	@Bind(R.id.btnNext)
	Button btnNext;
	@Bind(R.id.tvTitle)
	TextView tvTitle;
	@Bind(R.id.tvSubTitle)
	TextView tvSubTitle;
	@Bind(R.id.bottomMenu)
	LinearLayout bottomMenu;

	private ProgressDialog dlg;
	private int titleColor, statusColor;
	private int actionBarHeight;

	private int SWIPE_MIN_DISTANCE;
	private int SWIPE_THRESHOLD_VELOCITY;
	private ObjectAnimator statusBarAnimator;

	private BaseApiImpl serviceApi;
	private Detail currentItem;
	private Episode episode;

	@Inject
	BriteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		ButterKnife.bind(this);
		AppController.objectGraph(this).inject(this);

		setSupportActionBar(toolbar);
		initSupportActionBar();
		getApi();
		initView();
		initWebViewSetting();
		initLoad();
	}

	private void initSupportActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_36dp);

		TypedValue t = new TypedValue();
		getTheme().resolveAttribute(android.R.attr.actionBarSize, t, true);
		actionBarHeight = getResources().getDimensionPixelSize(t.resourceId);
	}

	private void getApi() {
		Intent intent = getIntent();
		serviceApi = Const.getServiceApi(intent);
	}

	private void initView() {
		episode = getIntent().getParcelableExtra(Const.EXTRA_EPISODE);
		tvSubTitle.setText(episode.getTitle());

		dlg = new ProgressDialog(this);
		dlg.setMessage(getString(R.string.msg_loading));

		titleColor = getIntent().getIntExtra(Const.EXTRA_MAIN_COLOR, Color.BLACK);
		statusColor = getIntent().getIntExtra(Const.EXTRA_STATUS_COLOR, Color.BLACK);

		TypedValue value = new TypedValue();
		getTheme().resolveAttribute(R.attr.colorPrimary, value, true);

		ValueAnimator bg = ValueAnimator.ofObject(new ArgbEvaluator(), value.data, titleColor);
		bg.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Integer value = (Integer) animation.getAnimatedValue();
				toolbar.setBackgroundColor(value);
				btnPrev.setBackgroundColor(value);
				btnNext.setBackgroundColor(value);
			}
		});
		bg.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				btnNext.setBackgroundDrawable(getStateListBgDrawable());
				btnPrev.setBackgroundDrawable(getStateListBgDrawable());

				btnNext.setTextColor(getStateListTextDrawable());
				btnPrev.setTextColor(getStateListTextDrawable());
			}
		});
		bg.setDuration(1000L);
		bg.setInterpolator(new DecelerateInterpolator());
		bg.start();

		changeStatusBar();
	}

	private void initWebViewSetting() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
		} else {
			webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
		}

		webview.getSettings().setBlockNetworkImage(false);
		webview.getSettings().setLoadsImagesAutomatically(true);
		webview.getSettings().setUseWideViewPort(false);
		webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				dlg.dismiss();
				mToggleHandler.sendEmptyMessageDelayed(0, 1500L);
			}
		});

		final GestureDetector gd = new GestureDetector(this, listener);
		webview.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gd.onTouchEvent(event);
			}
		});
		webview.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				// LongClick Disable
				return true;
			}
		});

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		SWIPE_MIN_DISTANCE = metrics.widthPixels / 3;
		SWIPE_THRESHOLD_VELOCITY = metrics.widthPixels / 2;
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void changeStatusBar() {
		if (statusBarAnimator != null) {
			statusBarAnimator.cancel();
		}
		ArgbEvaluator argbEvaluator = new ArgbEvaluator();
		TypedValue resValue = new TypedValue();
		getTheme().resolveAttribute(R.attr.colorPrimaryDark, resValue, true);
		statusBarAnimator = ObjectAnimator.ofInt(getWindow(), "statusBarColor", resValue.data,
												 statusColor);
		statusBarAnimator.setDuration(250L);
		statusBarAnimator.setEvaluator(argbEvaluator);
		statusBarAnimator.start();
	}

	private StateListDrawable getStateListBgDrawable() {
		StateListDrawable list = new StateListDrawable();
		// disabled
		list.addState(new int[]{-android.R.attr.state_enabled}, new ColorDrawable(Color.GRAY));
		// pressed
		list.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(Color.WHITE));
		// enabled
		list.addState(new int[]{android.R.attr.state_enabled}, new ColorDrawable(titleColor));
		return list;
	}

	private ColorStateList getStateListTextDrawable() {
		int[][] state = {
			new int[]{-android.R.attr.state_enabled},
			new int[]{android.R.attr.state_pressed},
			new int[]{android.R.attr.state_enabled},
		};

		int[] colors = {
			Color.WHITE,
			titleColor,
			Color.WHITE
		};

		return new ColorStateList(state, colors);
	}

	private void initLoad() {
		loading(episode, episode.getUrl());
	}

	private void loading(Episode item, String url) {
		Log.i(TAG, "Load Detail=" + url);
		if (currentItem != null) {
			currentItem.prevLink = currentItem.nextLink = null;
		}
		new AsyncTask<Object, Void, Detail>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				dlg.show();
			}

			@Override
			protected Detail doInBackground(Object... params) {
				return serviceApi.parseDetail(getBaseContext(), (Episode) params[0], (String) params[1]);
			}

			@Override
			protected void onPostExecute(final Detail item) {
				if (item != null && item.errorType != null) {
					dlg.dismiss();
					AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
					builder.setMessage(item.errorMsg)
						   .setCancelable(false)
						   .setPositiveButton(android.R.string.ok,
											  new DialogInterface.OnClickListener() {
												  @Override
												  public void onClick(DialogInterface dialogInterface, int i) {
													  finish();
												  }
											  })
						   .show();
					return;
				} else if (item == null || item.list.isEmpty()) {
					Toast.makeText(getBaseContext(), R.string.network_fail,
								   Toast.LENGTH_SHORT).show();
					dlg.dismiss();
					finish();
					return;
				}

				InjectDB.updateDetail(db, serviceApi.getClass().getSimpleName(), item);

				currentItem = item;
				tvTitle.setText(item.title);
				btnPrev.setEnabled(!TextUtils.isEmpty(item.prevLink));
				btnNext.setEnabled(!TextUtils.isEmpty(item.nextLink));
				loadWebView(item.list);
			}
		}.execute(item, url);
	}

	private void loadWebView(List<DetailView> list) {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("<!DOCTYPE html>")
				 .append("<html>")
				 .append("<head><meta charset=\"utf-8\"><style>")
				 .append("body{").append("padding-top:").append(actionBarHeight).append("px; padding-bottom:").append(bottomMenu.getHeight()).append("px; }")
				 .append("img{max-width: 100%; height: auto; display:block;}")
				 .append("ul{list-style: none; padding-left: 0px;}")
			.append("ul li:before{padding:0px; position:absolute; top:0; left:0px; }")
				 .append("</style></head>")
				 .append("<body>");

		strBuffer.append("<ul>");

		Iterator<DetailView> iterator = list.iterator();
		DetailView item;
		while (iterator.hasNext()) {
			item = iterator.next();
			Log.i(TAG, "Load=" + item);
			strBuffer.append("<li>");

			switch (item.getType()) {
				case IMAGE:
					strBuffer.append("<img src=\"").append(item.getValue()).append("\" />");
					break;
				case TEXT:
					strBuffer.append(item.getValue().replaceAll("\n", "<br></br>"));
					break;
			}
			strBuffer.append("</li>");
		}

		strBuffer.append("</ul></body></html>");
		Log.i(TAG, "Result=" + strBuffer.toString());
		webview.loadDataWithBaseURL(null, strBuffer.toString(), "text/html", "utf-8", null);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}

		switch (item.getItemId()) {
			case R.id.menu_item_share:
				// 공유하기
				if (currentItem != null && serviceApi != null) {
					ShareItem sender = serviceApi.getDetailShare(episode, currentItem);
					if (sender != null) {
						Log.i(TAG, "Share=" + sender);
						Intent intent = new Intent(Intent.ACTION_SEND);
						intent.setType("text/plain");
						intent.putExtra(Intent.EXTRA_SUBJECT, sender.title);
						intent.putExtra(Intent.EXTRA_TEXT, sender.url);
						startActivity(Intent.createChooser(intent, "Share"));
					}
				}
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	@OnClick({R.id.btnPrev, R.id.btnNext})
	public void onMovePage(View view) {
		String link;
		link = view.getId() == R.id.btnPrev ? currentItem.prevLink : currentItem.nextLink;
		if (TextUtils.isEmpty(link)) {
			return;
		}
		int idx = view.getId() == R.id.btnPrev ? currentItem.prevIdx : currentItem.nextIdx;
		episode.setTag(idx);
		loading(episode, link);
	}

	private final Handler mToggleHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			toggleHideBar();
			return true;
		}
	});

	/**
	 * Detects and toggles immersive mode.
	 */
	private void toggleHideBar() {
		int uiOptions = getWindow().getDecorView().getSystemUiVisibility();

		if ((uiOptions & View.SYSTEM_UI_FLAG_LOW_PROFILE) == 0) {
			moveToAxisY(toolbar, true);
			moveToAxisY(bottomMenu, false);
		} else {
			moveRevert(toolbar);
			moveRevert(bottomMenu);
		}

		int newUiOptions = uiOptions;
		newUiOptions ^= View.SYSTEM_UI_FLAG_LOW_PROFILE;
		getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
	}

	private void moveToAxisY(View view, boolean isToTop) {
		view.animate()
			.translationY(isToTop ? -view.getHeight() : view.getHeight())
			.start();
	}

	private void moveRevert(View view) {
		view.animate().translationY(0).start();
	}

	private final GestureDetector.SimpleOnGestureListener listener
		= new GestureDetector.SimpleOnGestureListener() {
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			mToggleHandler.removeMessages(0);
			mToggleHandler.sendEmptyMessage(0);
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
				&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				onMovePage(btnNext);
				return true;
			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
				&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				onMovePage(btnPrev);
				return true;
			}

			return super.onFling(e1, e2, velocityX, velocityY);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_detail, menu);
		return true;
	}
}
