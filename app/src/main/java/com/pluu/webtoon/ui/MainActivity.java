package com.pluu.webtoon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.pluu.event.OttoBusHolder;
import com.pluu.support.impl.ServiceConst.NAV_ITEM;
import com.pluu.webtoon.R;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.event.ListUpdateEvent;
import com.pluu.webtoon.item.WebToonInfo;

public class MainActivity extends BaseActivity {

	private NAV_ITEM selfDrawerItem;

	@Override
	protected NAV_ITEM getSelfNavDrawerItem() {
		return selfDrawerItem;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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

		WebToonInfo editItem = data.getParcelableExtra(Const.EXTRA_EPISODE);
		((OttoBusHolder) OttoBusHolder.get()).postQueue(new ListUpdateEvent(editItem));
	}
}
