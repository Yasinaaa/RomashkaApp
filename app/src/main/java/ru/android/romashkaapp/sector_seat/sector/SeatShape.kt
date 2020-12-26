package ru.android.romashkaapp.sector_seat.sector

import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import android.ru.romashkaapp.models.SeatModel

/**
 * Created by Mayuri Khinvasara on 01,December,2018
 */
public class SeatShape() : Parcelable {

    var x: Int = 0
    var y: Int = 0

    var width: Int = 0
    var type: Type? = null
    var isVisible = true
        private set
    var actionNumber = 0
    var lastTranformationIndex = -1
    var rect: Rect? = null

    var sid: String? = null
    var kind: String? = null
    var row: Int = 0
    var col: Int = 0
    var zoneId: String? = null

    constructor(parcel: Parcel) : this() {
        x = parcel.readInt()
        y = parcel.readInt()

        width = parcel.readInt()
        actionNumber = parcel.readInt()
        lastTranformationIndex = parcel.readInt()

        sid = parcel.readString()
        kind = parcel.readString()
        row = parcel.readInt()
        col = parcel.readInt()
        zoneId = parcel.readString()
    }

    constructor(seatModel: SeatModel) : this() {
//        if (seatModel.row != 32) {
            this.x = seatModel.x!!.toInt() * 5
            this.y = seatModel.y!!.toInt() * 5
//        }else if (seatModel.row != 31) {
//            this.x = seatModel.x!!.toInt() * 5
//            this.y = 40
//        }else{
//            this.x = seatModel.x!!.toInt() * 5
//            this.y = 20
//        }
        this.width = 10
        this.sid = seatModel.sid
        this.kind = seatModel.kind
        this.row = seatModel.row
        this.col = seatModel.col
        this.zoneId = seatModel.zone_id
    }

    /*
    Define all types of shapes
     */
    enum class Type private constructor(val value: Int) {
        CIRCLE(0), SQUARE(1), TRIANGLE(2)
    }

    fun setVisibility(visibility: Boolean) {
        this.isVisible = visibility
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(x)
        parcel.writeInt(y)

        parcel.writeInt(width)
        parcel.writeInt(actionNumber)
        parcel.writeInt(lastTranformationIndex)

        parcel.writeString(sid)
        parcel.writeString(kind)
        parcel.writeInt(row )
        parcel.writeInt(col)
        parcel.writeString(zoneId)
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
