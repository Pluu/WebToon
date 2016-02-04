package com.pluu.webtoon.ui;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import com.pluu.event.OttoBusHolder;
import com.pluu.support.impl.AbstractWeekApi;
import com.pluu.support.impl.ServiceConst;
import com.pluu.support.impl.ServiceConst.NAV_ITEM;
import com.pluu.webtoon.R;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.event.MainEpisodeLoadedEvent;
import com.pluu.webtoon.event.MainEpisodeStartEvent;
import com.pluu.webtoon.event.ThemeEvent;
import com.pluu.webtoon.utils.DisplayUtils;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;

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

		viewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {

			private final SparseArray<WebtoonListFragment> views = new SparseArray<>();

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
			ValueAnimator animator1
				= DisplayUtils.animatorToolbarColor((AppCompatActivity) activity, color);

			ValueAnimator animator2 = DisplayUtils
				.animatorStatusBarColor(activity,
										colorDark);

			AnimatorSet set = new AnimatorSet();
			set.playTogether(animator1, animator2);
			set.setDuration(250L);
			set.start();
		}

		((OttoBusHolder) OttoBusHolder.get()).postQueue(new ThemeEvent(color, colorDark));
		slidingTabLayout.setSelectedIndicatorColors(color);
	}

	@Override
	public void onStart() {
		super.onStart();
		OttoBusHolder.get().register(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		Glide.with(this).resumeRequests();
	}

	@Override
	public void onPause() {
		Glide.with(this).pauseRequests();
		super.onPause();
	}

	@Override
	public void onStop() {
		OttoBusHolder.get().unregister(this);
		super.onStop();
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
