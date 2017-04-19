package com.pluu.webtoon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.pluu.event.RxBusProvider;
import com.pluu.support.impl.ServiceConst.NAV_ITEM;
import com.pluu.webtoon.R;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.common.PrefConfig;
import com.pluu.webtoon.event.ThemeEvent;
import com.pluu.webtoon.ui.settting.SettingsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseNavActivity {

    private NAV_ITEM selfDrawerItem;

    @BindView(R.id.navTitle)
    View navTitle;

    private CompositeDisposable mCompositeDisposable;

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

        selfDrawerItem = PrefConfig.getDefaultWebToon(this);
        themeChange(new ThemeEvent(ContextCompat.getColor(this, selfDrawerItem.getColor()),
            ContextCompat.getColor(this, selfDrawerItem.getBgColor())));

        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.container, MainFragment.newInstance(selfDrawerItem), Const.MAIN_FRAG_TAG)
            .commit();
    }

    void setSelfDrawerItem(NAV_ITEM selfDrawerItem) {
        this.selfDrawerItem = selfDrawerItem;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(
            RxBusProvider.getInstance()
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getBusEvent())
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCompositeDisposable.dispose();
    }

    @NonNull
    private Consumer<Object> getBusEvent() {
        return o -> {
            if (o instanceof ThemeEvent) {
                themeChange((ThemeEvent) o);
            }
        };
    }

    private void themeChange(ThemeEvent event) {
        navTitle.setBackgroundColor(event.getDarkColor());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(Const.MAIN_FRAG_TAG);
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @OnClick(R.id.btnSetting)
    void clickSetting() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        closeNavDrawer();
    }

}
