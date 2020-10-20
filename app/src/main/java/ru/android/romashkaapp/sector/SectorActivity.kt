package ru.android.romashkaapp.sector

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class SectorActivity : AppCompatActivity(){

    object AndroidJSInterface {
        @JavascriptInterface
        fun onClicked(id: String?) {
            Log.d("TAG12345", "Clicked id=$id")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//
//        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//            viewModel!!.saveSvgToLocal(requireContext())
//        }else
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 12)
//
////        startAuth()
//
//        val webViewClient = object : WebViewClient() {
//            override fun onPageFinished(view: WebView, url: String) {
//                loadJs(view)
//            }
//        }
//
//        webview.webViewClient = webViewClient
//        webview.settings.javaScriptEnabled = true
//        webview.settings.builtInZoomControls = true;
//        webview.addJavascriptInterface(MainActivity.AndroidJSInterface, "Android")
//        webview.loadUrl("file:///android_asset/www/svgsina.svg")
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

    private fun startAuth(){
//        val authorizeUrl: HttpUrl =
//            HttpUrl.
//            parse("https://kamalteatr.ru/tickets/site/yasinevich")!!.newBuilder() //
//            .addQueryParameter("client_id", CLIENT_ID)
//            .addQueryParameter("scope", API_SCOPE)
//            .addQueryParameter("redirect_uri", REDIRECT_URI)
//            .addQueryParameter("response_type", CODE)
//            .build()
//        val i = Intent(Intent.ACTION_VIEW, Uri.parse(authorizeUrl.url().toString()))
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
//        i.setPackage("com.android.chrome");
//        startActivityForResult(i, 865)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode === Activity.RESULT_CANCELED) {

        }
    }
}