package com.pluu.webtoon.ui;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pluu.event.RxBusProvider;
import com.pluu.support.impl.NAV_ITEM;
import com.pluu.webtoon.AppController;
import com.pluu.webtoon.R;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.db.RealmHelper;
import com.pluu.webtoon.event.FirstItemSelectEvent;
import com.pluu.webtoon.item.WebToonInfo;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pluu.webtoon.utils.DisplayUtilsKt.animatorToolbarColor;
import static com.pluu.webtoon.utils.DisplayUtilsKt.setStatusBarColor;

/**
 * 에피소드 리스트 Activity
 * Created by nohhs on 2015-04-06.
 */
public class EpisodesActivity extends AppCompatActivity {

	private final String TAG = EpisodesActivity.class.getSimpleName();

	@BindView(R.id.toolbar_actionbar) Toolbar toolbar;
	@BindView(R.id.btnFirst) Button btnFirst;
	@BindView(R.id.tvName) TextView tvName;
	@BindView(R.id.tvRate) TextView tvRate;
    @Inject RealmHelper realmHelper;

	private View childTitle;

	private WebToonInfo webToonInfo;
	private int titleColor, statusColor;

	private boolean isEdit = false;
	private boolean isFavorite = false;
	private NAV_ITEM service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_episode);
		ButterKnife.bind(this);
        ((AppController) getApplicationContext()).getRealmHelperComponent().inject(this);

		setSupportActionBar(toolbar);

		webToonInfo = getIntent().getParcelableExtra(Const.EXTRA_EPISODE);
		isFavorite = webToonInfo.isFavorite();

		initSupportActionBar();
		getApi();
		initView();
		initFragment();
	}

	private void initSupportActionBar() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setTitle(webToonInfo.getTitle());
		}
	}

	private void getApi() {
		Intent intent = getIntent();
		service = (NAV_ITEM) intent.getSerializableExtra(Const.EXTRA_API);
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

		ValueAnimator.AnimatorUpdateListener listener = animation -> {
            Integer value = (Integer) animation.getAnimatedValue();
            toolbar.setBackgroundColor(value);
            btnFirst.setBackgroundColor(value);
            if (childTitle != null) {
                childTitle.setBackgroundColor(value);
            }

            tvName.setTextColor(value);
            tvRate.setTextColor(value);

            setStatusBarColor(EpisodesActivity.this, value);
        };

		ValueAnimator animator = animatorToolbarColor(this, titleColor, listener);
		animator.setDuration(1000L);
		animator.start();

		tvName.setText(webToonInfo.getWriter());
		if (!TextUtils.isEmpty(webToonInfo.getRate())) {
			tvRate.setText(webToonInfo.getRate());
			tvRate.setVisibility(View.VISIBLE);
		}
	}

	private void initFragment() {
		EpisodeFragment fragment
			= EpisodeFragment.getInstance(service, webToonInfo,
										  new int[]{titleColor, statusColor});

		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.container, fragment, Const.MAIN_FRAG_TAG)
			.commit();
	}

	@OnClick(R.id.btnFirst)
	public void firstViewClick() {
		RxBusProvider.getInstance().send(new FirstItemSelectEvent());
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
                realmHelper.addFavorite(service, webToonInfo.getToonId());
				setFavorite(true);
				break;
			case R.id.menu_item_favorite_delete:
				// 즐겨찾기 삭제
                realmHelper.removeFavorite(service, webToonInfo.getToonId());
				setFavorite(false);
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void setFavorite(boolean isFavorite) {
		isEdit = true;
		this.isFavorite = isFavorite;
		webToonInfo.setFavorite(isFavorite);

		Toast.makeText(this,
					   isFavorite ? R.string.favorite_add : R.string.favorite_delete,
					   Toast.LENGTH_SHORT).show();
	}

}
