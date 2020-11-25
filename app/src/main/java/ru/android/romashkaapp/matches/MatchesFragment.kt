package ru.android.romashkaapp.matches

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_basket.*
import kotlinx.android.synthetic.main.fragment_matches.*
import kotlinx.android.synthetic.main.fragment_matches.cl_bottomsheet

import ru.android.romashkaapp.R
import ru.android.romashkaapp.databinding.FragmentMatchesBinding
import ru.android.romashkaapp.main.MainViewModel
import ru.android.romashkaapp.matches.adapters.MatchesAndCalendarAdapter

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class MatchesFragment : Fragment(){

    lateinit var binding: FragmentMatchesBinding
    private val viewModel: MatchesViewModel by viewModels()
    private var matchesAdapter: MatchesAndCalendarAdapter? = null
    private lateinit var mainViewModel: MainViewModel
    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null

    fun setViewModel(viewModel: MainViewModel){
        this.mainViewModel = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_matches, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            matchesAdapter = MatchesAndCalendarAdapter(viewModel!!.getListener())
            rv_matches.adapter = matchesAdapter
            rv_matches.layoutManager = LinearLayoutManager(context)
        }

        viewModel.matchesList.observe(viewLifecycleOwner, {
            if(it[0] == null){
                tv_matches.setTextColor(ContextCompat.getColor(requireContext(), R.color.button_next_color))
                tv_calendar.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
                mb_info.visibility = GONE
                cl_toolbar.backgroundTintList = ContextCompat.getColorStateList(requireContext(), android.R.color.white)
            }else{
                tv_calendar.setTextColor(ContextCompat.getColor(requireContext(), R.color.button_next_color))
                tv_matches.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
                mb_info.visibility = VISIBLE
                cl_toolbar.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.background)
            }
            matchesAdapter!!.updateList(it)
            pb.visibility = GONE
        })

        viewModel.nextFragmentOpenClick.observe(viewLifecycleOwner, {
            findNavController().navigate(
                R.id.nav_stadium,
                null,
                NavOptions.Builder().setPopUpTo(
                    R.id.nav_main,
                    true
                ).build()
            )
        })

        bottomSheetBehavior = BottomSheetBehavior.from(cl_bottomsheet)
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
        cl_bottomsheet.visibility = GONE

        bottomSheetBehavior!!.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // handle onSlide
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        mainViewModel.showNavigationBar()
                        cl_bottomsheet.visibility = GONE
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {

                    }
                    BottomSheetBehavior.STATE_SETTLING -> {

                    }
                }
            }
        })

        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN

        mb_info.setOnClickListener {
            cl_bottomsheet.visibility = VISIBLE
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
            mainViewModel.skipNavigationBar()
        }

        ib_back.setOnClickListener {
            parentFragmentManager.popBackStackImmediate()
        }
    }

}