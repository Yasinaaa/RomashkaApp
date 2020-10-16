package ru.android.romashkaapp.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private var viewModel: MainViewModel? = null

    object AndroidJSInterface {
        @JavascriptInterface
        fun onClicked(id: String?) {
            Log.d("TAG12345", "Clicked id=$id")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                loadJs(view)
            }
        }
        val webView = WebView(this)
        webView.webViewClient = webViewClient
        webView.settings.javaScriptEnabled = true
        webView.settings.builtInZoomControls = true;
        webView.addJavascriptInterface(AndroidJSInterface, "Android")
        setContentView(webView)
        webView.loadUrl("file:///android_asset/www/svgsina.svg")
    }
    private fun loadJs(webView: WebView) {
//        webView.loadUrl(
//            """javascript:(function f() {
//                var b = document.getElementById('namefromjs')
//                b.setAttribute('onclick', 'Android.onClicked()');})()"""
//        )
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

//    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        imageView.setImageDrawable(getDrawable(R.drawable.ic_plan))

//        val factory: ViewModelProvider.Factory = NewInstanceFactory()
//        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
//        startAuth()
//
//        viewModel!!.picture.observe(this, Observer {
//            val imageByteArray: ByteArray = Base64.decode(it, Base64.DEFAULT)
//            Glide.with(applicationContext)
//                .asGif()
//                .load(imageByteArray)
//                .into(iv)
//        })

//        wb.setWebViewClient(MyBrowser())
//        wb.getSettings().setLoadsImagesAutomatically(true)
//        wb.getSettings().setJavaScriptEnabled(true)
//        wb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY)
//        wb.loadUrl("https://demo-games.infomatika.ru/ru/")

//        val gestureDetector =
//            GestureDetector(this, object : SimpleOnGestureListener() {
//                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
//                    Log.d("adaa", " " + e.x + " " + e.y)
//                    return true
//                }
//            })
//        imageView.setOnTouchListener { _, motionEvent -> gestureDetector.onTouchEvent(motionEvent) }
//        loadJs()

//        val webViewClient = object : WebViewClient() {
//            override fun onPageFinished(view: WebView, url: String) {
//                super.onPageFinished(view, url);
//            }
//        }
//
//        webview.webViewClient = webViewClient
//        webview.settings.javaScriptEnabled = true
//        webview.settings.userAgentString = "Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)"
//        webview.addJavascriptInterface(MyJavaScriptInterface(), "HTMLOUT")
//        webview.webChromeClient = MyChromeClient()
//        webview.loadUrl("file:///android_asset/www/index.html");
////        webview.loadUrl("https://demo-games.infomatika.ru/ru/site/htmlsina")
//        webview.requestFocus()
//
////        button.setOnClickListener{webview.loadUrl(
////            "javascript:(function(){"+
////                    "l=document.getElementById('namefromjs');"+
////                    "e=document.createEvent('HTMLEvents');"+
////                    "e.initEvent('click',true,true);"+
////                    "l.dispatchEvent(e);"+
////                    "})()"
////        )}
//        buttonClick.setOnClickListener { webview.loadUrl("javascript:(function(){document.getElementById('buttonClick').click();})()");}
//    }

    class MyJavaScriptInterface {
        fun showHTML(html: String?) {
            Log.d("TAG", "Help button clicked")
        }
    }

//    class AndroidJSInterface {
//
//        @JavascriptInterface
//        fun onClicked(sectorId: String) {
//            Log.d("TAG", "Help button clicked")
//        }
//        @SuppressWarnings("unused")
//        fun showHTML(html: String?) {
//            Log.d("TAG", "Help button clicked")
//        }
//    }

    internal class MyChromeClient : WebChromeClient()


    private class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {

        }
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