package com.pluu.webtoon.ui;

import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.pluu.event.RxBusProvider;
import com.pluu.support.impl.AbstractWeekApi;
import com.pluu.support.impl.NAV_ITEM;
import com.pluu.support.impl.ServiceConst;
import com.pluu.webtoon.AppController;
import com.pluu.webtoon.R;
import com.pluu.webtoon.adapter.MainListAdapter;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.db.RealmHelper;
import com.pluu.webtoon.event.MainEpisodeLoadedEvent;
import com.pluu.webtoon.event.MainEpisodeStartEvent;
import com.pluu.webtoon.item.WebToonInfo;
import com.pluu.webtoon.utils.GlideUtils;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Main EpisodePage List Fragment
 * Created by PLUUSYSTEM-NEW on 2015-10-27.
 */
public class WebtoonListFragment extends Fragment {
    private final String TAG = WebtoonListFragment.class.getSimpleName();

    @Inject
    RealmHelper realmHelper;

    private RecyclerView recyclerView;
    private GridLayoutManager manager;
    private int position;

    private final int REQUEST_DETAIL = 1000;
    public static final int REQUEST_DETAIL_REFERRER = 1001;

    private AbstractWeekApi serviceApi;
    private int columnCount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppController) getContext().getApplicationContext()).getRealmHelperComponent().inject(this);

        NAV_ITEM service = ServiceConst.getApiType(getArguments());
        serviceApi = AbstractWeekApi.getApi(getContext(), service);
        position = getArguments().getInt(Const.EXTRA_POS);
        columnCount = getResources().getInteger(R.integer.webtoon_column_count);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_webtoon_list, container, false);
        manager = new GridLayoutManager(getActivity(), columnCount);
        recyclerView.setLayoutManager(manager);

        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getApiRequest()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .map(getFavoriteProcessFunc())
            .map(unsortedList -> {
                // 정렬
                Collections.sort(unsortedList);
                return unsortedList;
            })
            .doOnSubscribe(disposable -> RxBusProvider.getInstance().send(new MainEpisodeStartEvent()))
            .doOnSuccess(items -> RxBusProvider.getInstance().send(new MainEpisodeLoadedEvent()))
            .subscribe(getRequestSubscriber(), throwable -> {
                RxBusProvider.getInstance().send(new MainEpisodeLoadedEvent());
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }

    //	@RxLogSubscriber
    @NonNull
    private Consumer<List<WebToonInfo>> getRequestSubscriber() {
        return list -> {
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
        };
    }

    @NonNull
    private Function<List<WebToonInfo>, List<WebToonInfo>> getFavoriteProcessFunc() {
        return list -> {
            for (WebToonInfo item : list) {
                item.setIsFavorite(
                    realmHelper.getFavoriteToon(serviceApi.getNaviItem(), item.getToonId())
                );
            }
            return list;
        };
    }

    //	@RxLogObservable
    private Single<List<WebToonInfo>> getApiRequest() {
        return Single.fromCallable(() -> serviceApi.parseMain(position));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DETAIL) {
            // 즐겨찾기 변경 처리 > 다른 ViewPager의 Fragment도 수신받기위해 Referrer
            Fragment frag = getFragmentManager().findFragmentByTag(Const.MAIN_FRAG_TAG);
            if (frag != null) {
                frag.onActivityResult(REQUEST_DETAIL_REFERRER, resultCode, data);
            }
        } else if (requestCode == REQUEST_DETAIL_REFERRER) {
            // ViewPager 로부터 전달받은 Referrer
            WebToonInfo info = data.getParcelableExtra(Const.EXTRA_EPISODE);
            favoriteUpdate(info);
        }
    }

    private void favoriteUpdate(WebToonInfo info) {
        MainListAdapter adapter = (MainListAdapter) recyclerView.getAdapter();
        int position = adapter.modifyInfo(info);
        if (position != -1) {
            adapter.notifyItemChanged(position);
        }
    }

    private void setClickListener(final MainListAdapter.ViewHolder vh) {
        View v = vh.itemView;
        v.setOnClickListener(v1 -> {
            final WebToonInfo item = (WebToonInfo) vh.titleView.getTag();
            if (item.isLock()) {
                Toast.makeText(getContext(),
                    R.string.msg_not_support,
                    Toast.LENGTH_SHORT).show();
            } else {
                loadPalette(vh.thumbnailView, item);
            }
        });
    }

    private void loadPalette(ImageView view, final WebToonInfo item) {
        Bitmap bitmap = GlideUtils.loadGlideBitmap(view);
        if (bitmap != null) {
            asyncPalette(item, bitmap);
        }
    }

    private void asyncPalette(final WebToonInfo item, Bitmap bitmap) {
        final Context context = getActivity();
        Palette.from(bitmap).generate(p -> {
            int bgColor = p.getDarkVibrantColor(
                Color.BLACK);
            int statusColor = p.getDarkMutedColor(
                ContextCompat.getColor(context, R.color.theme_primary_dark));
            moveEpisode(item, bgColor, statusColor);
        });
    }

    private void moveEpisode(WebToonInfo item, int bgColor, int statusColor) {
        Intent intent = new Intent(getActivity(), EpisodesActivity.class);
        intent.putExtra(Const.EXTRA_API, serviceApi.getNaviItem());
        intent.putExtra(Const.EXTRA_EPISODE, item);
        intent.putExtra(Const.EXTRA_MAIN_COLOR, bgColor);
        intent.putExtra(Const.EXTRA_STATUS_COLOR, statusColor);
        startActivityForResult(intent, REQUEST_DETAIL);
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