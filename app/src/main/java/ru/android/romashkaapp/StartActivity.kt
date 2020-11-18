package ru.android.romashkaapp

import android.os.Bundle
import android.ru.romashkaapp.data.net.repository.ApiRepository
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class StartActivity : AppCompatActivity(){

    companion object{
        val REPOSITORY = ApiRepository()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}