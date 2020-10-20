package ru.android.romashkaapp.boarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.android.romashkaapp.R
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_onboarding.*
import ru.android.romashkaapp.boarding.item.ItemBoardingFragment
import ru.android.romashkaapp.databinding.FragmentOnboardingBinding
import ru.android.romashkaapp.databinding.ItemOnboardingBinding

/**
 * Created by yasina on 20.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class BoardingFragment : Fragment(){

    lateinit var binding: FragmentOnboardingBinding
    private val boardingViewModel: BoardingViewModel by viewModels()
    private lateinit var pagerAdapter: BoardingFragmentPageAdapter
    private var pageListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            tv_current_page.text = "0" + (position + 1).toString()
        }
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pagerAdapter = BoardingFragmentPageAdapter(requireActivity())
        var list = arrayListOf<Fragment>()
        for(i in 0..5){
            val fragment = ItemBoardingFragment()
            list.add(fragment)
        }
        pagerAdapter.setItems(list)
        view_pager.adapter = pagerAdapter
        view_pager.currentItem = 0
        view_pager.registerOnPageChangeCallback(pageListener)

    }
}