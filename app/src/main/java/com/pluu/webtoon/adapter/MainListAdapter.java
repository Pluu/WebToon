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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pluu.webtoon.R;
import com.pluu.webtoon.item.WebToonInfo;
import com.pluu.webtoon.item.WebToonType;

/**
 * Main Episode List Adapter
 * Created by PLUUSYSTEM-NEW on 2015-10-27.
 */
public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder> {
	private final Context mContext;
	private final LayoutInflater mInflater;
	private final List<WebToonInfo> list;

	private final int filterColor;
	private WebToonInfo selectInfo;

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
			 .error(R.drawable.abc_ic_clear_mtrl_alpha)
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

		if (selectInfo != null &&
			selectInfo.getWebtoonId().equals(item.getWebtoonId())) {
			viewHolder.favorite.setVisibility(selectInfo.isFavorite() ? View.VISIBLE : View.GONE);
		}
	}

	@Override
	public int getItemCount() {
		return list != null ? list.size() : 0;
	}

	public void setSelectInfo(WebToonInfo info) {
		this.selectInfo = info;
	}

	public void modifyInfo(WebToonInfo info) {
		for (WebToonInfo item : list) {
			if (TextUtils.equals(info.getWebtoonId(), item.getWebtoonId())) {
				item.setIsFavorite(info.isFavorite());
				break;
			}
		}
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		@Bind(android.R.id.text1) public TextView titleView;
		@Bind(R.id.imageview1) public ImageView thumbnailView;
		@Bind(R.id.regDate) public TextView regDate;
		@Bind(R.id.tvNovel) public TextView tvNovel;
		@Bind(R.id.tv19) public TextView tv19;
		@Bind(R.id.tvUp) public TextView tvUp;
		@Bind(R.id.tvRest) public TextView tvRest;
		@Bind(R.id.progress) public View progress;
		@Bind(R.id.favorite) public ImageView favorite;

		public ViewHolder(View v) {
			super(v);
			ButterKnife.bind(this, v);
		}
	}
}