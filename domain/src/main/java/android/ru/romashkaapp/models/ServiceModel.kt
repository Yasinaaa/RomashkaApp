package android.ru.romashkaapp.models

/**
 * Created by yasina on 09.10.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
class ServiceModel {

    var id: Int = 0
    var name: String? = null
    var active: Boolean? = null
    var unit_id: Int = 0
    var thumbnail: String? = null
    var description: String? = null
    var prices: MutableList<ServicePriceModel>? = null
    var last: String? = null
}
