package com.pluu.webtoon.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pluu.webtoon.R
import com.pluu.webtoon.databinding.FragmentDefaultDetailBinding
import com.pluu.webtoon.utils.observeNonNull
import com.pluu.webtoon.utils.resolveAttribute
import com.pluu.webtoon.utils.viewbinding.viewBinding
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * Default Detail Fragment
 * Created by pluu on 2017-05-06.
 */
class DetailFragment : Fragment(R.layout.fragment_default_detail) {

    private val viewModel: DetailViewModel by sharedViewModel()

    private val binding by viewBinding(FragmentDefaultDetailBinding::bind)

    private var listener: ToggleListener? = null
    private var bindListener: FirstBindListener? = null
    private var actionBarHeight: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        firstBind()
    }

    private fun init() {
        val t = resolveAttribute(android.R.attr.actionBarSize)
        actionBarHeight = resources.getDimensionPixelSize(t.resourceId)

        viewModel.list.observeNonNull(viewLifecycleOwner) { list ->
            with(binding.recyclerView) {
                layoutManager = LinearLayoutManager(context).apply {
                    isItemPrefetchEnabled = true
                    initialPrefetchItemCount = 4
                }
                adapter = DetailAdapter(list, listener)
                addItemDecoration(
                    DetailItemDecoration(
                        actionBarHeight,
                        context.resources.getDimensionPixelOffset(
                            R.dimen.detail_bottom_size
                        )
                    )
                )
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? ToggleListener
            ?: throw RuntimeException("$context must implement ToggleListener")

        bindListener = context as? FirstBindListener
            ?: throw RuntimeException("$context must implement FirstBindListener")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        bindListener = null
    }

    private fun firstBind() {
        bindListener?.firstBind()
    }
}
