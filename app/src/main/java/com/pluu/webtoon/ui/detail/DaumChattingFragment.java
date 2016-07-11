package com.pluu.webtoon.ui.detail;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.pluu.webtoon.R;
import com.pluu.webtoon.adapter.DetailChatAdapter;
import com.pluu.webtoon.adapter.viewholder.BaseChattingViewHolder;
import com.pluu.webtoon.item.DetailView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Daum Chatting Fragment
 * Created by PLUUSYSTEM-NEW on 2016-01-30.
 */
@SuppressLint("ValidFragment")
public class DaumChattingFragment extends BaseDetailFragment {

    private final GestureDetector gd;

    @BindView(R.id.chattingList)
    RecyclerView chattingList;

    private DetailChatAdapter adapter;
    private Unbinder bind;

    public DaumChattingFragment(GestureDetector gd) {
        this.gd = gd;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daum_chatting, container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initChattingSetting();
        bindListener.firstBind();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

    private void initChattingSetting() {
        chattingList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new DetailChatAdapter(getContext()) {
            @Override
            public BaseChattingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                BaseChattingViewHolder holder = super.onCreateViewHolder(parent, viewType);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.childCallToggle(false);
                    }
                });
                return holder;
            }
        };
        chattingList.setAdapter(adapter);

        final int padding = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        chattingList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return gd.onTouchEvent(e);
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) { }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }
        });
        chattingList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = padding;
            }
        });
    }

    public void loadView(List<DetailView> list) {
        chattingList.setVisibility(View.VISIBLE);
        adapter.clear();
        adapter.setList(list);
        adapter.notifyDataSetChanged();
        chattingList.scrollToPosition(0);
        listener.childCallToggle(true);
    }

}
