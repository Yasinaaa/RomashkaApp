package android.ru.romashkaapp.models

import android.ru.romashkaapp.models.enums.EventType

/**
 * Created by yasina on 25.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
class EventModel {

    var id: Int = 0
    var name: String? = null
    var active: Int? = null
    var nom_id: Int = 0
    var hall_id: Int = 0
    var sdate: String? = null
    var edate: String? = null
    var thumbnail: String? = null
    var description: String? = null
    var last: String? = null
    var type: EventType? = null
}
