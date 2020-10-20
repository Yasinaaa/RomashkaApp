package ru.android.romashkaapp.boarding.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.android.romashkaapp.R
import ru.android.romashkaapp.databinding.ItemOnboardingBinding

/**
 * Created by yasina on 20.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class ItemBoardingFragment : Fragment(){

    lateinit var binding: ItemOnboardingBinding
    private val itemBoardingViewModel: ItemBoardingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.item_onboarding, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = itemBoardingViewModel
        binding.executePendingBindings()

        return binding.root
    }
}