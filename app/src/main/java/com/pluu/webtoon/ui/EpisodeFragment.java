package com.pluu.webtoon.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.pluu.support.impl.NAV_ITEM;
import com.pluu.webtoon.AppController;
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

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 에피소드 리스트 Fragment
 * Created by nohhs on 2015-04-06.
 */
public class EpisodeFragment extends Fragment
    implements SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = EpisodeFragment.class.getSimpleName();
    private final int REQUEST_DETAIL = 1000;

    @BindView(R.id.swipe_refresh_widget) SwipeRefreshLayout swipeRefreshWidget;
    @BindView(android.R.id.list) RecyclerView recyclerView;
    @Inject RealmHelper realmHelper;

    private GridLayoutManager manager;
    private EpisodeAdapter adapter;
    ProgressDialog loadDlg;

    WebToonInfo webToonInfo;
    private String nextLink;

    NAV_ITEM service;
    private AbstractEpisodeApi serviceApi;

    private int[] color;
    private CompositeDisposable mCompositeDisposable;
    private Unbinder bind;

    public EpisodeFragment() {
    }

    public static EpisodeFragment getInstance(NAV_ITEM service,
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppController) getContext().getApplicationContext()).getRealmHelperComponent().inject(this);
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
        service = (NAV_ITEM) args.getSerializable(Const.EXTRA_API);
        serviceApi = AbstractEpisodeApi.getApi(getContext(), service);
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
                vh.thumbnailView.setOnClickListener(v -> {
                    Episode item = adapter.getItem(vh.getAdapterPosition());
                    if (item.isLock()) {
                        Toast.makeText(getContext(),
                            R.string.msg_not_support,
                            Toast.LENGTH_SHORT).show();
                    } else {
                        moveDetailPage(item);
                    }
                });
                return vh;
            }
        };

        int columnCount = getResources().getInteger(R.integer.episode_column_count);
        manager = new GridLayoutManager(getContext(), columnCount);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(scrollListener);
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
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(
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
        mCompositeDisposable.dispose();
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

        Single.zip(getRequestApi(), getReadAction(), getRequestReadAction())
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(disposable -> loadDlg.show())
            .doOnSuccess(episodes -> loadDlg.dismiss())
            .doOnError(throwable -> Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show())
            .subscribe(getRequestSubscriber());
    }

    //	@RxLogObservable
    private Single<List<Episode>> getRequestApi() {
        return Single.defer(() -> {
            Log.i(TAG, "Load Episode=" + webToonInfo.getToonId());
            EpisodePage episodePage = serviceApi.parseEpisode(webToonInfo);
            List<Episode> list = episodePage.getEpisodes();
            nextLink = episodePage.moreLink();
            if (!TextUtils.isEmpty(nextLink)) {
                scrollListener.setLoadingMorePause();
            }
            return Single.just(list);
        });
    }

    @NonNull
    Single<List<REpisode>> getReadAction() {
        return Single.defer(() -> Single.just(realmHelper.getEpisode(service, webToonInfo.getToonId())));
    }

    @NonNull
    private BiFunction<List<Episode>, List<REpisode>, List<Episode>> getRequestReadAction() {
        return (list, readList) -> {
            for (REpisode readItem : readList) {
                for (Episode episode : list) {
                    if (TextUtils.equals(readItem.getEpisodeId(), episode.getEpisodeId())) {
                        episode.setReadFlag();
                        break;
                    }
                }
            }
            return list;
        };
    }

    @NonNull
    private Consumer<List<Episode>> getRequestSubscriber() {
        return episodes -> {
            if (episodes == null || episodes.isEmpty()) {
                if (episodes == null) {
                    Toast.makeText(getContext(), R.string.network_fail,
                        Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                return;
            }
            adapter.addItems(episodes);
            adapter.notifyDataSetChanged();
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
    private Consumer<Object> getBusEvent() {
        return o -> {
            if (o instanceof FirstItemSelectEvent) {
                firstItemSelect();
            }
        };
    }

    private void readUpdate() {
        getReadAction()
            .flatMapObservable(episodes -> Observable.fromIterable(episodes).map(REpisode::getEpisodeId))
            .toList()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(disposable -> loadDlg.show())
            .doOnSuccess(strings -> loadDlg.dismiss())
            .subscribe(new DisposableSingleObserver<List<String>>() {
                @Override
                public void onSuccess(List<String> strings) {
                    adapter.updateRead(strings);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable e) {

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
