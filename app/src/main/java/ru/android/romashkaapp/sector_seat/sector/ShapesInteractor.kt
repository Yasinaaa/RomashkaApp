package ru.android.romashkaapp.sector_seat.sector

import android.content.Context
import android.content.DialogInterface

import android.util.Log
import java.util.*
import kotlin.math.roundToInt


/**
 * Handles business logic of creation , transformation  and deletion of shape
 */
class ShapesInteractor private constructor() {

    private var mContext: Context? = null
    private var canvas: SectorCanvas? = null
    var maxX = 0
    var maxY = 0
    private var actionSequence = 0

    private fun deleteShape(oldShape: SeatShape, i: Int) {
        oldShape.setVisibility(false)
        oldShape.actionNumber = actionSequence++
        historyList[i] = oldShape
        Log.d(LOG_TAG, "askForDeleteShape =  " + oldShape.type)
        canvas!!.initHistoryList(historyList)
        canvas!!.invalidate()
    }

    fun changeShapeOnTouch(x: Float, y: Float, changeStatus: Int) {
        val touchX = x.roundToInt()
        val touchY = y.roundToInt()
        //   Toast.makeText(this.getContext(), " Touch at " + touchX + " y= " + touchY, Toast.LENGTH_SHORT).show();
        var oldX: Int
        var oldY: Int
        val list: ArrayList<SeatShape> = historyList
        val newShape: SeatShape? = null
        //Traverse from end so that we find the last performed action or shape first.
        for (i in list.indices.reversed()) {
            val oldShape: SeatShape = list[i]
            if (oldShape.isVisible) {
                oldX = oldShape.x
                oldY = oldShape.y

                //Find an existing shape where the user has clicked on the canvas
//                if (25 >= calculateDistanceBetweenPoints(
//                        oldX.toDouble(),
//                        oldY.toDouble(),
//                        touchX.toDouble(),
//                        touchY.toDouble()
//                    )
//                ) {
//                    if (changeStatus == 12) addTransformShape(
//                        oldShape,
//                        i,
//                        oldX,
//                        oldY
//                    )
//                    break
//                }
            }
        }
    }

//    private fun addTransformShape(oldShape: SeatShape, index: Int, newX: Int, newY: Int) {
//        Log.d(LOG_TAG, " oldShape =  " + oldShape.type)
//        oldShape.setVisibility(false)
//        historyList[index] = oldShape
//        Log.d(LOG_TAG, " HIDE oldShape =  " + oldShape.type)
//
//        //transform object , rotate into available objects
//        val newShapeType: Int =
//            (oldShape.type!!.value + 1) % 3
//        val newshapeType: SeatShape.Type = SeatShape.Type.values().get(newShapeType)
//        Log.d(LOG_TAG, " newshape =  $newshapeType")
//
//        val newShape: SeatShape = SeatShape(newX, newY, 50, oldShape.title)
//        newShape.type = SeatShape.Type.CIRCLE
//        newShape.lastTranformationIndex = index
//        upDateCanvas(newShape)
//    }

    fun calculateDistanceBetweenPoints(
        x1: Double,
        y1: Double,
        x2: Double,
        y2: Double
    ): Double {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1))
    }

    fun undo() {
        if (historyList.size > 0) {
            actionSequence--
            val toDeleteShape: SeatShape = historyList.last()
            if (toDeleteShape.lastTranformationIndex !== -1) {
                val lastVisibleIndex: Int = toDeleteShape.lastTranformationIndex
                if (lastVisibleIndex < historyList.size) {
                    val lastVisibleShape: SeatShape = historyList[lastVisibleIndex]
                    if (lastVisibleShape != null) {
                        lastVisibleShape.setVisibility(true)
                        historyList[lastVisibleIndex] = lastVisibleShape
                    }
                }
            }
            historyList.removeLast()
            canvas!!.initHistoryList(historyList)
            canvas!!.invalidate()
        }
    }

    fun upDateCanvas(shape: SeatShape) {
        Log.d(
            LOG_TAG,
            " upDateCanvas " + shape.type.toString() + " actiontype = " + actionSequence
        )
        shape.actionNumber = actionSequence++
        historyList.add(shape)
        canvas!!.initHistoryList(historyList)
        canvas!!.invalidate()
    }

    /*
   Remove all items of a shape
    */
    fun deleteAllByShape(shapeType: SeatShape.Type) {
        val itr: MutableIterator<SeatShape> = historyList.iterator()
        while (itr.hasNext()) {
            val shape: SeatShape = itr.next()
            if (shape.type === shapeType) {
                itr.remove()
            }
        }
    }

    /*
    Get all items in list , grouped by shape
     */
    val countByGroup: HashMap<SeatShape.Type, Int>
        get() {
            val shapeTypeCountMap: HashMap<SeatShape.Type, Int> = HashMap<SeatShape.Type, Int>()
            for (shape in historyList) {
                if (shape.isVisible) {
                    val shapeType: SeatShape.Type = shape.type!!
                    var existingCnt = 0
                    if (shapeTypeCountMap.containsKey(shape.type)) existingCnt =
                        shapeTypeCountMap[shape.type]!!
                    existingCnt++
                    shapeTypeCountMap[shapeType] = existingCnt
                }
            }
            return shapeTypeCountMap
        }

    fun getCanvas(): SectorCanvas? {
        return canvas
    }

    fun setCanvas(canvas: SectorCanvas?) {
        this.canvas = canvas
    }

    fun getmContext(): Context? {
        return mContext
    }

    fun setContext(mContext: Context?) {
        this.mContext = mContext
    }

    companion object {
        val instance = ShapesInteractor()
        private const val LOG_TAG = ""

        /*
    Choose linkedlist (default doubly linkedlist in java ) as the data structure
     since we can add, transform, delete shapes very quickly in the same list without using extra memory
     */
        private val historyList: ArrayList<SeatShape> = ArrayList<SeatShape>()
    }
}