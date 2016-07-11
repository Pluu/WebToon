package com.pluu.webtoon.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pluu.event.RxBusProvider;
import com.pluu.support.impl.AbstractEpisodeApi;
import com.pluu.support.impl.ServiceConst;
import com.pluu.webtoon.R;
import com.pluu.webtoon.adapter.EpisodeAdapter;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.db.RealmHelper;
import com.pluu.webtoon.event.FirstItemSelectEvent;
import com.pluu.webtoon.item.Episode;
import com.pluu.webtoon.item.EpisodePage;
import com.pluu.webtoon.item.WebToonInfo;
import com.pluu.webtoon.model.REpisode;
import com.pluu.webtoon.utils.MoreRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 에피소드 리스트 Fragment
 * Created by nohhs on 2015-04-06.
 */
public class EpisodeFragment extends Fragment
	implements SwipeRefreshLayout.OnRefreshListener {

	private final String TAG = EpisodeFragment.class.getSimpleName();
	private final int REQUEST_DETAIL = 1000;

	@BindView(R.id.swipe_refresh_widget)
	SwipeRefreshLayout swipeRefreshWidget;
	@BindView(android.R.id.list)
	RecyclerView recyclerView;

	private GridLayoutManager manager;
	private EpisodeAdapter adapter;
	private ProgressDialog loadDlg;

	private WebToonInfo webToonInfo;
	private String nextLink;

	private ServiceConst.NAV_ITEM service;
	private AbstractEpisodeApi serviceApi;

	private int[] color;
	private CompositeSubscription mCompositeSubscription;
	private Unbinder bind;

	public EpisodeFragment() { }

	public static EpisodeFragment getInstance(ServiceConst.NAV_ITEM service,
									   WebToonInfo info,
									   int[] color) {
		EpisodeFragment frag = new EpisodeFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(Const.EXTRA_API, service);
		bundle.putParcelable(Const.EXTRA_EPISODE, info);
		bundle.putIntArray(Const.EXTRA_MAIN_COLOR, color);
		frag.setArguments(bundle);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_episode, container, false);
		bind = ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Bundle args = getArguments();
		service = (ServiceConst.NAV_ITEM) args.getSerializable(Const.EXTRA_API);
		serviceApi = AbstractEpisodeApi.getApi(service);
		webToonInfo = args.getParcelable(Const.EXTRA_EPISODE);
		color = args.getIntArray(Const.EXTRA_MAIN_COLOR);

		initView();
		loading();
	}

	private void initView() {
		swipeRefreshWidget.setColorSchemeResources(
			R.color.color1,
			R.color.color2,
			R.color.color3,
			R.color.color4);
		swipeRefreshWidget.setOnRefreshListener(this);

		loadDlg = new ProgressDialog(getContext());
		loadDlg.setCancelable(false);
		loadDlg.setMessage(getString(R.string.msg_loading));

		adapter = new EpisodeAdapter(getContext()) {
			@Override
			public ViewHolder onCreateViewHolder(ViewGroup viewGroup,
												 int position) {
				final ViewHolder vh = super.onCreateViewHolder(viewGroup, position);
				vh.thumbnailView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Episode item = adapter.getItem(vh.getAdapterPosition());
						if (item.isLock()) {
							Toast.makeText(getContext(),
										   R.string.msg_not_support,
										   Toast.LENGTH_SHORT).show();
						} else {
							moveDetailPage(item);
						}
					}
				});
				return vh;
			}
		};

		int columnCount = getResources().getInteger(R.integer.episode_column_count);
		manager = new GridLayoutManager(getContext(), columnCount);
		recyclerView.setLayoutManager(manager);
		recyclerView.setAdapter(adapter);
		recyclerView.setOnScrollListener(scrollListener);
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
	public void onResume() {
		super.onResume();
		Glide.with(this).resumeRequests();
		mCompositeSubscription = new CompositeSubscription();
		mCompositeSubscription.add(
				RxBusProvider.getInstance()
						.toObservable()
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(getBusEvent())
		);
	}

	@Override
	public void onPause() {
		super.onPause();
		Glide.with(this).pauseRequests();
		mCompositeSubscription.unsubscribe();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		bind.unbind();
	}

	@Override
	public void onRefresh() {
		adapter.clear();
		serviceApi.init();
		loading();
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

	private void loading() {
		if (swipeRefreshWidget.isRefreshing()) {
			swipeRefreshWidget.setRefreshing(false);
		}

		Observable.zip(getRequestApi(), getReadAction(), getRequestReadAction())
				.subscribeOn(Schedulers.newThread())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnSubscribe(getSubscribeAction())
				.doOnUnsubscribe(getUnsubscribeAction())
				.subscribe(getRequestSubscriber());
	}

	@NonNull
	private Action0 getUnsubscribeAction() {
		return new Action0() {
            @Override
            public void call() {
                loadDlg.dismiss();
            }
        };
	}

	@NonNull
	private Action0 getSubscribeAction() {
		return new Action0() {
            @Override
            public void call() {
                loadDlg.show();
            }
        };
	}

	//	@RxLogObservable
	private Observable<List<Episode>> getRequestApi() {
		return Observable
			.create(new Observable.OnSubscribe<List<Episode>>() {
				@Override
				public void call(Subscriber<? super List<Episode>> subscriber) {
					Log.i(TAG, "Load Episode=" + webToonInfo.getToonId());
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
	private Observable<List<REpisode>> getReadAction() {
		return Observable.create(new Observable.OnSubscribe<List<REpisode>>() {
			@Override
			public void call(Subscriber<? super List<REpisode>> subscriber) {
				RealmHelper helper = RealmHelper.getInstance();
				List<REpisode> list = helper.getEpisode(getContext(),
														service, webToonInfo.getToonId());
				subscriber.onNext(list);
				subscriber.onCompleted();
			}
		});
	}

	@NonNull
	private Func2<List<Episode>, List<REpisode>, List<Episode>> getRequestReadAction() {
		return new Func2<List<Episode>, List<REpisode>, List<Episode>>() {
			@Override
			public List<Episode> call(List<Episode> list, List<REpisode> readList) {
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

	@NonNull
	private Subscriber<List<Episode>> getRequestSubscriber() {
		return new Subscriber<List<Episode>>() {
			@Override
			public void onCompleted() { }

			@Override
			public void onError(Throwable e) { }

			@Override
			public void onNext(List<Episode> list) {
				if (list == null || list.isEmpty()) {
					if (list == null) {
						Toast.makeText(getContext(), R.string.network_fail,
									   Toast.LENGTH_SHORT).show();
						getActivity().finish();
					}
					return;
				}
				adapter.addItems(list);
				adapter.notifyDataSetChanged();
			}
		};
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		if (requestCode == REQUEST_DETAIL) {
			readUpdate();
		}
	}

	@NonNull
	private Action1<Object> getBusEvent() {
		return new Action1<Object>() {
			@Override
			public void call(Object o) {
				if (o instanceof FirstItemSelectEvent) {
					firstItemSelect();
				}
			}
		};
	}

	private void readUpdate() {
		getReadAction()
			.map(new Func1<List<REpisode>, List<String>>() {
				@Override
				public List<String> call(List<REpisode> list) {
					List<String> result = new ArrayList<>(list.size());
					for (REpisode item : list) {
						result.add(item.getEpisodeId());
					}
					return result;
				}
			})
			.subscribeOn(Schedulers.newThread())
			.observeOn(AndroidSchedulers.mainThread())
			.doOnSubscribe(getSubscribeAction())
			.doOnUnsubscribe(getUnsubscribeAction())
			.subscribe(new Subscriber<List<String>>() {
				@Override
				public void onCompleted() { }

				@Override
				public void onError(Throwable e) { }

				@Override
				public void onNext(List<String> list) {
					adapter.updateRead(list);
					adapter.notifyDataSetChanged();
				}
			});
	}

	private void firstItemSelect() {
		Episode item = adapter.getItem(0);
		if (item.isLock()) {
			Toast.makeText(getContext(), R.string.msg_not_support, Toast.LENGTH_SHORT).show();
			return;
		}

		Episode firstItem = serviceApi.getFirstEpisode(item);
		if (firstItem == null) {
			return;
		}
		firstItem.setTitle(this.webToonInfo.getTitle());
		moveDetailPage(firstItem);
	}

	private void moveDetailPage(Episode item) {
		Intent intent = new Intent(getContext(), DetailActivity.class);
		intent.putExtra(Const.EXTRA_API, service);
		intent.putExtra(Const.EXTRA_EPISODE, item);
		intent.putExtra(Const.EXTRA_MAIN_COLOR, color[0]);
		intent.putExtra(Const.EXTRA_STATUS_COLOR, color[1]);
		startActivityForResult(intent, REQUEST_DETAIL);
	}

}
