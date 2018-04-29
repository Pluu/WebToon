package com.pluu.webtoon.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pluu.webtoon.R
import com.pluu.webtoon.item.DetailView
import kotlinx.android.synthetic.main.fragment_episode.*

/**
 * Default Detail Fragment
 * Created by pluu on 2017-05-06.
 */
@SuppressLint("ValidFragment")
class DefaultDetailFragment(
    private val bottomHeight: Int
) : BaseDetailFragment() {

    private var actionBarHeight: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_default_detail, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        firstBind()
    }

    private fun init() {
        val t = TypedValue()
        activity?.theme?.resolveAttribute(android.R.attr.actionBarSize, t, true)
        actionBarHeight = resources.getDimensionPixelSize(t.resourceId)
    }

    override fun loadView(list: List<DetailView>) {
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context).apply {
                isItemPrefetchEnabled = true
                initialPrefetchItemCount = 5
            }
            adapter = DetailAdapter(list, listener)
            addItemDecoration(DetailItemDecoration(actionBarHeight, bottomHeight))
        }
    }
}
