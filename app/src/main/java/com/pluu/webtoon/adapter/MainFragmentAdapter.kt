package com.pluu.webtoon.adapter

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup

import com.pluu.support.impl.AbstractWeekApi
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.ui.WebtoonListFragment

/**
 * Main ViewPager Fragment Adapter
 * Created by pluu on 2017-05-02.
 */
class MainFragmentAdapter(fm: FragmentManager, private val serviceApi: AbstractWeekApi) : FragmentStatePagerAdapter(fm) {

    private val views = SparseArray<WebtoonListFragment>()

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        views.remove(position)
    }

    override fun getItem(position: Int): Fragment {
        val fragment = WebtoonListFragment()
        fragment.arguments = Bundle().apply {
            putInt(Const.EXTRA_POS, position)
            putSerializable(Const.EXTRA_API, serviceApi.naviItem)
        }
        views.put(position, fragment)
        return fragment
    }

    override fun getCount(): Int {
        return serviceApi.weeklyTabSize
    }

    override fun getPageTitle(position: Int): CharSequence {
        return serviceApi.getWeeklyTabName(position)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        for (i in 0 until views.size()) {
            views.valueAt(i).onActivityResult(requestCode, resultCode, data)
        }
    }

}
