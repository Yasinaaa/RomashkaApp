package android.ru.romashkaapp.models

/**
 * Created by yasina on 26.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
class OrderModel {

    var id: Int = 0
    lateinit var opened_at: String
    lateinit var closed_at: String
    var status_id: Int = 0
    var carts: MutableList<CartModel>? = null
    var user_id: Int = 0
    var price: Float = 0f
    var discount: Int? = null
    var commision: Int? = null

}
