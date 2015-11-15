package com.pluu.webtoon.ui;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.pluu.support.impl.AbstractEpisodeApi;
import com.pluu.support.impl.ServiceConst;
import com.pluu.webtoon.R;
import com.pluu.webtoon.adapter.EpisodeAdapter;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.db.RealmHelper;
import com.pluu.webtoon.item.Episode;
import com.pluu.webtoon.item.EpisodePage;
import com.pluu.webtoon.item.WebToonInfo;
import com.pluu.webtoon.model.REpisode;
import com.pluu.webtoon.utils.MoreRefreshListener;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 에피소드 리스트 Activity
 * Created by anchangbeom on 15. 2. 26..
 */
public class EpisodesActivity extends AppCompatActivity
	implements SwipeRefreshLayout.OnRefreshListener {

	private final String TAG = EpisodesActivity.class.getSimpleName();

	@Bind(R.id.swipe_refresh_widget)
	SwipeRefreshLayout swipeRefreshWidget;
	@Bind(android.R.id.list)
	RecyclerView recyclerView;
	@Bind(R.id.toolbar_actionbar)
	Toolbar toolbar;
	@Bind(R.id.btnFirst)
	Button btnFirst;
	@Bind(R.id.tvName)
	TextView tvName;
	@Bind(R.id.tvRate)
	TextView tvRate;
	private View childTitle;

	private String nextLink;
	private ProgressDialog loadDlg;
	private EpisodeAdapter adapter;

	private GridLayoutManager manager;
	private WebToonInfo webToonInfo;
	private int titleColor, statusColor;

	private AbstractEpisodeApi serviceApi;

	private final CompositeSubscription mCompositeSubscription
		= new CompositeSubscription();

	private Episode firstItem;

	private boolean isEdit = false;
	private boolean isFavorite = false;
	private ServiceConst.NAV_ITEM service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_episode);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);

		webToonInfo = getIntent().getParcelableExtra(Const.EXTRA_EPISODE);
		isFavorite = webToonInfo.isFavorite();

		initSupportActionBar();
		getApi();
		initView();
		loading();
	}

	private void initSupportActionBar() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setTitle(webToonInfo.getTitle());
			actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_36dp);
		}
	}

	private void getApi() {
		Intent intent = getIntent();
		service = (ServiceConst.NAV_ITEM) intent.getSerializableExtra(Const.EXTRA_API);
		serviceApi = AbstractEpisodeApi.getApi(service);
	}

	private void initView() {
		titleColor = getIntent().getIntExtra(Const.EXTRA_MAIN_COLOR, Color.BLACK);
		statusColor = getIntent().getIntExtra(Const.EXTRA_STATUS_COLOR, Color.BLACK);

		for (int i = 0; i < toolbar.getChildCount(); i++) {
			if (toolbar.getChildAt(i) instanceof TextView) {
				childTitle = toolbar.getChildAt(i);
				break;
			}
		}

		TypedValue value = new TypedValue();
		getTheme().resolveAttribute(R.attr.colorPrimary, value, true);

		ValueAnimator bg = ValueAnimator.ofObject(new ArgbEvaluator(), value.data, titleColor);
		bg.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Integer value = (Integer) animation.getAnimatedValue();
				toolbar.setBackgroundColor(value);
				btnFirst.setBackgroundColor(value);
				if (childTitle != null) {
					childTitle.setBackgroundColor(value);
				}

				tvName.setTextColor(value);
				tvRate.setTextColor(value);
			}
		});
		bg.setDuration(2000L);
		bg.setInterpolator(new DecelerateInterpolator());
		bg.start();

		changeStatusBar();

		tvName.setText(webToonInfo.getWriter());
		if (!TextUtils.isEmpty(webToonInfo.getRate())) {
			tvRate.setText(webToonInfo.getRate());
			tvRate.setVisibility(View.VISIBLE);
		}

		swipeRefreshWidget.setColorSchemeResources(
			R.color.color1,
			R.color.color2,
			R.color.color3,
			R.color.color4);
		swipeRefreshWidget.setOnRefreshListener(this);

		loadDlg = new ProgressDialog(this);
		loadDlg.setCancelable(false);
		loadDlg.setMessage("Loading ...");
		adapter = new EpisodeAdapter(this) {
			@Override
			public ViewHolder onCreateViewHolder(ViewGroup viewGroup,
												 int position) {
				final ViewHolder vh = super.onCreateViewHolder(viewGroup, position);
				vh.thumbnailView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Episode item = adapter.getItem(vh.getAdapterPosition());
						if (item.isLoginNeed()) {
							Toast.makeText(EpisodesActivity.this,
										   R.string.msg_not_support,
										   Toast.LENGTH_SHORT).show();
						}
						moveDetailPage(item);
					}
				});
				return vh;
			}
		};

		int columnCount = getResources().getInteger(R.integer.episode_column_count);
		manager = new GridLayoutManager(this, columnCount);
		recyclerView.setLayoutManager(manager);
		recyclerView.setAdapter(adapter);
		recyclerView.setOnScrollListener(scrollListener);
	}

	private ObjectAnimator statusBarAnimator;

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void changeStatusBar() {
		if (statusBarAnimator != null) {
			statusBarAnimator.cancel();
		}
		ArgbEvaluator argbEvaluator = new ArgbEvaluator();
		TypedValue resValue = new TypedValue();
		getTheme().resolveAttribute(R.attr.colorPrimaryDark, resValue, true);
		statusBarAnimator = ObjectAnimator.ofInt(getWindow(), "statusBarColor", resValue.data, statusColor);
		statusBarAnimator.setDuration(250L);
		statusBarAnimator.setEvaluator(argbEvaluator);
		statusBarAnimator.start();
	}

	private void loading() {
		loadDlg.show();
		if (swipeRefreshWidget.isRefreshing()) {
			swipeRefreshWidget.setRefreshing(false);
		}

		getRequestApi()
			.subscribeOn(Schedulers.newThread())
			.map(getReadAction())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(getRequestSubscriber());
	}

