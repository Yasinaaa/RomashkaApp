package android.ru.romashkaapp.models

/**
 * Created by yasina on 12.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class PointModel {

    var width: Int? = null
    var height: Int? = null
    var points: MutableList<Point> = mutableListOf()

    class Point{
        var x: Int? = null
        var y: Int? = null
        var sid: String? = null
        var kind: String? = null
    }

}
