package com.pluu.webtoon.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.pluu.event.OttoBusHolder;
import com.pluu.support.impl.AbstractWeekApi;
import com.pluu.support.impl.ServiceConst;
import com.pluu.webtoon.AppController;
import com.pluu.webtoon.R;
import com.pluu.webtoon.adapter.MainListAdapter;
import com.pluu.webtoon.item.WebToonInfo;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.db.InjectDB;
import com.pluu.webtoon.event.ListUpdateEvent;
import com.pluu.webtoon.event.MainEpisodeLoadedEvent;
import com.pluu.webtoon.event.MainEpisodeStartEvent;
import com.pluu.webtoon.event.WebToonSelectEvent;
import com.squareup.otto.Subscribe;
import com.squareup.sqlbrite.BriteDatabase;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Main EpisodePage List Fragment
 * Created by PLUUSYSTEM-NEW on 2015-10-27.
 */
public class WebtoonListFragment extends Fragment {
	private final String TAG = WebtoonListFragment.class.getSimpleName();

	private RecyclerView recyclerView;
	private GridLayoutManager manager;
	private int position;

	private static final int REQUEST_CODE = 1000;

	@Inject
	BriteDatabase db;

	private AbstractWeekApi serviceApi;
	private WebToonInfo selectInfo;
	private int columnCount;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ServiceConst.NAV_ITEM service = ServiceConst.getApiType(getArguments());
		serviceApi = AbstractWeekApi.getApi(service);
		position = getArguments().getInt(Const.EXTRA_POS);
		columnCount = getResources().getInteger(R.integer.webtoon_column_count);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_webtoon_list, null);
		manager = new GridLayoutManager(getActivity(), columnCount);
		recyclerView.setLayoutManager(manager);

		AppController.objectGraph(getActivity()).inject(this);
		return recyclerView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		OttoBusHolder.get().post(new MainEpisodeStartEvent());
		getApiRequest()
			.subscribeOn(Schedulers.newThread())
			.observeOn(AndroidSchedulers.mainThread())
			.map(getFavoriteProcessFunc())
			.subscribe(getRequestSubscriber());
	}

//	@RxLogSubscriber
	@NonNull
	private Subscriber<List<WebToonInfo>> getRequestSubscriber() {
		return new Subscriber<List<WebToonInfo>>() {
			@Override
			public void onCompleted() { }

			@Override
			public void onError(Throwable e) { }

			@Override
			public void onNext(List<WebToonInfo> list) {
				final FragmentActivity activity = getActivity();
				if (activity == null || activity.isFinishing()) {
					return;
				}

				recyclerView.setAdapter(new MainListAdapter(activity, list) {
					@Override
					public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
						ViewHolder vh = super.onCreateViewHolder(viewGroup, i);
						setClickListener(vh);
						return vh;
					}
				});

				OttoBusHolder.get().post(new MainEpisodeLoadedEvent());
			}
		};
	}

	@NonNull
	private Func1<List<WebToonInfo>, List<WebToonInfo>> getFavoriteProcessFunc() {
		return new Func1<List<WebToonInfo>, List<WebToonInfo>>() {
			@Override
			public List<WebToonInfo> call(List<WebToonInfo> list) {
				for (final WebToonInfo item : list) {
					InjectDB
						.getEpisodeFavorite(
							db, serviceApi.getNaviItem().name(), item,
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
		};
	}

	//	@RxLogObservable
	private Observable<List<WebToonInfo>> getApiRequest() {
		return Observable.create(new Observable.OnSubscribe<List<WebToonInfo>>() {
			@Override
			public void call(Subscriber<? super List<WebToonInfo>> subscriber) {
				Log.i(TAG, "Load pos=" + position);
				List<WebToonInfo> list = serviceApi.parseMain(position);
				subscriber.onNext(list);
				subscriber.onCompleted();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		OttoBusHolder.get().register(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		OttoBusHolder.get().unregister(this);
	}

	@Subscribe
	public void responseNetwork(WebToonSelectEvent result) {
		selectInfo = result.item;
		((MainListAdapter) recyclerView.getAdapter()).setSelectInfo(selectInfo);
	}

	@Subscribe
	public void favoriteUpdate(ListUpdateEvent event) {
		recyclerView.getAdapter().notifyDataSetChanged();
	}

	private void setClickListener(final MainListAdapter.ViewHolder vh) {
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
					 asyncPalette(item, resource);
				 }
			 });
	}

	private void asyncPalette(final WebToonInfo item, Bitmap bitmap) {
		final Context context = getActivity();
		Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
			public void onGenerated(Palette p) {
				int bgColor = p.getDarkVibrantColor(
					Color.BLACK);
				int statusColor = p.getDarkMutedColor(
					ContextCompat.getColor(context, R.color.theme_primary_dark));
				moveEpisode(item, bgColor, statusColor);
			}
		});
	}

	private void moveEpisode(WebToonInfo item, int bgColor, int statusColor) {
		Intent intent = new Intent(getActivity(), EpisodesActivity.class);
		intent.putExtra(Const.EXTRA_API, serviceApi.getNaviItem());
		intent.putExtra(Const.EXTRA_EPISODE, item);
		intent.putExtra(Const.EXTRA_MAIN_COLOR, bgColor);
		intent.putExtra(Const.EXTRA_STATUS_COLOR, statusColor);
		getActivity().startActivityForResult(intent, REQUEST_CODE);
	}

	private void updateSpanCount() {
		int columnCount = getResources().getInteger(R.integer.webtoon_column_count);
		manager.setSpanCount(columnCount);
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