package com.pluu.webtoon.ui.detail;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.pluu.webtoon.item.DetailView;

import java.util.List;

/**
 * Base Detail Fragment
 * Created by PLUUSYSTEM-NEW on 2016-01-30.
 */
public abstract class BaseDetailFragment extends Fragment {

    protected ToggleListener listener;
    protected FirstBindListener bindListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToggleListener) {
            listener = (ToggleListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ToggleListener");
        }

        if (context instanceof FirstBindListener) {
            bindListener = (FirstBindListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FirstBindListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        bindListener = null;
    }

    public abstract void loadView(List<DetailView> list);

}
