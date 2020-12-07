package ru.android.romashkaapp.sector_seat.sector

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.ru.romashkaapp.models.SeatModel
import android.util.AttributeSet
import android.view.View

/**
 * Created by yasina on 07.12.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class SectorCanvas @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    private var seats: MutableList<SeatModel> = mutableListOf()

    fun setSeat(seats: MutableList<SeatModel>){
        this.seats = seats
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var paint = Paint().apply {
            isAntiAlias = true
            color = Color.RED
            style = Paint.Style.STROKE
        }
        seats.forEach {
            canvas!!.drawCircle(it.x!!.toFloat(), it.y!!.toFloat(), 2f, paint)
        }

    }
}