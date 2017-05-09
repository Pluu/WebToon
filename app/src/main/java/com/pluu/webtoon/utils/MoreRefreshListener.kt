package com.pluu.webtoon.utils

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

/**
 * RecyclerView 하단 More Load 기능 관련 Listener
 * Created by pluu on 2017-04-29.
 */
abstract class MoreRefreshListener : RecyclerView.OnScrollListener() {

    private var layoutManagerType: LAYOUT_MANAGER_TYPE? = null
    private var lastPositions: IntArray? = null

    var isLoadingMore: Boolean = false
        protected set

    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (isLoadingMore) {
            return
        }

        recyclerView?.layoutManager?.apply {
            layoutManagerType = layoutManagerType ?: parseLayoutManager(this)

            val visibleItemCount = childCount
            val totalItemCount = itemCount
            val lastVisibleItemPosition = getLastVisibleItemPosition(this)

            if (totalItemCount - lastVisibleItemPosition == 1 && totalItemCount >= visibleItemCount) {
                isLoadingMore = true
                onMoreRefresh()
            }
        }
    }

    private fun getLastVisibleItemPosition(layoutManager: RecyclerView.LayoutManager) =
            when (layoutManagerType) {
                LAYOUT_MANAGER_TYPE.LINEAR ->
                    (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                LAYOUT_MANAGER_TYPE.GRID ->
                    (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                LAYOUT_MANAGER_TYPE.STAGGERED_GRID -> {
                    val staggeredGridLayoutManager = layoutManager as StaggeredGridLayoutManager
                    lastPositions = lastPositions ?: IntArray(staggeredGridLayoutManager.spanCount)
                    staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions)
                    findMax(lastPositions!!)
                }
                else -> -1
            }

    private fun parseLayoutManager(manager: RecyclerView.LayoutManager) = when (manager) {
        is LinearLayoutManager -> LAYOUT_MANAGER_TYPE.LINEAR
        is GridLayoutManager -> LAYOUT_MANAGER_TYPE.GRID
        is StaggeredGridLayoutManager -> LAYOUT_MANAGER_TYPE.STAGGERED_GRID
        else -> throw RuntimeException("Unsupported LayoutManager used")
    }

    private fun findMax(lastPositions: IntArray) = lastPositions.max() ?: Integer.MIN_VALUE

    /**
     * Enable/Disable the More event
     */
    fun setLoadingMorePause() {
        this.isLoadingMore = false
    }

    abstract fun onMoreRefresh()

    enum class LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }
}
