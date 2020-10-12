package android.ru.romashkaapp.models

import android.ru.romashkaapp.models.enums.SeatStatusType

/**
 * Created by yasina on 12.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class SeatModel {

    var row: Int = 0
    var col: Int = 0
    var sector: Int = 0
    var name: String? = null
    var color: String? = null
    var price: String? = null
    var sid: String? = null
    var zone: String? = null
    var status: SeatStatusType? = null

}
