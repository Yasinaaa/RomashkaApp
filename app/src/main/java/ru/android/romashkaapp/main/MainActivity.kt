package ru.android.romashkaapp.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import ru.android.romashkaapp.R


class MainActivity : AppCompatActivity() {

    /**
     * You client id, you have it from the google console when you register your project
     * https://console.developers.google.com/a
     */
    private val CLIENT_ID =
        "1020597890643-n3m1t7fplcv2t0f78g7miachq7lgnbrv.apps.googleusercontent.com"

    /**
     * The redirect uri you have define in your google console for your project
     */
    private val REDIRECT_URI = "ru.android.romashkaapp:/sdd"

    /**
     * The redirect root uri you have define in your google console for your project
     * It is also the scheme your Main Activity will react
     */
    private val REDIRECT_URI_ROOT = "ru.android.romashkaapp"

    /**
     * You are asking to use a code when autorizing
     */
    private val CODE = "code"

    /**
     * The scope: what do we want to use
     * Here we want to be able to do anything on the user's GDrive
     */
    val API_SCOPE = "https://www.googleapis.com/auth/drive"

    private var viewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        imageView.setImageDrawable(getDrawable(R.drawable.ic_plan))

        val factory: ViewModelProvider.Factory = NewInstanceFactory()
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
        startAuth()

        viewModel!!.picture.observe(this, Observer {
            val imageByteArray: ByteArray = Base64.decode(it, Base64.DEFAULT)
            Glide.with(applicationContext)
                .load(imageByteArray)
                .into(iv)
        })

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