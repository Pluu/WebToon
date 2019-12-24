package com.pluu.webtoon.adapter

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.domain.base.AbstractWeekApi
import com.pluu.webtoon.ui.weekly.WebtoonListFragment

/**
 * Main ViewPager Fragment Adapter
 * Created by pluu on 2017-05-02.
 */
class MainFragmentAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val serviceApi: AbstractWeekApi
) : FragmentStateAdapter(fm, lifecycle) {

    private val views = SparseArray<WebtoonListFragment>()

    override fun createFragment(position: Int): Fragment {
        val fragment = WebtoonListFragment()
        fragment.arguments = Bundle().apply {
            putInt(Const.EXTRA_POS, position)
        }
        views.put(position, fragment)
        return fragment
    }

    override fun getItemCount(): Int = serviceApi.weeklyTabSize

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        for (i in 0 until views.size()) {
            views.valueAt(i).onActivityResult(requestCode, resultCode, data)
        }
    }
}
