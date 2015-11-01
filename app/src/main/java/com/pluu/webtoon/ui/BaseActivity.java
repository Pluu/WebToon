package com.pluu.webtoon.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.pluu.support.impl.ServiceConst;
import com.pluu.support.impl.ServiceConst.NAV_ITEM;
import com.pluu.webtoon.R;
import com.pluu.webtoon.common.Const;

/**
 * Base ActionBar Activity
 * Created by nohhs on 2015-04-06.
 */
public class BaseActivity extends AppCompatActivity {

	private final String TAG = BaseActivity.class.getSimpleName();

	// Primary toolbar and drawer toggle
	private Toolbar mActionBarToolbar;
	private ActionBarDrawerToggle mDrawerToggle;

	// Navigation drawer:
	private DrawerLayout mDrawerLayout;
	private ViewGroup mDrawerItemsListContainer;
	private Handler mHandler;

	// delay to launch nav drawer item, to allow close animation to play
	private static final int NAVDRAWER_LAUNCH_DELAY = 250;

	// list of navdrawer items that were actually added to the navdrawer, in order
	private ArrayList<NAV_ITEM> mNavDrawerItems = new ArrayList<>();

	// views that correspond to each navdrawer item, null if not yet created
	private View[] mNavDrawerItemViews = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mHandler = new Handler();

		ActionBar ab = getSupportActionBar();
		if (ab != null) {
			ab.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		setupNavDrawer();
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		getActionBarToolbar();
	}

	protected Toolbar getActionBarToolbar() {
		if (mActionBarToolbar == null) {
			mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
			if (mActionBarToolbar != null) {
				setSupportActionBar(mActionBarToolbar);
			}
		}
		return mActionBarToolbar;
	}

	protected NAV_ITEM getSelfNavDrawerItem() {
		return NAV_ITEM.INVALID;
	}

	private void setupNavDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (mDrawerLayout == null) {
			return;
		}

		mDrawerLayout.setStatusBarBackgroundColor(
			getResources().getColor(R.color.theme_primary_dark));

		if (mActionBarToolbar != null) {
//			mActionBarToolbar.setNavigationIcon(R.drawable.ic_drawer);
//			mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View view) {
//					mDrawerLayout.openDrawer(Gravity.START);
//				}
//			});

			mDrawerToggle = new ActionBarDrawerToggle(
				this,                    /* host Activity */
				mDrawerLayout,                    /* DrawerLayout object */
				mActionBarToolbar,            /* nav drawer image to replace 'Up' caret */
				R.string.app_name,  /* "open drawer" description for accessibility */
				R.string.app_name /* "close drawer" description for accessibility */
			);
			mDrawerToggle.setDrawerIndicatorEnabled(true);
			mDrawerLayout.setDrawerListener(mDrawerToggle);
			mDrawerToggle.syncState();
		}

