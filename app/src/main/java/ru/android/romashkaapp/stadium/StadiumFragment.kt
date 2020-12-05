package ru.android.romashkaapp.stadium

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebViewClient
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_stadium.*
import kotlinx.android.synthetic.main.fragment_stadium.cl_bottomsheet
import ru.android.romashkaapp.R
import ru.android.romashkaapp.databinding.FragmentStadiumBinding
import ru.android.romashkaapp.main.MainViewModel
import ru.android.romashkaapp.sector_seat.SectorSeatFragment.Companion.AREA_ID
import ru.android.romashkaapp.sector_seat.SectorSeatFragment.Companion.SECTOR_ID
import ru.android.romashkaapp.stadium.adapters.PricesAdapter

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class StadiumFragment : Fragment(){

    companion object{
        val EVENT_ID = "EVENT_ID"
        val CHAMPIONSHIP_TITLE = "CHAMPIONSHIP_TITLE"
    }

    lateinit var binding: FragmentStadiumBinding
    private val viewModel: StadiumViewModel by viewModels()
    private var jsInterface: AndroidJSInterface? = null
    private var pricesAdapter: PricesAdapter? = null
    private lateinit var mainViewModel: MainViewModel
    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null

    fun setViewModel(viewModel: MainViewModel){
        this.mainViewModel = viewModel
        jsInterface!!.mainViewModel = viewModel
        mainViewModel.hideNavigationBar()
    }

    class AndroidJSInterface {

        var mainViewModel: MainViewModel? = null
        var eventId: Int? = null
        var areaId: Int? = null

        @JavascriptInterface
        fun onClicked(id: String?) {
            Log.d("TAG12345", "Clicked id=$id")
            mainViewModel!!.setStadiumFragment(bundleOf(EVENT_ID to eventId, AREA_ID to areaId, SECTOR_ID to id))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stadium, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jsInterface = AndroidJSInterface()

        requireArguments().containsKey(EVENT_ID).let {
            jsInterface!!.eventId = requireArguments().getInt(EVENT_ID)
            viewModel.getEvent(requireArguments().getInt(EVENT_ID),
                requireArguments().getString(CHAMPIONSHIP_TITLE))
        }

        val webViewClient = WebViewClient()
        wv_stadium.webViewClient = webViewClient
        wv_stadium.settings.javaScriptEnabled = true
        wv_stadium.settings.builtInZoomControls = true
        wv_stadium.settings.displayZoomControls = false
        wv_stadium.setBackgroundColor(Color.TRANSPARENT)

        wv_stadium.settings.loadWithOverviewMode = true
        wv_stadium.settings.useWideViewPort = true

        binding.viewModel?.zoomView!!.observe(viewLifecycleOwner, {
           if(it){
               wv_stadium.zoomIn()
           }else{
               wv_stadium.zoomOut()
           }
        })

        binding.viewModel?.toolbarView!!.observe(viewLifecycleOwner, {
            if(it){
                setToolbarView(VISIBLE)
                iv_expand.setBackgroundResource(R.drawable.ic_expand_less)
            }else{
                setToolbarView(GONE)
                iv_expand.setBackgroundResource(R.drawable.ic_expand_more)
            }
        })

        binding.apply {
            pricesAdapter = PricesAdapter(viewModel!!.getListener())
            rv_full_prices.adapter = pricesAdapter
            rv_full_prices.layoutManager = LinearLayoutManager(context)
        }

        binding.viewModel?.pricesList!!.observe(viewLifecycleOwner, {
            cl_bottomsheet.visibility = VISIBLE
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED

            pricesAdapter!!.updateList(it)
        })

        binding.viewModel?.areaId!!.observe(viewLifecycleOwner, {
            jsInterface!!.areaId = it
        })

        binding.viewModel?.svgArea!!.observe(viewLifecycleOwner, {
            wv_stadium.addJavascriptInterface(jsInterface!!, "Android")
            wv_stadium.loadUrl(it)
        })

        initPricesView()
    }

    private fun initPricesView(){
        bottomSheetBehavior = BottomSheetBehavior.from(cl_bottomsheet)
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
        cl_bottomsheet.visibility = GONE

        bottomSheetBehavior!!.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // handle onSlide
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        cl_bottomsheet.visibility = GONE
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {

                    }
                    BottomSheetBehavior.STATE_SETTLING -> {

                    }
                }
            }
        })

        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN

        binding.viewModel?.selectedItem!!.observe(viewLifecycleOwner, {
            if (it) {
                //todo set to main view
                pricesAdapter!!.selectedItems
                cl_bottomsheet.visibility = GONE
                bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
            }else{
                pricesAdapter!!.resetSelectedItems()
            }
        })
    }

    private fun setToolbarView(v: Int){
        tv_event_type.visibility = v
        tv_event_place.visibility = v
        civ_logo1.visibility = v
        civ_logo2.visibility = v
    }

}