package com.pluu.webtoon.ui;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import com.pluu.support.ApiImpl;
import com.pluu.support.BaseApiImpl;
import com.pluu.support.naver.NaverApi;
import com.pluu.webtoon.AppController;
import com.pluu.webtoon.R;
import com.pluu.webtoon.api.WebToonInfo;
import com.pluu.webtoon.api.WebToonType;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.db.InjectDB;
import com.squareup.sqlbrite.BriteDatabase;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by PLUUSYSTEM-NEW on 2015-04-06.
 */
public class MainFragment extends Fragment {

	private static final int REQUEST_CODE = 1000;

	private final String TAG = MainFragment.class.getSimpleName();
	private static final String RECYCLER_TAG = "MAIN_RECYCLER_VIEW";

	private static final String ARG_API = "api";

	@Bind(R.id.slidingTabLayout)
	SlidingTabLayout slidingTabLayout;
	@Bind(R.id.viewPager)
	ViewPager viewPager;

	@Inject
	BriteDatabase db;

	private ProgressDialog loadDlg;
	private boolean isFirstDlg = true;
	private ApiImpl serviceApi;

	private Subscription subscriptions;
	private static WebToonInfo selectInfo;

	public static MainFragment newInstance(Class<? extends BaseApiImpl> target) {
		MainFragment fragment = new MainFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_API, target);
		fragment.setArguments(args);
		return fragment;
	}

	public MainFragment() { }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			serviceApi = Const.getServiceApi(
				(Class<BaseApiImpl>) getArguments().getSerializable(ARG_API));
		} else {
			serviceApi = Const.getServiceApi(NaverApi.class);
		}

		if (getActivity() instanceof MainActivity) {
			MainActivity activity = (MainActivity) getActivity();
			activity.setSelfDrawerItem(serviceApi.getNaviItem());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_toon, null);
		ButterKnife.bind(this, view);
		AppController.objectGraph(getActivity()).inject(this);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		getApi();
		loadDlg = new ProgressDialog(getActivity());
		loadDlg.setCancelable(false);
		loadDlg.setMessage(getString(R.string.msg_loading));

		viewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {

			private SparseArray<WebtoonListFragment> views = new SparseArray<>();

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				super.destroyItem(container, position, object);
				views.remove(position);
			}

			@Override
			public void notifyDataSetChanged() {
				int key;
				int size = views.size();
				for (int i = 0; i < size; i++) {
					key = views.keyAt(i);
					views.get(key).update();
				}
				super.notifyDataSetChanged();
			}

			@Override
			public Fragment getItem(int position) {
				WebtoonListFragment fragment = new WebtoonListFragment();
				Bundle arguments = new Bundle();
				String url = serviceApi.getWeeklyUrl(position);
				arguments.putString(WebtoonListFragment.ARGUMENT_URL, url);
				arguments.putInt(WebtoonListFragment.ARGUMENT_POS, position);
				fragment.setArguments(arguments);

				views.put(position, fragment);

				return fragment;
			}

			@Override
			public int getCount() {
				return serviceApi.getWeeklyTabSize();
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return serviceApi.getWeeklyTabName(position);
			}
		});

		// 금일 기준으로 ViewPager 기본 표시
		viewPager.setCurrentItem(serviceApi.getTodayTabPosition());

		slidingTabLayout.setDistributeEvenly(true);
		slidingTabLayout.setViewPager(viewPager);
	}

	private void getApi() {
		// 선택한 서비스에 맞는 컬러 테마 변경
		setServiceTheme(serviceApi);
	}

	private void setServiceTheme(ApiImpl serviceApi) {
		TypedValue value = new TypedValue();
		getActivity().getTheme().resolveAttribute(R.attr.colorPrimary, value, true);

		int titleColor = serviceApi.getMainTitleColor(getActivity());
		ValueAnimator bg = ValueAnimator.ofObject(new ArgbEvaluator(), value.data, titleColor);
		bg.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Integer value = (Integer) animation.getAnimatedValue();
				MainActivity activity = (MainActivity) getActivity();
				if (activity != null) {
					Toolbar toolbar = activity.getActionBarToolbar();
					if (toolbar != null) {
						toolbar.setBackgroundColor(value);
					}
				}
			}
		});
		bg.setDuration(2000L);
		bg.setInterpolator(new DecelerateInterpolator());
		bg.start();

		slidingTabLayout.setSelectedIndicatorColors(serviceApi.getMainTitleColor(getActivity()));
	}

	public static class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
		private final Context mContext;
		private final LayoutInflater mInflater;
		private final List<WebToonInfo> list;

		private final int filterColor;

		public ListAdapter(Context context, List<WebToonInfo> list) {
			mContext = context;
			mInflater = LayoutInflater.from(context);
			this.list = list;

			filterColor = context.getResources().getColor(R.color.color_accent);
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
			View v = mInflater.inflate(R.layout.layout_main_list_item, viewGroup, false);
			final ViewHolder vh = new ViewHolder(v);
			vh.favorite.setColorFilter(filterColor);
			return vh;
		}

		@Override
		public void onBindViewHolder(final ViewHolder viewHolder, int i) {
			WebToonInfo item = list.get(i);
			viewHolder.titleView.setTag(item);
			viewHolder.titleView.setText(item.getTitle());
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

			viewHolder.regDate.setText(item.getUpdateDate());

			boolean isViewUpdate = !TextUtils.isEmpty(item.getUpdateDate());
			viewHolder.regDate.setVisibility(isViewUpdate ? View.VISIBLE : View.GONE);

			switch (item.getStatus()) {
				case UPDATE:
					viewHolder.tvUp.setVisibility(View.VISIBLE);
					viewHolder.tvRest.setVisibility(View.GONE);
					break;
				case BREAK:
					viewHolder.tvUp.setVisibility(View.GONE);
					viewHolder.tvRest.setVisibility(View.VISIBLE);
					break;
				default:
					viewHolder.tvUp.setVisibility(View.GONE);
					viewHolder.tvRest.setVisibility(View.GONE);
					break;
			}
			viewHolder.tvNovel.setVisibility(item.getType() == WebToonType.NOVEL ? View.VISIBLE : View.GONE);
			viewHolder.tv19.setVisibility(item.isAdult() ? View.VISIBLE : View.GONE);
			viewHolder.favorite.setVisibility(item.isFavorite() ? View.VISIBLE : View.GONE);

			if (selectInfo != null &&
				selectInfo.getWebtoonId().equals(item.getWebtoonId())) {
				viewHolder.favorite.setVisibility(selectInfo.isFavorite() ? View.VISIBLE : View.GONE);
			}
		}

		@Override
		public int getItemCount() {
			return list != null ? list.size() : 0;
		}

		public static class ViewHolder extends RecyclerView.ViewHolder {
			@Bind(android.R.id.text1) public TextView titleView;
			@Bind(R.id.imageview1) public ImageView thumbnailView;
			@Bind(R.id.regDate) public TextView regDate;
			@Bind(R.id.tvNovel) public TextView tvNovel;
			@Bind(R.id.tv19) public TextView tv19;
			@Bind(R.id.tvUp) public TextView tvUp;
			@Bind(R.id.tvRest) public TextView tvRest;
			@Bind(R.id.progress) public View progress;
			@Bind(R.id.favorite) public ImageView favorite;

			public ViewHolder(View v) {
				super(v);
				ButterKnife.bind(this, v);
			}
		}
	}

	@SuppressLint("ValidFragment")
	private class WebtoonListFragment extends Fragment {
		public static final String ARGUMENT_URL = "url";
		public static final String ARGUMENT_POS = "pos";

		RecyclerView recyclerView;

		private GridLayoutManager manager;
		private int position;

		@Override
		public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
								 @Nullable Bundle savedInstanceState) {
			recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_webtoon_list, null);
			int columnCount = getResources().getInteger(R.integer.webtoon_column_count);
			manager = new GridLayoutManager(getActivity(), columnCount);

			position = getArguments().getInt(ARGUMENT_POS);

			recyclerView.setLayoutManager(manager);

			return recyclerView;
		}

		@Override
		public void onActivityCreated(@Nullable Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			new AsyncTask<Void, Void, List<WebToonInfo>>() {
				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					if (isFirstDlg) {
						loadDlg.show();
					}
				}

				@Override
				protected List<WebToonInfo> doInBackground(Void... params) {
					String url = getArguments().getString(ARGUMENT_URL);

					Log.i(TAG, "Load=" + url.toString() + ", pos=" + position);

					List<WebToonInfo> list = serviceApi.parseMain(getActivity(), url, position);

					for (final WebToonInfo item : list) {
						InjectDB
							.getEpisodeFavorite(db,
												serviceApi.getClass().getSimpleName(),
												item,
												new Action1<Boolean>() {
													@Override
													public void call(Boolean aBoolean) {
														item.setIsFavorite(aBoolean);
													}
												}
							);
					}

					return list;
				}

				@Override
				protected void onPostExecute(List<WebToonInfo> list) {
					recyclerView.setAdapter(new ListAdapter(getActivity(), list) {
						@Override
						public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
							ViewHolder vh = super.onCreateViewHolder(viewGroup, i);
							setClickListener(vh);
							return vh;
						}
					});

					if (isFirstDlg) {
						loadDlg.dismiss();
						isFirstDlg = false;
					}
				}
			}.execute();
		}

		private void setClickListener(final ListAdapter.ViewHolder vh) {
			View v = vh.itemView;
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					final WebToonInfo item = (WebToonInfo) vh.titleView.getTag();
					selectInfo = item;
					loadPalette(item);
				}
			});
		}

		private void loadPalette(final WebToonInfo item) {
			final Context context = getActivity();
			Glide.with(context)
				 .load(item.getImage())
				 .asBitmap()
				 .into(new SimpleTarget<Bitmap>() {
					 @Override
					 public void onResourceReady(Bitmap resource,
												 GlideAnimation<? super Bitmap> glideAnimation) {
						 Palette.generateAsync(resource,
											   new Palette.PaletteAsyncListener() {
												   @Override
												   public void onGenerated(Palette palette) {
													   int bgColor = palette.getDarkVibrantColor(
														   Color.BLACK);
													   int statusColor = palette.getDarkMutedColor(
														   context.getResources()
																  .getColor(
																	  R.color.theme_primary_dark));
													   moveEpisode(item, bgColor, statusColor);
												   }
											   });
					 }
				 });
		}

		private void moveEpisode(WebToonInfo item, int bgColor, int statusColor) {
			Intent intent = new Intent(getActivity(), EpisodesActivity.class);
			intent.putExtra(Const.EXTRA_API, serviceApi.getClass());
			intent.putExtra(Const.EXTRA_EPISODE, item);
			intent.putExtra(Const.EXTRA_MAIN_COLOR, bgColor);
			intent.putExtra(Const.EXTRA_STATUS_COLOR, statusColor);
			getActivity().startActivityForResult(intent, REQUEST_CODE);
		}

		public void updateSpanCount() {
			int columnCount = getResources().getInteger(R.integer.webtoon_column_count);
			manager.setSpanCount(columnCount);
			recyclerView.getAdapter().notifyDataSetChanged();
		}

		public void update() {
			recyclerView.getAdapter().notifyDataSetChanged();
		}

		@Override
		public void onConfigurationChanged(Configuration newConfig) {
			super.onConfigurationChanged(newConfig);

			if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
				|| newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
				updateSpanCount();
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		if (subscriptions != null) {
			subscriptions.unsubscribe();
		}

		selectInfo = data.getParcelableExtra(Const.EXTRA_EPISODE);

		subscriptions = InjectDB.getEpisodeFavorite(
			db,
			serviceApi.getClass().getSimpleName(),
			selectInfo,
			new Action1<Boolean>() {
				@Override
				public void call(Boolean aBoolean) {
					selectInfo.setIsFavorite(aBoolean);
					viewPager.getAdapter().notifyDataSetChanged();
				}
			});
	}

	@Override
	public void onResume() {
		super.onResume();
		Glide.with(this).resumeRequests();
	}

	@Override
	public void onPause() {
		super.onPause();
		Glide.with(this).pauseRequests();
	}
}