		populateNavDrawer();
	}

	private void populateNavDrawer() {
		mNavDrawerItems.clear();

		mNavDrawerItems.add(NAV_ITEM.NAVER);
		mNavDrawerItems.add(NAV_ITEM.DAUM);
		mNavDrawerItems.add(NAV_ITEM.OLLEH);
		mNavDrawerItems.add(NAV_ITEM.KAKAOPAGE);
		mNavDrawerItems.add(NAV_ITEM.NATE);
		mNavDrawerItems.add(NAV_ITEM.T_STORE);

		createNavDrawerItems();
	}

	private void createNavDrawerItems() {
		mDrawerItemsListContainer = (ViewGroup) findViewById(R.id.navdrawer_items_list);
		if (mDrawerItemsListContainer == null) {
			return;
		}

		mNavDrawerItemViews = new View[mNavDrawerItems.size()];
		mDrawerItemsListContainer.removeAllViews();
		int i = 0;
		for (NAV_ITEM item : mNavDrawerItems) {
			mNavDrawerItemViews[i] = makeNavDrawerItem(item, mDrawerItemsListContainer);
			mDrawerItemsListContainer.addView(mNavDrawerItemViews[i]);
			++i;
		}

	}

	private View makeNavDrawerItem(final NAV_ITEM item, ViewGroup container) {
		boolean selected = getSelfNavDrawerItem() == item;
		int layoutToInflate;
		if (item == NAV_ITEM.SEPARATOR) {
			layoutToInflate = R.layout.navdrawer_separator;
		} else {
			layoutToInflate = R.layout.navdrawer_item;
		}
		View view = getLayoutInflater().inflate(layoutToInflate, container, false);

		if (isSeparator(item)) {
			// we are done
			return view;
		}

		ImageView iconView = (ImageView) view.findViewById(R.id.icon);
		TextView titleView = (TextView) view.findViewById(R.id.title);
		int idx = item.ordinal();
		int iconId = ServiceConst.NAVDRAWER_ICON_RES_ID[idx];
		int titleId = ServiceConst.NAVDRAWER_TITLE_RES_ID[idx];

		// set icon and text
		iconView.setVisibility(iconId > 0 ? View.VISIBLE : View.GONE);
		if (iconId > 0) {
			iconView.setImageResource(iconId);
		}
		titleView.setText(getString(titleId));

		formatNavDrawerItem(view, item, selected);

		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onNavDrawerItemClicked(item);
			}
		});

		return view;
	}

	private void formatNavDrawerItem(View view, NAV_ITEM item, boolean selected) {
		if (isSeparator(item)) {
			// not applicable
			return;
		}

		ImageView iconView = (ImageView) view.findViewById(R.id.icon);
		TextView titleView = (TextView) view.findViewById(R.id.title);

		if (selected) {
			view.setBackgroundResource(R.drawable.selected_navdrawer_item_background);
		} else {
			view.setBackgroundResource(R.drawable.selector_nav_item);
		}

		// configure its appearance according to whether or not it's selected
		titleView.setTextColor(selected ?
								   getNaviSelectedTextColor() :
								   getResources().getColor(R.color.navdrawer_text_color));
		iconView.setColorFilter(selected ?
									getNaviSelectedIconColorFilter() :
									getResources().getColor(R.color.navdrawer_icon_tint));
	}

	/**
	 * Navi Drawer Title Color
	 * @return color
	 */
	protected int getNaviSelectedTextColor() {
		return getResources().getColor(R.color.navdrawer_text_color_selected);
	}

	/**
	 * Navi Drawer Icon Color Filter
	 * @return color
	 */
	protected int getNaviSelectedIconColorFilter() {
		return getResources().getColor(R.color.navdrawer_icon_tint_selected);
	}

	private boolean isSpecialItem(NAV_ITEM item) {
		switch (item) {
			default:
				return false;
		}
	}

	private boolean isSeparator(NAV_ITEM item) {
		return item == NAV_ITEM.SEPARATOR;
	}

	protected boolean isNavDrawerOpen() {
		return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
	}

	protected void closeNavDrawer() {
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(GravityCompat.START);
		}
	}

	private void onNavDrawerItemClicked(final NAV_ITEM item) {
		if (item == getSelfNavDrawerItem()) {
			mDrawerLayout.closeDrawer(GravityCompat.START);
			return;
		}

		if (isSpecialItem(item)) {
			goToNavDrawerItem(item);
		} else {
			// launch the target Activity after a short delay, to allow the close animation to play
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					goToNavDrawerItem(item);
				}
			}, NAVDRAWER_LAUNCH_DELAY);

			// change the active item on the list so the user can see the item changed
			setSelectedNavDrawerItem(item);
		}

		mDrawerLayout.closeDrawer(GravityCompat.START);
	}

	private void goToNavDrawerItem(NAV_ITEM item) {
		if (item != null) {
			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			Fragment tag = manager.findFragmentByTag(Const.MAIN_FRAG_TAG);
			transaction.remove(tag);
			transaction.replace(R.id.container,
								MainFragment.newInstance(item), Const.MAIN_FRAG_TAG);
			transaction.commit();
		}
	}

	/**
	 * Sets up the given navdrawer item's appearance to the selected state. Note: this could
	 * also be accomplished (perhaps more cleanly) with state-based layouts.
	 * @param item Select Navi Item
	 */
	private void setSelectedNavDrawerItem(NAV_ITEM item) {
		if (mNavDrawerItemViews != null) {
			for (int i = 0; i < mNavDrawerItemViews.length; i++) {
				if (i < mNavDrawerItems.size()) {
					NAV_ITEM thisItem = mNavDrawerItems.get(i);
					formatNavDrawerItem(mNavDrawerItemViews[i], thisItem, item == thisItem);
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (isNavDrawerOpen()) {
			closeNavDrawer();
		} else {
			super.onBackPressed();
		}
	}

}
