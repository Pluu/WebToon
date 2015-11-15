package com.pluu.webtoon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pluu.webtoon.R;
import com.pluu.webtoon.item.Episode;

/**
 * 에피소드 화면 Adapter
 * Created by PLUUSYSTEM-NEW on 2015-11-14.
 */
public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {
	private final Context mContext;
	private final LayoutInflater mInflater;
	private final List<Episode> list;

	public EpisodeAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		list = new ArrayList<>();
	}

	public void addItems(List<Episode> list) {
		this.list.addAll(list);
	}

	public List<Episode> getList() {
		return list;
	}

	public Episode getItem(int position) {
		return list.get(position);
	}

	public void clear() {
		list.clear();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View v = mInflater.inflate(R.layout.layout_episode_list_item, viewGroup, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, int i) {
		Episode item = list.get(i);
		viewHolder.titleView.setText(item.getEpisodeTitle());
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
		viewHolder.readView.setVisibility(item.isReaded() ? View.VISIBLE : View.GONE);

		if (item.isLoginNeed()) {
			viewHolder.lockView.setImageResource(R.drawable.lock_circle);
		} else {
			viewHolder.lockView.setVisibility(View.GONE);
		}
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	public void updateRead(List<String> readList) {
		for (String id : readList) {
			for (Episode item : list) {
				if (TextUtils.equals(id, item.getEpisodeId())) {
					item.setReadFlag();
					break;
				}
			}
		}
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		@Bind(R.id.textView) public TextView titleView;
		@Bind(R.id.imageView) public ImageView thumbnailView;
		@Bind(R.id.readView) public View readView;
		@Bind(R.id.lockStatusView) public ImageView lockView;
		@Bind(R.id.progress) public View progress;

		public ViewHolder(View v) {
			super(v);
			ButterKnife.bind(this, v);
		}
	}
}
