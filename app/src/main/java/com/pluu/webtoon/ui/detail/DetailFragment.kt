package com.pluu.webtoon.ui.detail

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pluu.webtoon.databinding.FragmentDefaultDetailBinding
import com.pluu.webtoon.utils.observeNonNull
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * Default Detail Fragment
 * Created by pluu on 2017-05-06.
 */
@SuppressLint("ValidFragment")
class DetailFragment(
    private val bottomHeight: Int
) : Fragment() {

    private val viewModel: DetailViewModel by sharedViewModel()

    private lateinit var binding: FragmentDefaultDetailBinding

    private var listener: ToggleListener? = null
    private var bindListener: FirstBindListener? = null
    private var actionBarHeight: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDefaultDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        firstBind()
    }

    private fun init() {
        val t = TypedValue()
        activity?.theme?.resolveAttribute(android.R.attr.actionBarSize, t, true)
        actionBarHeight = resources.getDimensionPixelSize(t.resourceId)

        viewModel.list.observeNonNull(viewLifecycleOwner) { list ->
            with(binding.recyclerView) {
                layoutManager = LinearLayoutManager(context).apply {
                    isItemPrefetchEnabled = true
                    initialPrefetchItemCount = 4
                }
                adapter = DetailAdapter(list, listener)
                addItemDecoration(DetailItemDecoration(actionBarHeight, bottomHeight))
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
