package ru.android.romashkaapp.matches

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.android.romashkaapp.main.MainViewModel

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class MatchesFragment : Fragment(){

    private lateinit var mainViewModel: MainViewModel

    fun setViewModel(viewModel: MainViewModel){
        this.mainViewModel = viewModel
    }

}