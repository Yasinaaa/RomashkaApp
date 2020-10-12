package android.ru.romashkaapp.models

/**
 * Created by yasina on 26.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
class OrderModel {

    var id: Int = 0
    var user_id: Int = 0
    var amount: String? = null
    var commision: String? = null
    var default_amount: String? = null
    var open_date: String? = null
    var close_date: String? = null
    var type: Int = 0
    var status: Int = 0
    var carts: MutableList<CartModel>? = null
}
