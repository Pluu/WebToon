package com.pluu.webtoon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pluu.webtoon.R;
import com.pluu.webtoon.adapter.viewholder.BaseChattingViewHolder;
import com.pluu.webtoon.adapter.viewholder.DaumEmptyViewHolder;
import com.pluu.webtoon.adapter.viewholder.DaumLeftViewHolder;
import com.pluu.webtoon.adapter.viewholder.DaumNoticeImageViewHolder;
import com.pluu.webtoon.adapter.viewholder.DaumNoticeViewHolder;
import com.pluu.webtoon.adapter.viewholder.DaumRIghtViewHolder;
import com.pluu.webtoon.item.DetailView;
import com.pluu.webtoon.item.VIEW_TYPE;

import java.util.ArrayList;
import java.util.List;

/**
 * Detail, Chatting Adapter
 * Created by PLUUSYSTEM-NEW on 2016-01-24.
 */
public class DetailChatAdapter extends RecyclerView.Adapter<BaseChattingViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private final List<DetailView> list;

    public DetailChatAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        list = new ArrayList<>();
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
    public int getItemViewType(int position) {
        return list.get(position).getType().ordinal();
    }

    @Override
    public BaseChattingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        VIEW_TYPE view_type = VIEW_TYPE.values()[viewType];

       BaseChattingViewHolder viewHolder;
        switch (view_type) {
            case CHAT_NOTICE:
                viewHolder = new DaumNoticeViewHolder(
                        inflater.inflate(R.layout.view_chatting_notice_layout, parent, false));
                break;
            case CHAT_NOTICE_IMAGE:
                viewHolder = new DaumNoticeImageViewHolder(
                        inflater.inflate(R.layout.view_chatting_notice_image_layout, parent, false));
                break;
            case CHAT_LEFT:
                viewHolder = new DaumLeftViewHolder(
                        inflater.inflate(R.layout.view_chatting_left_layout, parent, false));
                break;
            case CHAT_RIGHT:
                viewHolder = new DaumRIghtViewHolder(
                        inflater.inflate(R.layout.view_chatting_right_layout, parent, false));
                break;
            default:
                viewHolder = new DaumEmptyViewHolder(
                        inflater.inflate(R.layout.view_chatting_empty_layout, parent, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BaseChattingViewHolder holder, int position) {
        DetailView item = list.get(position);
        holder.bind(context, item.getChatValue());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

}
