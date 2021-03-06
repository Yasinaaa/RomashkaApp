package android.ru.romashkaapp.models.enums

/**
 * Created by yasina on 12.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
enum class EventType(var data: Int) {
    VAGRANT(1),
    MAIN_SPLIT(2),
    CHILD_SPLIT(3),
    SUBSCRIPTION(4),
    SEASON(5),
    PACKAGE(6)
}