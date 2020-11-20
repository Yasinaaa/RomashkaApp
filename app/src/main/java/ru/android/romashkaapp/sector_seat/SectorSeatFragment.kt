package ru.android.romashkaapp.sector_seat

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_sector.*
import ru.android.romashkaapp.R
import ru.android.romashkaapp.adapter.helpers.SwipeRemoveActionCallback
import ru.android.romashkaapp.adapter.helpers.SwipeRemoveItemDecoration
import ru.android.romashkaapp.databinding.FragmentSectorBinding
import ru.android.romashkaapp.sector_seat.adapter.AnimationOnLastItemAdapter

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class SectorSeatFragment : Fragment() {

    lateinit var binding: FragmentSectorBinding
    private val viewModel: SectorSeatViewModel by viewModels()
    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null
    private val adapter = AnimationOnLastItemAdapter()
    private lateinit var swipeItemTouchHelper: ItemTouchHelper

    private lateinit var background: ColorDrawable
    private var icon: Drawable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sector, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        background = ColorDrawable(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
        icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)

        binding.apply {
            rv_cart_items.adapter = adapter
            rv_cart_items.layoutManager = LinearLayoutManager(context)
            rv_cart_items.addItemDecoration(
                SwipeRemoveItemDecoration(
                    background,
                    icon
                )
            )
        }

        viewModel.list.observe(viewLifecycleOwner, {
            adapter.updateList(it)
        })

        swipeItemTouchHelper = ItemTouchHelper(
            SwipeRemoveActionCallback(
                background,
                icon,
                arrayListOf(viewModel, adapter),
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            )
        )
        swipeItemTouchHelper.attachToRecyclerView(binding.rvCartItems)

        val webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                loadJs(view)
            }
        }

        wv_stadium.webViewClient = webViewClient
        wv_stadium.settings.javaScriptEnabled = true
        wv_stadium.settings.builtInZoomControls = true
        wv_stadium.settings.displayZoomControls = false
        wv_stadium.setBackgroundColor(Color.TRANSPARENT)
//        wv_stadium.addJavascriptInterface(AndroidJSInterface, "Android")

        wv_stadium.settings.loadWithOverviewMode = true
        wv_stadium.settings.useWideViewPort = true

        wv_stadium.loadUrl("file:///android_asset/www/sector203.svg")

        binding.viewModel?.zoomView!!.observe(viewLifecycleOwner, Observer {
            if(it){
                wv_stadium.zoomIn()
            }else{
                wv_stadium.zoomOut()
            }
        })

        bottomSheetBehavior = BottomSheetBehavior.from(cl_bottomsheet)
        bottomSheetBehavior!!.peekHeight = 300
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomSheetBehavior!!.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // handle onSlide
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        rv_cart_items.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        rv_cart_items.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        rv_cart_items.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                        rv_cart_items.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun loadJs(webView: WebView) {
        webView.requestFocus()
    }

}


