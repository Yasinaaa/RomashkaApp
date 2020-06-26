package android.ru.romashkaapp.models

/**
 * Created by yasina on 26.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
class NomModel {

    var id: Int = 0
    lateinit var name: String
    lateinit var photo: String
    var actions: MutableList<Int>? = null
    var description: String? = null

}
