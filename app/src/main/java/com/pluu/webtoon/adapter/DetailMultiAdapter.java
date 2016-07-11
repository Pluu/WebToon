package com.pluu.webtoon.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.pluu.webtoon.R;
import com.pluu.webtoon.item.DetailView;
import com.pluu.webtoon.ui.view.AspectRatioImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Detail, Multi Adapter
 * Created by PLUUSYSTEM-NEW on 2016-01-24.
 */
public class DetailMultiAdapter extends RecyclerView.Adapter<DetailMultiAdapter.ViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private final List<DetailView> list;
    private final int profileSize;

    public DetailMultiAdapter(Context context, int profileSize) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        list = new ArrayList<>();
        this.profileSize = profileSize;
    }

    public void setList(List<DetailView> list) {
        this.list.addAll(list);
    }

    public void clear() {
        if (list != null) {
            list.clear();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.view_multi_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DetailView item = list.get(position);

        switch (item.getType()) {
            case MULTI_IMAGE:
                holder.empty.setVisibility(View.GONE);
                if (item.getHeight() != 0) {
                    holder.imageView.sethRatio(item.getHeight());
                    Glide.with(context)
                            .load(item.getValue())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(holder.imageView);
                } else {
                    Glide.with(context)
                            .load(item.getValue())
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    float hRatio = (float) resource.getHeight() / resource.getWidth();
                                    item.setHeight(hRatio);
                                    holder.imageView.sethRatio(hRatio);
                                    holder.imageView.setImageBitmap(resource);
                                }
                            });
                }
                break;
            case MULTI_GIF:
                holder.empty.setVisibility(View.GONE);
                if (item.getHeight() != 0) {
                    holder.imageView.sethRatio(item.getHeight());
                    Glide.with(context)
                            .load(item.getValue())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(holder.imageView);
                } else {
                    Glide.with(context)
                            .load(item.getValue())
                            .asGif()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(new SimpleTarget<GifDrawable>() {
                                @Override
                                public void onResourceReady(GifDrawable resource, GlideAnimation<? super GifDrawable> glideAnimation) {
                                    float hRatio = (float) resource.getIntrinsicHeight() / resource.getIntrinsicWidth();
                                    item.setHeight(hRatio);
                                    holder.imageView.sethRatio(hRatio);
                                    holder.imageView.setImageDrawable(resource);
                                }
                            });
                }
                break;
            case CHAT_EMPTY:
                holder.imageView.setVisibility(View.GONE);
                holder.empty.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView)
        AspectRatioImageView imageView;
        @BindView(R.id.emptyView)
        View empty;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
