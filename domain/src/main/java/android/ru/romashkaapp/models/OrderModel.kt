package android.ru.romashkaapp.models

/**
 * Created by yasina on 26.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
class OrderModel {

    var id: Int = 0
    var user_id: Int = 0
    var count: Int? = null
    var status_id: Int = 0
    var default_amount: Int? = null
    var amount: Int? = null
    var bonuses: String? = null
    var commision: String? = null
    var creatred_at: String? = null
    var closed_at: String? = null
    var expired_at: String? = null

//    var carts: MutableList<CartModel>? = null
}
