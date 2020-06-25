package android.ru.romashkaapp.models

/**
 * Created by yasina on 25.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
class EventModel {
    var id: Int = 0
    lateinit var startDate: String
    lateinit var endDate: String
    lateinit var unitId: String
    lateinit var hallId: String
    lateinit var name: String
    var isActive: Int = 0
    lateinit var adversaryLogo: String
    var freeSeats: Int = 0
    var allSeats: Int = 0
    var priceMin: Int = 0
    var priceMax: Int = 0
    var description: String? = null
    var sectorsCount: Int = 0
    var nomId: Int = 0
    lateinit var actions: MutableList<Int>
    lateinit var categories: MutableList<Int>
}
