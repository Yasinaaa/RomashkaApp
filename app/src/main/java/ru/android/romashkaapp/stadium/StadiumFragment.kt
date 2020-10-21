package ru.android.romashkaapp.stadium

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
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
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_stadium.*
import ru.android.romashkaapp.R
import ru.android.romashkaapp.databinding.FragmentStadiumBinding

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class StadiumFragment : Fragment(){

    lateinit var binding: FragmentStadiumBinding
    private val viewModel: StadiumViewModel by viewModels()

    object AndroidJSInterface {
        @JavascriptInterface
        fun onClicked(id: String?) {
            Log.d("TAG12345", "Clicked id=$id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stadium, container, false)
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
        wv_stadium.addJavascriptInterface(AndroidJSInterface, "Android")

        wv_stadium.settings.loadWithOverviewMode = true
        wv_stadium.settings.useWideViewPort = true

        wv_stadium.loadUrl("file:///android_asset/www/kazan_arena.svg")

        binding.viewModel?.zoomView!!.observe(viewLifecycleOwner, Observer {
           if(it){
               wv_stadium.zoomIn()
           }else{
               wv_stadium.zoomOut()
           }
        })
    }

    private fun loadJs(webView: WebView) {
        webView.loadUrl(
            """javascript:(function f() {
                    var sectors = document.querySelectorAll('g[free][view_id]');
                    for (const sector of sectors) {
                      var sectorId = sector.getAttribute('sector_id');
                      sector.setAttribute('onclick', 'Android.onClicked(' + sectorId + ')');
                    }
                  })()"""
        )
        webView.requestFocus()
    }
}