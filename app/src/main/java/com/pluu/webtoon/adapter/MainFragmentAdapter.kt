package com.pluu.webtoon.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
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

    override fun createFragment(position: Int): Fragment {
        return WebtoonListFragment.newInstance(position)
    }

    override fun getItemCount(): Int = serviceApi.weeklyTabSize
}
