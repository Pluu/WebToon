package com.pluu.webtoon.ui.detail;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pluu.webtoon.R;
import com.pluu.webtoon.item.DetailView;

import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Default Detail Fragment
 * Created by PLUUSYSTEM-NEW on 2016-01-30.
 */
@SuppressLint("ValidFragment")
public class DefaultDetailFragment extends BaseDetailFragment {

    @Bind(R.id.webView)
    WebView webview;

    private final GestureDetector gd;
    private final int bottomHeight;
    private int actionBarHeight;

    public DefaultDetailFragment(GestureDetector gd, int bottomHeight) {
        this.gd = gd;
        this.bottomHeight = bottomHeight;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_default_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initWebViewSetting();

        bindListener.firstBind();
    }

    @Override
    public void onStop() {
        if (webview != null) {
            webview.stopLoading();
        }
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void init() {
        TypedValue t = new TypedValue();
        getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, t, true);
        actionBarHeight = getResources().getDimensionPixelSize(t.resourceId);
    }

    private void initWebViewSetting() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }

        webview.getSettings().setBlockNetworkImage(false);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setUseWideViewPort(false);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                listener.loadingHide();
                listener.childCallToggle(true);
            }
        });

        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);
            }
        });
        webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // LongClick Disable
                return true;
            }
        });
    }

    public void loadView(List<DetailView> list) {
        webview.setVisibility(View.VISIBLE);

        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>")
                .append("<html>")
                .append("<head><meta charset=\"utf-8\"><style>")
                .append("body{").append("padding-top:").append(actionBarHeight).append("px; padding-bottom:").append(bottomHeight).append("px; }")
                .append("img{max-width: 100%; height: auto; display:block;}")
                .append("ul{list-style: none; padding-left: 0px;}")
                .append("ul li:before{padding:0px; position:absolute; top:0; left:0px; }")
                .append("</style></head>")
                .append("<body>");

        builder.append("<ul>");

        Iterator<DetailView> iterator = list.iterator();
        DetailView item;
        while (iterator.hasNext()) {
            item = iterator.next();
//			Log.i(TAG, "Load=" + item);
            builder.append("<li>");

            switch (item.getType()) {
                case IMAGE:
                    builder.append("<img src=\"").append(item.getValue()).append("\" />");
                    break;
                case TEXT:
                    builder.append(item.getValue().replaceAll("\n", "<br></br>"));
                    break;
            }
            builder.append("</li>");
        }

        builder.append("</ul></body></html>");
//		Log.i(TAG, "Result=" + builder.toString());
        webview.loadDataWithBaseURL(null, builder.toString(), "text/html", "utf-8", null);
    }
}
