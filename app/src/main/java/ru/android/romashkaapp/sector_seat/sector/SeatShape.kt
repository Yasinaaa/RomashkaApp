package ru.android.romashkaapp.sector_seat.sector

import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Mayuri Khinvasara on 01,December,2018
 */
public class SeatShape() : Parcelable {
    /*
     Defines centroid of shape : x,y.  Pivot about which shape has to be drawn
     */
    private var xCordinate: Int = 0
    private var yCordinate: Int = 0
    var title: String = ""
    var width: Int = 0
    var type: Type? = null
    var isVisible = true
        private set
    var actionNumber = 0
    var lastTranformationIndex = -1
    var rect: Rect? = null

    constructor(parcel: Parcel) : this() {
        xCordinate = parcel.readInt()
        yCordinate = parcel.readInt()
        title = parcel.readString()!!
        width = parcel.readInt()
        actionNumber = parcel.readInt()
        lastTranformationIndex = parcel.readInt()
    }

    constructor(x: Int, y: Int, width: Int, title: String) : this() {
        this.xCordinate = x
        this.yCordinate = y
        this.width = width
        this.title = title
    }

    /*
    Define all types of shapes
     */
    enum class Type private constructor(val value: Int) {
        CIRCLE(0), SQUARE(1), TRIANGLE(2)
    }

    fun getxCordinate(): Int {
        return xCordinate
    }

    fun setxCordinate(xCordinate: Int) {
        this.xCordinate = xCordinate
    }

    fun getyCordinate(): Int {
        return yCordinate
    }

    fun setyCordinate(yCordinate: Int) {
        this.yCordinate = yCordinate
    }

    fun setVisibility(visibility: Boolean) {
        this.isVisible = visibility
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(xCordinate)
        parcel.writeInt(yCordinate)
        parcel.writeString(title)
        parcel.writeInt(width)
        parcel.writeInt(actionNumber)
        parcel.writeInt(lastTranformationIndex)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SeatShape> {
        override fun createFromParcel(parcel: Parcel): SeatShape {
            return SeatShape(parcel)
        }

        override fun newArray(size: Int): Array<SeatShape?> {
            return arrayOfNulls(size)
        }
    }

}
