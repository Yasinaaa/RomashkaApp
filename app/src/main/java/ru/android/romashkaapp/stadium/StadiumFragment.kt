package ru.android.romashkaapp.stadium

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_stadium2.*
import ru.android.romashkaapp.R
import ru.android.romashkaapp.databinding.FragmentStadium2Binding
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class StadiumFragment : Fragment(){

    lateinit var binding: FragmentStadium2Binding
    private val viewModel: StadiumViewModel by viewModels()

    class AndroidJSInterface(f: StadiumFragment) {

        private var fr: StadiumFragment? = f

        @JavascriptInterface
        fun onClicked(id: String?) {
            Log.d("TAG12345", "Clicked id=$id")

            GlobalScope.launch {
                launch(Dispatchers.Main) {
                    findNavController(fr!!).navigate(
                        R.id.nav_sector_seats,
                        null,
                        NavOptions.Builder().setPopUpTo(
                            R.id.nav_stadium,
                            true
                        ).build()
                    )
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stadium2, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        wv_stadium.addJavascriptInterface(AndroidJSInterface(this), "Android")

        wv_stadium.settings.loadWithOverviewMode = true
        wv_stadium.settings.useWideViewPort = true

        wv_stadium.loadUrl("file:///android_asset/www/kazan_arena.svg") //kazan_arena.svg

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
    }

    private fun setToolbarView(v: Int){
        tv_event_type.visibility = v
        tv_event_place.visibility = v
        civ_logo1.visibility = v
        civ_logo2.visibility = v
    }

    private fun loadJs(webView: WebView) {
//        webView.loadUrl(
//            """javascript:(function f() {
//                    var sectors = document.querySelectorAll('g');
//                    for (const sector of sectors) {
//                      var sectorId = sector.getAttribute('sector_id');
//                      sector.setAttribute('onclick', 'Android.onClicked(' + sectorId + ')');
//                    }
//                  })()"""
//        )
//        webView.requestFocus()
    }
}