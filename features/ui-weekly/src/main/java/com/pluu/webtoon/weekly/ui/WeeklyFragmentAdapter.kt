package com.pluu.webtoon.weekly.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pluu.webtoon.domain.usecase.WeeklyUseCase

/**
 * Main ViewPager Fragment Adapter
 * Created by pluu on 2017-05-02.
 */
class WeeklyFragmentAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val serviceApi: WeeklyUseCase
) : FragmentStateAdapter(fm, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return WebtoonListFragment.newInstance(position)
    }

    override fun getItemCount(): Int = serviceApi.weeklyTabSize
}
