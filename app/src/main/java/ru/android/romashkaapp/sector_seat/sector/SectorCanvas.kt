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
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import ru.android.romashkaapp.R
import java.util.*

/**
 * Created by yasina on 07.12.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class SectorCanvas: View, ScaleGestureDetector.OnScaleGestureListener  {

    private var seats: MutableList<SeatModel> = mutableListOf()

    fun setSeat(seats: MutableList<SeatModel>){
        this.seats = seats
    }

    private val LOG_TAG: String = SectorCanvas::class.java.getSimpleName()
    private val TAG: String = SectorCanvas::class.java.getSimpleName()
    private val RADIUS: Int = 20
    private var canvas: Canvas? = null
    var historyList = ArrayList<SeatShape>()
    var rows = mutableMapOf<Int, Int>()
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
    private var detector: ScaleGestureDetector = ScaleGestureDetector(context, this)
    private var scaleFactor: Float = 0.5f // Zoom level (initial value is 1x)

    private val MIN_ZOOM: Float = 1f
    private val MAX_ZOOM: Float = 4f

    constructor(context: Context?, @Nullable attrs: AttributeSet?): super(context, attrs) {
        isFocusable = true
        isFocusableInTouchMode = true
        setupPaint()
        Log.d(TAG, "  constructor called")
    }

    fun setCanvas(c: CanvasTouch){
        canvasTouch = c
    }

    // defines paint and canvas
    private var drawPaint: Paint? = null
    private var textPaint: Paint? = null
    private var colTextPaint: Paint? = null

    // Setup paint with color and stroke styles
    private fun setupPaint() {

        drawPaint = Paint()
        drawPaint!!.color = Color.BLUE
        drawPaint!!.isAntiAlias = true
        drawPaint!!.strokeWidth = 1f
        drawPaint!!.style = Paint.Style.STROKE
        drawPaint!!.strokeJoin = Paint.Join.ROUND
        drawPaint!!.strokeCap = Paint.Cap.ROUND
        drawPaint!!.style = Paint.Style.FILL

        textPaint = Paint()
        textPaint!!.color = Color.BLACK
        textPaint!!.textSize = 18f
        textPaint!!.isAntiAlias = true
        textPaint!!.textAlign = Paint.Align.CENTER


        val typeface = ResourcesCompat.getFont(context, R.font.pfbeausanspro_bold)
        colTextPaint = Paint()
        colTextPaint!!.color = Color.WHITE
        colTextPaint!!.typeface = typeface
        colTextPaint!!.textSize = 18f
        colTextPaint!!.isAntiAlias = true
        colTextPaint!!.textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas

        if (firstDraw) {
            canvasX = width/2f
            canvasY = height/2f
            firstDraw = false
        }
//        canvas.scale(scaleFactor, scaleFactor)
//        if (canvasX != 0f && canvasY != 0f)
  //      canvas.translate(canvasX, canvasY)
        //
//        canvas.save()
    //for zoom
//        canvas.translate(_xoffset, _yoffset)//for pan

        this.canvas!!.drawColor(ContextCompat.getColor(context, android.R.color.white))

        Log.d(TAG, "  onDraw called")
        for (shape in historyList) {

            if (shape.isVisible) {
                drawPaint!!.color = Color.BLUE
                canvas.drawCircle(
                    shape.x.toFloat(), shape.y.toFloat(),
                    RADIUS.toFloat(),
                    drawPaint!!
                )
                drawPaint!!.color = Color.RED
                canvas.drawText(
                    "${shape.col}",
                    shape.x.toFloat(),
                    shape.y.toFloat() + 5f,
                    colTextPaint!!
                )

                if(rows[shape.row] == shape.col) {
                    canvas.drawText(
                        "Ряд${shape.row}",
                        (shape.x - 100).toFloat(),
                        shape.y.toFloat(),
                        textPaint!!
                    )
//                    if(shape.row == 32 && shape.col == 28){
//                        canvas.translate((shape.x - 100).toFloat(), shape.y.toFloat())
//                    }
                }else if(shape.col == 1)
                    canvas.drawText(
                        "Ряд${shape.row}",
                        (shape.x + 100).toFloat(),
                        shape.y.toFloat(),
                        textPaint!!
                    )
            }
        }



        canvas.apply {
            save()
            scale(scaleFactor, scaleFactor)
            // onDraw() code goes here
            restore()
        }
    }


    override fun onScale(detector: ScaleGestureDetector?): Boolean {
        scaleFactor *= detector!!.scaleFactor + 0.1f
        //scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f))
        invalidate()
        return true
    }

    override fun onScaleBegin(p0: ScaleGestureDetector?): Boolean {
       return true
    }

    override fun onScaleEnd(p0: ScaleGestureDetector?) {

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x: Float = event.x
        val y: Float = event.y

//        when (e.action) {
//            MotionEvent.ACTION_DOWN -> {
//                _prevx = e.getX()
//                _prevy = e.getY()
//            }
//            MotionEvent.ACTION_UP -> {
//                _xoffset += e.getX() - _prevx
//                _yoffset += e.getY() - _prevy
//                invalidate()
//                _prevx = e.getX()
//                _prevy = e.getY()
//            }
//        }
//        detector.onTouchEvent(e)
//        return true

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                for (shape in historyList) {
//                    if (shape.rect!!.contains(x.toInt(), y.toInt())) {
//                        Log.d(LOG_TAG, " ACTION_DOWN shape=" + shape.col.toString())
//                    }
                }

                // Might not be necessary; check out later
                dragging = true
                // We want to store the coords of the user's finger as it is before they move
                //  in order to calculate dx and dy
//                if (x < 2000) {
                    initX = x
                    initY = y
//                }
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
                    if (scaleFactor > 0.5) {
                        canvasX += dx / 5 //scaleFactor
                        canvasY += dy / 5 //scaleFactor

//                        if(initX < 500 && initY < 900){
                        invalidate() // Re-draw the canvas
                        // Change initX and initY to the new x- and y-coords
//                        initX = x
//                        initY = y
//                        }

                        Log.d(
                            LOG_TAG,
                            "ACTION_MOVE dx=$dx dy=$dy initX=$initX initY=$initY canvasX=$canvasX canvasY=$canvasY"
                        )
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
        Log.d(
            "TOUCHEVENT", "x: $x, y: $y,\ninitY: $initX, initY,\n" +
                    "canvasX: $canvasX, canvasY: $canvasY,\nwidth: $dispWidth, height: $dispHeight\n" +
                    "focusX: ${detector.focusX}, focusY: ${detector.focusY}"
        )
        // Data pertaining to fingers for responsiveness and stuff
        Log.d("TOUCHEVENT", "Action: ${event.action and MotionEvent.ACTION_MASK}\n")

        return true
    }


    fun initHistoryList(list: ArrayList<SeatShape>) {
        this.historyList = list
        this.rows = mutableMapOf<Int, Int>()
        for (shape in historyList) {

            if (rows.containsKey(shape.row)) {
                if (shape.col > rows[shape.row]!!) {
                    rows[shape.row] = shape.col
                }
            } else {
                rows[shape.row] = shape.col
            }
        }
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

//    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
//        override fun onScale(detector: ScaleGestureDetector): Boolean {
//            // Self-explanatory
//            scaleFactor *= detector.scaleFactor
//            // If scaleFactor is less than 0.5x, default to 0.5x as a minimum. Likewise, if
//            //  scaleFactor is greater than 10x, default to 10x zoom as a maximum.
//            scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM))
//
//            invalidate() // Re-draw the canvas
//
//            return true
//        }
//    }

    fun drawText(row: String, x: Float, y: Float){
        val paint = Paint()
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        canvas!!.drawPaint(paint)

        paint.color = Color.BLACK
        paint.textSize = 20f
        canvas!!.drawText(row, x, y, paint)
    }

    fun paintRowText(seats: MutableList<SeatModel>){
        var rows = mutableMapOf<Int, Int>()

        seats.forEach {
            if (rows.containsKey(it.row)){
                if (it.col < rows[it.row]!!){
                    rows[it.row] = it.col
                }
            }else{
                rows[it.row] = it.col
            }
        }
    }



}