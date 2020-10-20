package ru.android.romashkaapp.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel

/**
 * Created by yasina on 20.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    val context = application.applicationContext!!

}