//	@RxLogObservable
	private Observable<List<Episode>> getRequestApi() {
		return Observable
			.create(new Observable.OnSubscribe<List<Episode>>() {
				@Override
				public void call(Subscriber<? super List<Episode>> subscriber) {
					Log.i(TAG, "Load Episode=" + webToonInfo.getWebtoonId());
					EpisodePage episodePage = serviceApi.parseEpisode(webToonInfo);
					List<Episode> list = episodePage.getEpisodes();
					nextLink = episodePage.moreLink();
					if (!TextUtils.isEmpty(nextLink)) {
						scrollListener.setLoadingMorePause();
					}
					subscriber.onNext(list);
					subscriber.onCompleted();
				}
			});
	}

	@NonNull
	private Func1<List<Episode>, List<Episode>> getReadAction() {
		return new Func1<List<Episode>, List<Episode>>() {
			@Override
			public List<Episode> call(List<Episode> list) {
				RealmHelper helper = RealmHelper.getInstance();
				List<REpisode> readList = helper.getEpisode(getBaseContext(),
														   service,
														   webToonInfo.getWebtoonId());
				for (REpisode readItem : readList) {
					for (Episode episode : list) {
						if (readItem.getEpisodeId().equals(episode.getEpisodeId())) {
							episode.setReadFlag();
							break;
						}
					}
				}
				return list;
			}
		};
	}

