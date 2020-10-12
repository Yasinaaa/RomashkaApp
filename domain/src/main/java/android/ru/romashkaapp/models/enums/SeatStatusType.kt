package android.ru.romashkaapp.models.enums

/**
 * Created by yasina on 12.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
enum class SeatStatusType(var type: Int) {
    FREE(0),
    RESERVED_BY_OTHER_USER(2),
    RESERVED_BY_CURRENT_USER(3),
}