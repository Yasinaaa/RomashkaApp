package ru.android.romashkaapp.boarding.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.item_onboarding.*
import ru.android.romashkaapp.R
import ru.android.romashkaapp.databinding.ItemOnboardingBinding

/**
 * Created by yasina on 20.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class ItemBoardingFragment : Fragment(){

    companion object{
        val BOARDING_PAGE_TITLE: String = "BOARDING_PAGE_TITLE"
        val BOARDING_PAGE_TEXT: String = "BOARDING_PAGE_TEXT"
        val BOARDING_PAGE_IMAGE: String = "BOARDING_PAGE_IMAGE"
        val BOARDING_PAGE_BUTTON: String = "BOARDING_PAGE_BUTTON"
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf { it.containsKey(BOARDING_PAGE_TITLE) }?.apply {
            tv_title.text = getString(BOARDING_PAGE_TITLE, "")
            tv_text.text = getString(BOARDING_PAGE_TEXT, "")
        }
    }
}