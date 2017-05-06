package com.pluu.webtoon.ui.detail

import android.content.Context
import android.support.v4.app.Fragment

import com.pluu.webtoon.item.DetailView

/**
 * Base Detail Fragment
 * Created by pluu on 2017-05-06.
 */
abstract class BaseDetailFragment : Fragment() {

    protected var listener: ToggleListener? = null
    protected var bindListener: FirstBindListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as? ToggleListener
                ?: throw RuntimeException(context.toString() + " must implement ToggleListener")

        bindListener = context as? FirstBindListener
            ?: throw RuntimeException(context.toString() + " must implement FirstBindListener")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        bindListener = null
    }

    abstract fun loadView(list: List<DetailView>)

    fun firstBind() {
        bindListener?.firstBind()
    }

}