//	@RxLogSubscriber
	@NonNull
	private Subscriber<List<Episode>> getRequestSubscriber() {
		return new Subscriber<List<Episode>>() {
			@Override
			public void onCompleted() { }

			@Override
			public void onError(Throwable e) {
				loadDlg.dismiss();
			}

			@Override
			public void onNext(List<Episode> list) {
				if (list == null || list.isEmpty()) {
					loadDlg.dismiss();
					if (list == null) {
						Toast.makeText(getBaseContext(), R.string.network_fail,
									   Toast.LENGTH_SHORT).show();
						finish();
					}
					return;
				}
				adapter.addItems(list);
				adapter.notifyDataSetChanged();
				loadDlg.dismiss();
			}
		};
	}

	private final MoreRefreshListener scrollListener = new MoreRefreshListener() {
		@Override
		public void onMoreRefresh() {
			moreLoad();
		}
	};

	private void moreLoad() {
		if (!TextUtils.isEmpty(nextLink)) {
			Log.i(TAG, "Next Page Link=" + nextLink);
			loading();
			nextLink = null;
		}
	}

	@Override
	public void onRefresh() {
		adapter.clear();
		serviceApi.init();
		loading();
	}

	@OnClick(R.id.btnFirst)
	public void firstViewClick() {
		Episode item = adapter.getItem(0);
		if (item.isLoginNeed()) {
			Toast.makeText(this, R.string.msg_not_support, Toast.LENGTH_SHORT).show();
			return;
		}

		if (firstItem == null) {
			firstItem = serviceApi.getFirstEpisode(item);
		}
		if (firstItem == null) {
			return;
		}
		firstItem.setTitle(this.webToonInfo.getTitle());
		moveDetailPage(firstItem);
	}

	private void moveDetailPage(Episode item) {
		Intent intent = new Intent(this, DetailActivity.class);
		intent.putExtra(Const.EXTRA_API, service);
		intent.putExtra(Const.EXTRA_EPISODE, item);
		intent.putExtra(Const.EXTRA_MAIN_COLOR, titleColor);
		intent.putExtra(Const.EXTRA_STATUS_COLOR, statusColor);
		startActivityForResult(intent, 0);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
			|| newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

			int spanCount = getResources().getInteger(R.integer.episode_column_count);
			manager.setSpanCount(spanCount);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}

		readUpdate();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Glide.with(this).resumeRequests();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Glide.with(this).pauseRequests();
	}

	@Override
	public void finish() {
		if (isEdit) {
			Intent intent = new Intent();
			intent.putExtra(Const.EXTRA_EPISODE, webToonInfo);
			setResult(RESULT_OK, intent);
		}
		super.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mCompositeSubscription.unsubscribe();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_episode, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean ret = super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.menu_item_favorite_add).setVisible(!isFavorite);
		menu.findItem(R.id.menu_item_favorite_delete).setVisible(isFavorite);
		return ret;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}

		switch (item.getItemId()) {
			case R.id.menu_item_favorite_add:
				// 즐겨찾기 추가
				RealmHelper.getInstance()
						   .addFavorite(this, service, webToonInfo.getWebtoonId());
				setFavorite(true);
				break;
			case R.id.menu_item_favorite_delete:
				// 즐겨찾기 삭제
				RealmHelper.getInstance()
						   .removeFavorite(this, service, webToonInfo.getWebtoonId());
				setFavorite(false);
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void setFavorite(boolean isFavorite) {
		isEdit = true;
		this.isFavorite = isFavorite;
		webToonInfo.setIsFavorite(isFavorite);

		Toast.makeText(this,
					   isFavorite ? R.string.favorite_add : R.string.favorite_delete,
					   Toast.LENGTH_SHORT).show();
	}

	private void readUpdate() {
		loadDlg.show();
		Observable.create(new Observable.OnSubscribe<List<String>>() {
			@Override
			public void call(Subscriber<? super List<String>> subscriber) {
				RealmHelper helper = RealmHelper.getInstance();
				List<REpisode> list = helper.getEpisode(getBaseContext(),
														service,
														webToonInfo.getWebtoonId());

				List<String> result = new ArrayList<>(list.size());
				for (REpisode item : list) {
					result.add(item.getEpisodeId());
				}
				subscriber.onNext(result);
				subscriber.onCompleted();
			}})
			.subscribeOn(Schedulers.newThread())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new Subscriber<List<String>>() {
				@Override
				public void onCompleted() { }

				@Override
				public void onError(Throwable e) {
					loadDlg.dismiss();
				}

				@Override
				public void onNext(List<String> list) {
					adapter.updateRead(list);
					adapter.notifyDataSetChanged();
					loadDlg.dismiss();
				}
			});
	}

}
