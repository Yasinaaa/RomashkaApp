package ru.android.romashkaapp.sector_seat.sector

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
import kotlin.math.pow

/**
 * Created by yasina on 07.12.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class SectorCanvas: View, ScaleGestureDetector.OnScaleGestureListener  {

    private var seats: MutableList<SeatModel> = mutableListOf()

    private val RADIUS: Int = 20
    private var canvas: Canvas? = null
    var historyList = ArrayList<SeatShape>()
    var rows = mutableMapOf<Int, Int>()
    private var initX: Float = 0f
    private var initY: Float = 0f
    private var canvasX: Float = 0f
    private var canvasY: Float = 0f
    private var dispWidth: Float = 100f
    private var dispHeight: Float = 100f
    private var dragging: Boolean = false
    private var firstDraw: Boolean = true
    private var detector: ScaleGestureDetector = ScaleGestureDetector(context, this)
    private var scaleFactor: Float = 1f
    private val MIN_ZOOM: Float = 1f
    private val MAX_ZOOM: Float = 4f

    constructor(context: Context?, @Nullable attrs: AttributeSet?): super(context, attrs) {
        isFocusable = true
        isFocusableInTouchMode = true
        setupPaint()
    }

    private var drawPaint: Paint? = null
    private var textPaint: Paint? = null
    private var colTextPaint: Paint? = null

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
            canvasX = 0f
            canvasY = 0f
            firstDraw = false
        }

        canvas.scale(scaleFactor, scaleFactor)
        canvas.translate(canvasX, canvasY)

        this.canvas!!.drawColor(ContextCompat.getColor(context, android.R.color.white))

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
        scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 2.0f))
        invalidate()
        return true
    }

    override fun onScaleBegin(p0: ScaleGestureDetector?): Boolean {
       return true
    }

    override fun onScaleEnd(p0: ScaleGestureDetector?) {

    }

    fun calculateDistanceBetweenPoints(
        x1: Double,
        y1: Double,
        x2: Double,
        y2: Double
    ): Double {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1))
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x: Float = event.x
        val y: Float = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dragging = true
                initX = x
                initY = y

                historyList.forEach {
                    val dx: Float = x - initX
                    val dy: Float = y - initY
                    canvasX += dx / scaleFactor
                    canvasY += dy / scaleFactor

                    var value = calculateDistanceBetweenPoints(
                        it.x.toDouble(), it.y.toDouble(),
                        Math.round(canvasX).toDouble(), Math.round(canvasY).toDouble()
                    )
                    if (RADIUS.toDouble().pow(2) >= value) {
                        Log.d("TOUCHEVENT", "Action: Column ${it.col} Row ${it.row}")
                    }
                }
            }

            MotionEvent.ACTION_MOVE -> {
                val dx: Float = x - initX
                val dy: Float = y - initY

                if (dragging) {
                    if (scaleFactor > 0.5) {
                        canvasX += dx / scaleFactor
                        canvasY += dy / scaleFactor
                        invalidate()
                        initX = x
                        initY = y
                    }
                }
            }

            MotionEvent.ACTION_POINTER_UP -> {
                initX = x
                initY = y
            }

            MotionEvent.ACTION_UP -> {
                dragging = false
            }
        }
        detector.onTouchEvent(event)
        return true
    }

    fun initHistoryList(list: ArrayList<SeatShape>) {
        this.historyList = list
        this.rows = mutableMapOf()
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

    fun setSeat(seats: MutableList<SeatModel>){
        this.seats = seats
    }

}