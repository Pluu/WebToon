package com.pluu.webtoon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pluu.event.RxBusProvider;
import com.pluu.webtoon.R;
import com.pluu.webtoon.event.RecyclerViewEvent;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Main Episode List Adapter
 * Created by PLUUSYSTEM-NEW on 2015-10-27.
 */
public class LicenseAdapter extends RecyclerView.Adapter<LicenseAdapter.ViewHolder> {
	private final LayoutInflater mInflater;
	private final String[] list;

	public LicenseAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		list = context.getResources().getStringArray(R.array.license_title);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View v = mInflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, int i) {
		String item = list[i];
		viewHolder.title.setText(item);
	}

	@Override
	public int getItemCount() {
		return list != null ? list.length : 0;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder
		implements View.OnClickListener {
		@Bind(android.R.id.text1) public TextView title;

		public ViewHolder(View v) {
			super(v);
			ButterKnife.bind(this, v);
			v.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			RxBusProvider.getInstance().send(new RecyclerViewEvent(getAdapterPosition()));
		}
	}
}