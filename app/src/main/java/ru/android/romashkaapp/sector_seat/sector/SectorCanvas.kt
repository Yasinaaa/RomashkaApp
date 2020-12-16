package ru.android.romashkaapp.sector_seat.sector

import android.content.Context
import android.graphics.*
import android.ru.romashkaapp.models.SeatModel
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.annotation.Nullable
import java.util.*

/**
 * Created by yasina on 07.12.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class SectorCanvas: View{

    private var seats: MutableList<SeatModel> = mutableListOf()

    fun setSeat(seats: MutableList<SeatModel>){
        this.seats = seats
    }

    private val LOG_TAG: String = SectorCanvas::class.java.getSimpleName()
    private val TAG: String = SectorCanvas::class.java.getSimpleName()
    private val RADIUS: Int = 30
    private var canvas: Canvas? = null
    private var historyList = ArrayList<SeatShape>()
    private var canvasTouch: CanvasTouch? = null

    private var initX: Float = 0f // See onTouchEvent
    private var initY: Float = 0f // See onTouchEvent

    private var canvasX: Float = 0f // x-coord of canvas center
    private var canvasY: Float = 0f // y-coord of canvas center
    private var dispWidth: Float = 100f // (Supposed to be) width of entire canvas
    private var dispHeight: Float = 100f // (Supposed to be) height of entire canvas

    private var dragging: Boolean = false // May be unnecessary
    private var firstDraw: Boolean = true

    // Detector for scaling gestures (i.e. pinching or double tapping
    private var detector: ScaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    private var scaleFactor: Float = 1f // Zoom level (initial value is 1x)

    private val MIN_ZOOM: Float = 1f
    private val MAX_ZOOM: Float = 4f

    constructor(context: Context?, @Nullable attrs: AttributeSet?): super(context, attrs) {
        isFocusable = true
        isFocusableInTouchMode = true
        setupPaint()
        Log.d(TAG, "  constructor called")
    }

    private var mScaleFactor = 1f

    fun setCanvas(c: CanvasTouch){
        canvasTouch = c
    }

    // defines paint and canvas
    private var drawPaint: Paint? = null
    private var textPaint: Paint? = null

    // Setup paint with color and stroke styles
    private fun setupPaint() {
        drawPaint = Paint()
        drawPaint!!.color = Color.BLUE
        drawPaint!!.isAntiAlias = true
        drawPaint!!.strokeWidth = 1f
        drawPaint!!.style = Paint.Style.STROKE
        drawPaint!!.strokeJoin = Paint.Join.ROUND
        drawPaint!!.strokeCap = Paint.Cap.ROUND

        textPaint = Paint()
        textPaint!!.color = Color.BLACK
        textPaint!!.textSize = 18f
        textPaint!!.isAntiAlias = true
        textPaint!!.textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas

        if (firstDraw) {
            canvasX = width/2f
            canvasY = height/2f
            firstDraw = false
        }
        canvas.scale(scaleFactor, scaleFactor) // Scale the canvas according to scaleFactor
        canvas.translate(canvasX, canvasY)

        Log.d(TAG, "  onDraw called")
        for (shape in getHistoryList()) {
            if (shape.isVisible) {
                drawPaint!!.color = Color.BLUE
//                canvas.drawCircle(
//                    shape.getxCordinate().toFloat(),
//                    shape.getyCordinate().toFloat(),
//                    RADIUS.toFloat(),
//                    drawPaint!!
//                )
//                drawRectangle(shape.getxCordinate(), shape.getyCordinate())
                drawPaint!!.color = Color.RED
                shape.rect = Rect(
                    (shape.getxCordinate() - squareSideHalf * RADIUS).toInt(),
                    (shape.getyCordinate() - squareSideHalf * RADIUS).toInt(),
                    (shape.getxCordinate() + squareSideHalf * RADIUS).toInt(),
                    (shape.getyCordinate() + squareSideHalf * RADIUS).toInt()
                )
                canvas.drawRect(shape.rect!!, drawPaint!!)
                canvas.drawText(
                    shape.title, shape.rect!!.exactCenterX().toFloat(),
                    shape.rect!!.exactCenterY().toFloat(), textPaint!!
                )
            }
        }

        canvas.apply {
            save()
            scale(mScaleFactor, mScaleFactor)
            // onDraw() code goes here
            restore()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x: Float = event.x
        val y: Float = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                for (shape in getHistoryList()) {
                    if(shape.rect!!.contains(x.toInt(), y.toInt())){
                        Log.d(LOG_TAG, " ACTION_DOWN shape=" + shape.title)
                    }
                }

                // Might not be necessary; check out later
                dragging = true
                // We want to store the coords of the user's finger as it is before they move
                //  in order to calculate dx and dy
                if(x < 2000) {
                    initX = x
                    initY = y
                }
            }

            MotionEvent.ACTION_MOVE -> {

                // Self explanatory; the difference in x- and y-coords between successive calls to
                //  onTouchEvent
                val dx: Float = x - initX
                val dy: Float = y - initY

                if (dragging) {
                    // Move the canvas dx units right and dy units down
                    // dx and dy are divided by scaleFactor so that panning speeds are consistent
                    //  with the zoom level
                        if(scaleFactor > 0.5) {
                            canvasX += dx / scaleFactor
                            canvasY += dy / scaleFactor

                            invalidate() // Re-draw the canvas

                            // Change initX and initY to the new x- and y-coords
                            initX = x
                            initY = y
                        }
                }
            }

            MotionEvent.ACTION_POINTER_UP -> {
                // This sets initX and initY to the position of the pointer finger so that the
                //  screen doesn't jump when it's lifted with the main finger still down
                initX = x
                initY = y
            }

            MotionEvent.ACTION_UP -> dragging = false // Again, may be unnecessary
        }
        detector.onTouchEvent(event) // Listen for scale gestures (i.e. pinching or double tap+drag
        // Just some useful coordinate data
        Log.d("TOUCHEVENT", "x: $x, y: $y,\ninitY: $initX, initY,\n" +
                "canvasX: $canvasX, canvasY: $canvasY,\nwidth: $dispWidth, height: $dispHeight\n" +
                "focusX: ${detector.focusX}, focusY: ${detector.focusY}")
        // Data pertaining to fingers for responsiveness and stuff
        Log.d("TOUCHEVENT", "Action: ${event.action and MotionEvent.ACTION_MASK}\n")

        return true
    }

    var squareSideHalf = 1 / Math.sqrt(2.0)
    //Consider pivot x,y as centroid.

    fun getHistoryList(): List<SeatShape> {
        return historyList
    }

    fun setHistoryList(historyList: ArrayList<SeatShape>) {
        this.historyList = historyList
    }

    fun getCanvasTouch(): CanvasTouch? {
        return canvasTouch
    }

    fun setCanvasTouch() {
        this.canvasTouch = object : CanvasTouch {
            override fun onClickEvent(event: MotionEvent) {
                Log.d("LOG_TAG", " onClickEvent done ")
                ShapesInteractor.instance.changeShapeOnTouch(event.x, event.y, 12)
            }

            override fun onLongPressEvent(initialTouchX: Float, initialTouchY: Float) {
                Log.d("LOG_TAG", " onLongPressEvent done ")
                ShapesInteractor.instance.changeShapeOnTouch(initialTouchX, initialTouchY, 14)
            }
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            // Self-explanatory
            scaleFactor *= detector.scaleFactor
            // If scaleFactor is less than 0.5x, default to 0.5x as a minimum. Likewise, if
            //  scaleFactor is greater than 10x, default to 10x zoom as a maximum.
            scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM))

            invalidate() // Re-draw the canvas

            return true
        }
    }

}