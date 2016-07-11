package com.pluu.webtoon.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.pluu.support.impl.AbstractWeekApi;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.ui.WebtoonListFragment;

/**
 * Main ViewPager Fragment Adapter
 * Created by PLUUSYSTEM-NEW on 2016-03-21.
 */
public class MainFragmentAdapter extends FragmentStatePagerAdapter {

    private final SparseArray<WebtoonListFragment> views = new SparseArray<>();
    private final AbstractWeekApi serviceApi;

    public MainFragmentAdapter(FragmentManager fm, AbstractWeekApi serviceApi) {
        super(fm);
        this.serviceApi = serviceApi;
    }

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

}
