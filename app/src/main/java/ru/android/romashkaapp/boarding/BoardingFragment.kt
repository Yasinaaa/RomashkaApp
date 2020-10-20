package ru.android.romashkaapp.boarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.android.romashkaapp.R
import androidx.fragment.app.viewModels
import ru.android.romashkaapp.databinding.FragmentOnboardingBinding

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class BoardingFragment : Fragment(){

    lateinit var binding: FragmentOnboardingBinding
    private val boardingViewModel: BoardingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_onboarding, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = boardingViewModel
        binding.executePendingBindings()

        return binding.root
    }
}