package com.pluu.webtoon.utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * RecyclerView 하단 More Load 기능 관련 Listener
 * Created by nohhs on 2015-03-03.
 */
public abstract class MoreRefreshListener extends RecyclerView.OnScrollListener {

	private final String TAG = MoreRefreshListener.class.getSimpleName();
	private LAYOUT_MANAGER_TYPE layoutManagerType;
	private int[] lastPositions;

	protected boolean isLoadingMore;

	@Override
	public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
		super.onScrollStateChanged(recyclerView, newState);

		if (isLoadingMore) {
			return;
		}

		RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
		if (layoutManagerType == null) {
			parseLayoutManager(layoutManager);
		}

		int visibleItemCount = layoutManager.getChildCount();
		int totalItemCount = layoutManager.getItemCount();
		int lastVisibleItemPosition = getLastVisibleItemPosition(layoutManager);

		if ((totalItemCount - lastVisibleItemPosition) == 1
			&& totalItemCount >= visibleItemCount) {
			isLoadingMore = true;
			onMoreRefresh();
		}
	}

	private int getLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
		int lastVisibleItemPosition = -1;
		switch (layoutManagerType) {
			case LINEAR:
				lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
				break;
			case GRID:
				lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
				break;
			case STAGGERED_GRID:
				StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
				if (lastPositions == null) {
					lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
				}

				staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
				lastVisibleItemPosition = findMax(lastPositions);
				break;
		}
		return lastVisibleItemPosition;
	}

	private void parseLayoutManager(RecyclerView.LayoutManager manager) {
		if (manager instanceof LinearLayoutManager) {
			layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
		} else if (manager instanceof GridLayoutManager) {
			layoutManagerType = LAYOUT_MANAGER_TYPE.GRID;
		} else if (manager instanceof StaggeredGridLayoutManager) {
			layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
		} else {
			throw new RuntimeException("Unsupported LayoutManager used");
		}
	}

	private int findMax(int[] lastPositions) {
		int max = Integer.MIN_VALUE;
		for (int value : lastPositions) {
			if (value > max)
				max = value;
		}
		return max;
	}

	public boolean isLoadingMore() {
		return isLoadingMore;
	}

	/**
	 * Enable/Disable the More event
	 * @param isLoadingMore true/false
	 */
	public void setLoadingMore(boolean isLoadingMore) {
		this.isLoadingMore = isLoadingMore;
	}

	public abstract void onMoreRefresh();

	public enum LAYOUT_MANAGER_TYPE {
		LINEAR,
		GRID,
		STAGGERED_GRID
	}
}
