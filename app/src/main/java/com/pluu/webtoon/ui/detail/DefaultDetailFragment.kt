package com.pluu.webtoon.ui.detail

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.pluu.webtoon.R
import com.pluu.webtoon.item.DetailView
import com.pluu.webtoon.item.VIEW_TYPE
import kotlinx.android.synthetic.main.fragment_default_detail.*

/**
 * Default Detail Fragment
 * Created by pluu on 2017-05-06.
 */
@SuppressLint("ValidFragment")
class DefaultDetailFragment(private val gd: GestureDetector, private val bottomHeight: Int) : BaseDetailFragment() {

    private var actionBarHeight: Int = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater?.inflate(R.layout.fragment_default_detail, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        initWebViewSetting()
        firstBind()
    }

    override fun onStop() {
        webView?.stopLoading()
        super.onStop()
    }

    private fun init() {
        val t = TypedValue()
        activity.theme.resolveAttribute(android.R.attr.actionBarSize, t, true)
        actionBarHeight = resources.getDimensionPixelSize(t.resourceId)
    }

    private fun initWebViewSetting() {
        webView.apply {
            settings.layoutAlgorithm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
            } else {
                WebSettings.LayoutAlgorithm.NORMAL
            }

            settings.blockNetworkImage = false
            settings.loadsImagesAutomatically = true
            settings.useWideViewPort = false
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)

            setWebViewClient(object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    listener?.loadingHide()
                    listener?.childCallToggle(true)
                }
            })

            setOnTouchListener { _, event -> gd.onTouchEvent(event) }
            setOnLongClickListener { true }
        }
    }

    override fun loadView(list: List<DetailView>) {
        webView.visibility = View.VISIBLE

        val builder = StringBuilder()
        builder.append("<!DOCTYPE html>")
                .append("<html>")
                .append("<head><meta charset=\"utf-8\"><style>")
                .append("body{").append("padding-top:").append(actionBarHeight).append("px; padding-bottom:").append(bottomHeight).append("px; }")
                .append("img{max-width: 100%; height: auto; display:block;}")
                .append("ul{list-style: none; padding-left: 0px;}")
                .append("ul li:before{padding:0px; position:absolute; top:0; left:0px; }")
                .append("</style></head>")
                .append("<body>")

        builder.append("<ul>")

        val iterator = list.iterator()
        var item: DetailView
        while (iterator.hasNext()) {
            item = iterator.next()
            //			Log.i(TAG, "Load=" + item);
            builder.append("<li>")

            @Suppress("NON_EXHAUSTIVE_WHEN")
            when (item.type) {
                VIEW_TYPE.IMAGE -> builder.append("<img src=\"").append(item.value).append("\" />")
                VIEW_TYPE.TEXT -> builder.append(item.value!!.replace("\n".toRegex(), "<br></br>"))
            }
            builder.append("</li>")
        }

        builder.append("</ul></body></html>")
        //		Log.i(TAG, "Result=" + builder.toString());
        webView.loadDataWithBaseURL(null, builder.toString(), "text/html", "utf-8", null)
    }
}
