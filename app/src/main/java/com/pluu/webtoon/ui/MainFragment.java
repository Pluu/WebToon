package com.pluu.webtoon.ui;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import com.pluu.event.RxBusProvider;
import com.pluu.support.impl.AbstractWeekApi;
import com.pluu.support.impl.ServiceConst;
import com.pluu.support.impl.ServiceConst.NAV_ITEM;
import com.pluu.webtoon.R;
import com.pluu.webtoon.adapter.MainFragmentAdapter;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.event.MainEpisodeLoadedEvent;
import com.pluu.webtoon.event.MainEpisodeStartEvent;
import com.pluu.webtoon.event.ThemeEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import static com.pluu.webtoon.utils.DisplayUtilsKt.animatorStatusBarColor;
import static com.pluu.webtoon.utils.DisplayUtilsKt.animatorToolbarColor;

/**
 * Main View Fragment
 * Created by PLUUSYSTEM-NEW on 2015-04-06.
 */
public class MainFragment extends Fragment {

    private final String TAG = MainFragment.class.getSimpleName();

    @BindView(R.id.slidingTabLayout) SlidingTabLayout slidingTabLayout;
    @BindView(R.id.viewPager) ViewPager viewPager;

    private CompositeDisposable mCompositeDisposable;

    private AbstractWeekApi serviceApi;

    private boolean isFirstDlg = true;
    private ProgressDialog loadDlg;
    private MainFragmentAdapter adapter;

    public static MainFragment newInstance(NAV_ITEM item) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putSerializable(Const.EXTRA_API, item);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NAV_ITEM service = ServiceConst.getApiType(getArguments());
        serviceApi = AbstractWeekApi.getApi(getContext(), service);

        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.setSelfDrawerItem(service);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toon, container, false);
        ButterKnife.bind(this, view);

        loadDlg = new ProgressDialog(getActivity());
        loadDlg.setCancelable(false);
        loadDlg.setMessage(getString(R.string.msg_loading));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getApi();

        adapter = new MainFragmentAdapter(getFragmentManager(), serviceApi);
        viewPager.setAdapter(adapter);
        // 금일 기준으로 ViewPager 기본 표시
        viewPager.setCurrentItem(serviceApi.getTodayTabPosition());

        slidingTabLayout.setCustomTabView(R.layout.view_sliding_tab,
            android.R.id.text1);
        slidingTabLayout.setViewPager(viewPager);
    }

    private void getApi() {
        // 선택한 서비스에 맞는 컬러 테마 변경
        setServiceTheme(serviceApi);
    }

    private void setServiceTheme(AbstractWeekApi serviceApi) {
        int color = serviceApi.getTitleColor(getContext());
        int colorDark = serviceApi.getTitleColorDark(getContext());
        FragmentActivity activity = getActivity();

        if (activity instanceof AppCompatActivity) {
            ValueAnimator animator1 = animatorToolbarColor((AppCompatActivity) activity, color);
            ValueAnimator animator2 = animatorStatusBarColor(activity, colorDark);

            AnimatorSet set = new AnimatorSet();
            set.playTogether(animator1, animator2);
            set.setDuration(250L);
            set.start();
        }

        RxBusProvider.getInstance().send(new ThemeEvent(color, colorDark));
        slidingTabLayout.setSelectedIndicatorColors(color);
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
        Glide.with(this).pauseRequests();
        mCompositeDisposable.dispose();
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == WebtoonListFragment.REQUEST_DETAIL_REFERRER) {
            // 포함되어있는 ViewPager 의 Fragment 갱신 처리
            adapter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @NonNull
    private Consumer<Object> getBusEvent() {
        return o -> {
            if (o instanceof MainEpisodeStartEvent) {
                eventStartEvent();
            } else if (o instanceof MainEpisodeLoadedEvent) {
                eventLoadedEvent();
            }
        };
    }

    private void eventStartEvent() {
        if (isFirstDlg) {
            Log.d(TAG, "eventStartEvent");
            loadDlg.show();
            isFirstDlg = false;
        }
    }

    private void eventLoadedEvent() {
        if (!isFirstDlg) {
            Log.d(TAG, "eventLoadedEvent");
            loadDlg.dismiss();
        }
    }

}
