package android.ru.romashkaapp

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView.setImageDrawable(getDrawable(R.drawable.ic_plan))
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
    }

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
}