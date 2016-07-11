package com.pluu.webtoon.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pluu.webtoon.R;
import com.pluu.webtoon.item.WebToonInfo;
import com.pluu.webtoon.item.WebToonType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main Episode List Adapter
 * Created by PLUUSYSTEM-NEW on 2015-10-27.
 */
public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder> {
	private final Context mContext;
	private final LayoutInflater mInflater;
	private final List<WebToonInfo> list;

	private final int filterColor;

	public MainListAdapter(Context context, List<WebToonInfo> list) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		this.list = list;

		filterColor = ContextCompat.getColor(context, R.color.color_accent);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View v = mInflater.inflate(R.layout.layout_main_list_item, viewGroup, false);
		final ViewHolder vh = new ViewHolder(v);
		vh.favorite.setColorFilter(filterColor);
		return vh;
	}

	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, int i) {
		WebToonInfo item = list.get(i);
		viewHolder.titleView.setTag(item);
		viewHolder.titleView.setText(item.getTitle());
		Glide.with(mContext)
			 .load(item.getImage())
			 .centerCrop()
			 .error(R.drawable.ic_sentiment_very_dissatisfied_black_36dp)
			 .listener(new RequestListener<String, GlideDrawable>() {
				 @Override
				 public boolean onException(Exception e, String model,
											Target<GlideDrawable> target,
											boolean isFirstResource) {
					 viewHolder.progress.setVisibility(View.GONE);
					 return false;
				 }

				 @Override
				 public boolean onResourceReady(GlideDrawable resource, String model,
												Target<GlideDrawable> target,
												boolean isFromMemoryCache,
												boolean isFirstResource) {
					 viewHolder.progress.setVisibility(View.GONE);
					 return false;
				 }
			 })
			 .into(viewHolder.thumbnailView);

		viewHolder.regDate.setText(item.getUpdateDate());

		boolean isViewUpdate = !TextUtils.isEmpty(item.getUpdateDate());
		viewHolder.regDate.setVisibility(isViewUpdate ? View.VISIBLE : View.GONE);

		switch (item.getStatus()) {
			case UPDATE:
				viewHolder.tvUp.setVisibility(View.VISIBLE);
				viewHolder.tvRest.setVisibility(View.GONE);
				break;
			case BREAK:
				viewHolder.tvUp.setVisibility(View.GONE);
				viewHolder.tvRest.setVisibility(View.VISIBLE);
				break;
			default:
				viewHolder.tvUp.setVisibility(View.GONE);
				viewHolder.tvRest.setVisibility(View.GONE);
				break;
		}
		viewHolder.tvNovel.setVisibility(item.getType() == WebToonType.NOVEL ? View.VISIBLE : View.GONE);
		viewHolder.tv19.setVisibility(item.isAdult() ? View.VISIBLE : View.GONE);
		viewHolder.favorite.setVisibility(item.isFavorite() ? View.VISIBLE : View.GONE);
	}

	@Override
	public int getItemCount() {
		return list != null ? list.size() : 0;
	}

	public int modifyInfo(WebToonInfo info) {
		for (int i = 0, size = list.size(); i < size; i++) {
			WebToonInfo item = list.get(i);
			if (TextUtils.equals(info.getToonId(), item.getToonId())) {
				item.setIsFavorite(info.isFavorite());
				return i;
			}
		}
		return -1;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(android.R.id.text1) public TextView titleView;
		@BindView(R.id.imageview1) public ImageView thumbnailView;
		@BindView(R.id.regDate) public TextView regDate;
		@BindView(R.id.tvNovel) public TextView tvNovel;
		@BindView(R.id.tv19) public TextView tv19;
		@BindView(R.id.tvUp) public TextView tvUp;
		@BindView(R.id.tvRest) public TextView tvRest;
		@BindView(R.id.progress) public View progress;
		@BindView(R.id.favorite) public ImageView favorite;

		public ViewHolder(View v) {
			super(v);
			ButterKnife.bind(this, v);
		}
	}
}