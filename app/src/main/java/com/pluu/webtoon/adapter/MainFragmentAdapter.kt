package com.pluu.webtoon.adapter

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.support.impl.AbstractWeekApi
import com.pluu.webtoon.ui.weekly.WebtoonListFragment

/**
 * Main ViewPager Fragment Adapter
 * Created by pluu on 2017-05-02.
 */
class MainFragmentAdapter(
    fm: FragmentManager,
    private val serviceApi: AbstractWeekApi
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val views = SparseArray<WebtoonListFragment>()

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        views.remove(position)
    }

    override fun getItem(position: Int): Fragment {
        val fragment = WebtoonListFragment()
        fragment.arguments = Bundle().apply {
            putInt(Const.EXTRA_POS, position)
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
