package ru.android.romashkaapp.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.android.romashkaapp.R
import ru.android.romashkaapp.databinding.FragmentBasketBinding
import ru.android.romashkaapp.basket.BasketViewModel
import ru.android.romashkaapp.main.MainViewModel

/**
 * Created by yasina on 05.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class BasketFragment : Fragment() {

    lateinit var binding: FragmentBasketBinding
    private val viewModel: BasketViewModel by viewModels()
    private lateinit var mainViewModel: MainViewModel

    fun setViewModel(viewModel: MainViewModel) {
        this.mainViewModel = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_basket, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}