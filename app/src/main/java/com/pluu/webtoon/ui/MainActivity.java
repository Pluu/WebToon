package com.pluu.webtoon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.pluu.event.OttoBusHolder;
import com.pluu.support.impl.ServiceConst.NAV_ITEM;
import com.pluu.webtoon.R;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.event.ListUpdateEvent;

public class MainActivity extends BaseActivity {

	private NAV_ITEM selfDrawerItem;

	@Bind(R.id.toolbar_actionbar)
	Toolbar toolbar;

	@Override
	protected NAV_ITEM getSelfNavDrawerItem() {
		return selfDrawerItem;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
		}

		selfDrawerItem = NAV_ITEM.getDefault();

		getSupportFragmentManager()
			.beginTransaction()
			.add(R.id.container, new MainFragment(), Const.MAIN_FRAG_TAG)
			.commit();
	}

	public void setSelfDrawerItem(NAV_ITEM selfDrawerItem) {
		this.selfDrawerItem = selfDrawerItem;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}

		((OttoBusHolder) OttoBusHolder.get()).postQueue(new ListUpdateEvent());

//		Fragment fragment = getSupportFragmentManager().findFragmentByTag(Const.MAIN_FRAG_TAG);
//		if (fragment != null) {
//			fragment.onActivityResult(requestCode, resultCode, data);
//		}
	}
}
