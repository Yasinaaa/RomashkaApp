package android.ru.romashkaapp.models

/**
 * Created by yasina on 26.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
class CartModel {

    var id: Int = 0
    lateinit var row: String
    lateinit var col: String
    lateinit var sector: String
    lateinit var tribune: String
    var price: Float = 0f
    var discount: Int = 0
    var commission: Int = 0
    var eventId: Int = 0
}
