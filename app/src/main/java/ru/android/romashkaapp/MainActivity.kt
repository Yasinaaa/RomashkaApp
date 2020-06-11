package android.ru.romashkaapp

import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView.setImageDrawable(getDrawable(R.drawable.ic_plan))

        val gestureDetector =
            GestureDetector(this, object : SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    Log.d("adaa", " " + e.x + " " + e.y)
                    return true
                }
            })
        imageView.setOnTouchListener { view, motionEvent -> gestureDetector.onTouchEvent(motionEvent) }
    }
}