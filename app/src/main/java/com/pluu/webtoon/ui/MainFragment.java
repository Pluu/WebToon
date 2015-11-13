package com.pluu.webtoon.ui;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import com.pluu.event.OttoBusHolder;
import com.pluu.support.impl.AbstractWeekApi;
import com.pluu.support.impl.ServiceConst;
import com.pluu.support.impl.ServiceConst.NAV_ITEM;
import com.pluu.webtoon.R;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.event.ListUpdateEvent;
import com.pluu.webtoon.event.MainEpisodeLoadedEvent;
import com.pluu.webtoon.event.MainEpisodeStartEvent;
import com.squareup.otto.Subscribe;

/**
 * Main View Fragment
 * Created by PLUUSYSTEM-NEW on 2015-04-06.
 */
public class MainFragment extends Fragment {

	private final String TAG = MainFragment.class.getSimpleName();

	@Bind(R.id.slidingTabLayout)
	SlidingTabLayout slidingTabLayout;
	@Bind(R.id.viewPager)
	ViewPager viewPager;

	private AbstractWeekApi serviceApi;

	private boolean isFirstDlg = true;
	private ProgressDialog loadDlg;

	public static MainFragment newInstance(NAV_ITEM item) {
		MainFragment fragment = new MainFragment();
		Bundle args = new Bundle();
		args.putSerializable(Const.EXTRA_API, item);
		fragment.setArguments(args);
		return fragment;
	}

	public MainFragment() { }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		NAV_ITEM service = ServiceConst.getApiType(getArguments());
		serviceApi = AbstractWeekApi.getApi(service);

		if (getActivity() instanceof MainActivity) {
			MainActivity activity = (MainActivity) getActivity();
			activity.setSelfDrawerItem(service);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_toon, null);
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

		viewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {

			private SparseArray<WebtoonListFragment> views = new SparseArray<>();

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				super.destroyItem(container, position, object);
				views.remove(position);
			}

			@Override
			public Fragment getItem(int position) {
				WebtoonListFragment fragment = new WebtoonListFragment();
				Bundle args = new Bundle();
				args.putInt(Const.EXTRA_POS, position);
				args.putSerializable(Const.EXTRA_API, serviceApi.getNaviItem());
				fragment.setArguments(args);

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

	private void setServiceTheme(AbstractWeekApi serviceApi) {
		TypedValue value = new TypedValue();
		getActivity().getTheme().resolveAttribute(R.attr.colorPrimary, value, true);

		int titleColor = serviceApi.getTitleColor(getActivity());
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

		slidingTabLayout.setSelectedIndicatorColors(titleColor);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		OttoBusHolder.get().post(new ListUpdateEvent());
	}

	@Override
	public void onResume() {
		super.onResume();
		Glide.with(this).resumeRequests();
		OttoBusHolder.get().register(this);
	}

	@Override
	public void onPause() {
		OttoBusHolder.get().unregister(this);
		Glide.with(this).pauseRequests();
		super.onPause();
	}

	@Subscribe
	public void eventStartEvent(MainEpisodeStartEvent event) {
		if (isFirstDlg) {
			Log.d(TAG, "eventStartEvent");
			loadDlg.show();
			isFirstDlg = false;
		}
	}

	@Subscribe
	public void eventLoadedEvent(MainEpisodeLoadedEvent event) {
		if (!isFirstDlg) {
			Log.d(TAG, "eventLoadedEvent");
			loadDlg.dismiss();
		}
	}

}
