package ru.android.romashkaapp.boarding

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_onboarding.*
import ru.android.romashkaapp.R
import ru.android.romashkaapp.boarding.item.ItemBoardingFragment
import ru.android.romashkaapp.boarding.item.ItemBoardingFragment.Companion.BOARDING_PAGE_TEXT
import ru.android.romashkaapp.boarding.item.ItemBoardingFragment.Companion.BOARDING_PAGE_TITLE
import ru.android.romashkaapp.databinding.FragmentOnboardingBinding


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
            if(position == 3){
                mb_next.text = getString(R.string.last_boarding_btn_title)
                btn_skip.visibility = GONE

                val constraintSet = ConstraintSet()
                constraintSet.clone(cl)
                constraintSet.connect(
                    R.id.mb_next,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START,
                    0
                )
                constraintSet.applyTo(cl)
                setMargins(0, 0)
            }else{
                mb_next.text = getString(R.string.next)
                btn_skip.visibility = VISIBLE

                val constraintSet = ConstraintSet()
                constraintSet.clone(cl)
                constraintSet.clear(R.id.mb_next, ConstraintSet.START)
                constraintSet.applyTo(cl)
                setMargins(16.toDp(), 24.toDp())
            }
        }
    }

    fun Int.toDp():Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), requireContext().resources.displayMetrics
    ).toInt()

    private fun setMargins(leftMargin: Int, rightMargin: Int){
        val newLayoutParams = mb_next.layoutParams as ConstraintLayout.LayoutParams
        newLayoutParams.leftMargin = leftMargin
        newLayoutParams.rightMargin = rightMargin
        mb_next.layoutParams = newLayoutParams
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
        var boardingTitles = resources.getStringArray(R.array.boarding_titles)
        var boardingTexts = resources.getStringArray(R.array.boarding_texts)

        for(i in 0..3){
            val fragment = ItemBoardingFragment()
            fragment.arguments = bundleOf(
                BOARDING_PAGE_TITLE to boardingTitles[i],
                BOARDING_PAGE_TEXT to boardingTexts[i]
            )
            list.add(fragment)
        }
        pagerAdapter.setItems(list)
        view_pager.adapter = pagerAdapter
        view_pager.currentItem = 0
        view_pager.registerOnPageChangeCallback(pageListener)
        dots_indicator.setViewPager2(view_pager)

        boardingViewModel.nextClick.observe(viewLifecycleOwner, {
            if(mb_next.text == getString(R.string.last_boarding_btn_title)){
                findNavController().navigate(
                    R.id.nav_main,
                    null,
                    NavOptions.Builder().setPopUpTo(
                        R.id.nav_boarding,
                        true
                    ).build()
                )
            }else{
                view_pager.currentItem = view_pager.currentItem + 1
            }
        })
    }
}