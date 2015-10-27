package com.pluu.webtoon.ui;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pluu.support.BaseApiImpl;
import com.pluu.webtoon.AppController;
import com.pluu.webtoon.R;
import com.pluu.webtoon.api.Episode;
import com.pluu.webtoon.api.WebToon;
import com.pluu.webtoon.api.WebToonInfo;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.db.InjectDB;
import com.pluu.webtoon.utils.MoreRefreshListener;
import com.squareup.sqlbrite.BriteDatabase;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by anchangbeom on 15. 2. 26..
 */
public class EpisodesActivity extends AppCompatActivity
	implements SwipeRefreshLayout.OnRefreshListener {

	private final String TAG = EpisodesActivity.class.getSimpleName();
	private static final String RECYCLER_TAG = "EPISODE_RECYCLER_VIEW";

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
	private ListAdapter adapter;

	private GridLayoutManager manager;
	private WebToonInfo webToonInfo;
	private int titleColor, statusColor;

	private BaseApiImpl serviceApi;
	private List<String> readIdxs;

	@Inject
	BriteDatabase db;

	private Subscription subscriptions;

	private Episode firstItem;

	private final String LOGIN_DIALOG_TAG = "LoginDialogFragment";

	private boolean isEdit = false;
	private boolean isFavorite = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_episode);
		ButterKnife.bind(this);
		AppController.objectGraph(this).inject(this);

		setSupportActionBar(toolbar);

		getApi();

		swipeRefreshWidget.setColorSchemeResources(
			R.color.color1,
			R.color.color2,
			R.color.color3,
			R.color.color4);
		swipeRefreshWidget.setOnRefreshListener(this);

		loadDlg = new ProgressDialog(this);
		loadDlg.setCancelable(false);
		loadDlg.setMessage("Loading ...");
		adapter = new ListAdapter(this) {
			@Override
			public ViewHolder onCreateViewHolder(ViewGroup viewGroup,
												 int position) {
				final ViewHolder vh = super.onCreateViewHolder(viewGroup, position);
				vh.thumbnailView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Episode item = adapter.getItem(vh.getAdapterPosition());
						boolean isCheck = serviceApi.isLoginDataExist(getBaseContext());
						if (!isCheck) {
							if (item.isLocked()) {
								Toast.makeText(EpisodesActivity.this,
											   R.string.login_from_service_access,
											   Toast.LENGTH_SHORT).show();
								return;
							} else if (item.isAdult()) {
								Toast.makeText(EpisodesActivity.this,
											   R.string.login_from_adult_service_access,
											   Toast.LENGTH_SHORT).show();
								return;
							}
						}
						moveDetailPage(item);
					}
				});
				return vh;
			}
		};

		adapter.setIsLogin(serviceApi.isLoginDataExist(this));

		int columnCount = getResources().getInteger(R.integer.episode_column_count);
		manager = new GridLayoutManager(this, columnCount);
		recyclerView.setLayoutManager(manager);
		recyclerView.setAdapter(adapter);
		recyclerView.setOnScrollListener(scrollListener);

		webToonInfo = getIntent().getParcelableExtra(Const.EXTRA_EPISODE);
		initSupportActionBar();
		initView();
	}

	private void initSupportActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(webToonInfo.getTitle());
		actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_36dp);
	}

	private void getApi() {
		Intent intent = getIntent();
		serviceApi = Const.getServiceApi(intent);
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

	private void loading(String url) {
		new AsyncTask<Object, Void, List<Episode>>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loadDlg.show();

				if (swipeRefreshWidget.isRefreshing()) {
					swipeRefreshWidget.setRefreshing(false);
				}
			}

			@Override
			protected List<Episode> doInBackground(Object... params) {
				Log.i(TAG, "Load Episode=" + params[1].toString());
				WebToon webToon = serviceApi.parseEpisode(EpisodesActivity.this,
														  (WebToonInfo) params[0],
														  (String) params[1]);
				List<Episode> list = webToon.getEpisodes();
				nextLink = webToon.moreLink();
				if (!TextUtils.isEmpty(nextLink)) {
					scrollListener.setLoadingMore(false);
				}

				// Set Readed
				if (readIdxs != null && !readIdxs.isEmpty() && list != null) {
					for (Episode items : list) {
						items.setIsReaded(readIdxs.contains(items.getEpisodeId()));
					}
				}

				return list;
			}

			@Override
			protected void onPostExecute(List<Episode> list) {
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
		}.execute(webToonInfo, url);
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
			loading(nextLink);
			nextLink = null;
		}
	}

	@Override
	public void onRefresh() {
		adapter.clear();
		serviceApi.refreshEpisode();
		loading(webToonInfo.getUrl());
	}

	public static class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
		private final Context mContext;
		private final LayoutInflater mInflater;
		private final List<Episode> list;
		private boolean isLogin;

		public ListAdapter(Context context) {
			mContext = context;
			mInflater = LayoutInflater.from(context);
			list = new ArrayList<>();
		}

		public void addItems(List<Episode> list) {
			this.list.addAll(list);
		}

		public List<Episode> getList() {
			return list;
		}

		public Episode getItem(int position) {
			return list.get(position);
		}

		public void clear() {
			list.clear();
		}

		public void setIsLogin(boolean isLogin) {
			this.isLogin = isLogin;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
			View v = mInflater.inflate(R.layout.layout_episode_list_item, viewGroup, false);
			return new ViewHolder(v);
		}

		@Override
		public void onBindViewHolder(final ViewHolder viewHolder, int i) {
			Episode item = list.get(i);
			viewHolder.titleView.setText(item.getEpisodeTitle());
			Glide.with(mContext)
				 .load(item.getImage())
				 .centerCrop()
				 .error(R.drawable.abc_ic_clear_mtrl_alpha)
				 .listener(new RequestListener<String, GlideDrawable>() {
					 @Override
					 public boolean onException(Exception e, String model,
												Target<GlideDrawable> target,
												boolean isFirstResource) {
						 viewHolder.progress.setVisibility(View.GONE);
						 return false;
					 }

					 @Override
					 public boolean onResourceReady(GlideDrawable resource, String model,
													Target<GlideDrawable> target,
													boolean isFromMemoryCache,
													boolean isFirstResource) {
						 viewHolder.progress.setVisibility(View.GONE);
						 return false;
					 }
				 })
				 .into(viewHolder.thumbnailView);
			viewHolder.readView.setVisibility(item.isReaded() ? View.VISIBLE : View.GONE);

			if (item.isLocked() || item.isAdult()) {
				viewHolder.lockView.setImageResource(isLogin ? R.drawable.lock_open_circle : R.drawable.lock_circle);
			} else {
				viewHolder.lockView.setVisibility(View.GONE);
			}
		}

		@Override
		public int getItemCount() {
			return list != null ? list.size() : 0;
		}

		public static class ViewHolder extends RecyclerView.ViewHolder {
			@Bind(R.id.textView) public TextView titleView;
			@Bind(R.id.imageView) public ImageView thumbnailView;
			@Bind(R.id.readView) public View readView;
			@Bind(R.id.lockStatusView) public ImageView lockView;
			@Bind(R.id.progress) public View progress;

			public ViewHolder(View v) {
				super(v);
				ButterKnife.bind(this, v);
			}
		}
	}

	@OnClick(R.id.btnFirst)
	public void firstViewClick() {
		Episode item = adapter.getItem(0);
		boolean isCheck = serviceApi.isLoginDataExist(getBaseContext());
		if (!isCheck) {
			if (item.isLocked()) {
				Toast.makeText(this, R.string.login_from_service_access, Toast.LENGTH_SHORT).show();
				return;
			} else if (item.isAdult()) {
				Toast.makeText(this, R.string.login_from_adult_service_access, Toast.LENGTH_SHORT)
					 .show();
				return;
			}
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
		intent.putExtra(Const.EXTRA_API, serviceApi.getClass());
		intent.putExtra(Const.EXTRA_EPISODE, item);
		intent.putExtra(Const.EXTRA_MAIN_COLOR, titleColor);
		intent.putExtra(Const.EXTRA_STATUS_COLOR, statusColor);
		startActivity(intent);
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
	protected void onResume() {
		super.onResume();
		loadReaded();
		Glide.with(this).resumeRequests();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (subscriptions != null) {
			subscriptions.unsubscribe();
		}
		Glide.with(this).pauseRequests();
	}

	private void loadReaded() {
		if (TextUtils.isEmpty(webToonInfo.getWebtoonId())) {
			return;
		}

		if (subscriptions != null) {
			subscriptions.unsubscribe();
		}

		subscriptions = InjectDB.getEpisodeInfo(db,
												serviceApi.getClass().getSimpleName(),
												webToonInfo,
												onAction);
		InjectDB.getEpisodeFavorite(db,
									serviceApi.getClass().getSimpleName(),
									webToonInfo,
									onFavoriteAction);
	}

	private Action1<List<String>> onAction
		= new Action1<List<String>>() {
		@Override
		public void call(List<String> strings) {
			if (readIdxs != null && !readIdxs.isEmpty()) {
				readIdxs.clear();
			}
			readIdxs = strings;
			if (adapter.getItemCount() == 0) {
				loading(webToonInfo.getUrl());
			} else {
				if (readIdxs != null && !readIdxs.isEmpty()) {
					for (Episode item : adapter.getList()) {
						item.setIsReaded(readIdxs.contains(item.getEpisodeId()));
					}
				}

				adapter.notifyDataSetChanged();
			}
		}
	};

	private Action1<Boolean> onFavoriteAction
		= new Action1<Boolean>() {
		@Override
		public void call(Boolean aBoolean) {
			isFavorite = aBoolean;

			if (subscriptions != null) {
				subscriptions.unsubscribe();
			}
		}
	};

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
				if (subscriptions != null) {
					subscriptions.unsubscribe();
				}
				subscriptions = InjectDB.favoriteAdd(db,
													 serviceApi.getClass().getSimpleName(),
													 webToonInfo);
				setFavorite(true);
				break;
			case R.id.menu_item_favorite_delete:
				// 즐겨찾기 삭제
				InjectDB.favoriteDelete(db,
										serviceApi.getClass().getSimpleName(),
										webToonInfo);
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

	@Override
	public void finish() {
		if (isEdit) {
			Intent intent = new Intent();
			intent.putExtra(Const.EXTRA_EPISODE, webToonInfo);
			setResult(RESULT_OK, intent);
		}
		super.finish();
	}
}
