package android.ru.romashkaapp.models

/**
 * Created by yasina on 25.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
class SectorModel {

    var id: Int = 0
    lateinit var name: String
    var freeSeats: Int = 0
    var allSeats: Int = 0
    var priceMin: Int = 0
    var priceMax: Int = 0
    var sectorsCount: Int = 0
    var sectors: MutableList<Sectors>? = null

    class Sectors{
        var id: Int = 0
        lateinit var name: String
        var freeSeats: Int = 0
        var allSeats: Int = 0
        var priceMin: Int = 0
        var priceMax: Int = 0
        var sectorsCount: Int = 0
        var sectors: MutableList<Sectors>? = null
    }
}
