package ru.android.romashkaapp.matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_matches.*
import kotlinx.android.synthetic.main.fragment_matches.cl_bottomsheet

import ru.android.romashkaapp.R
import ru.android.romashkaapp.base.BaseFragment
import ru.android.romashkaapp.databinding.FragmentMatchesBinding
import ru.android.romashkaapp.main.MainViewModel
import ru.android.romashkaapp.matches.adapters.MatchesAndCalendarAdapter
import ru.android.romashkaapp.stadium.StadiumFragment
import ru.android.romashkaapp.stadium.StadiumFragment.Companion.EVENT_ID
import ru.android.romashkaapp.utils.hideKeyboard

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class MatchesFragment : BaseFragment(){

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
            if(it != null) {
                if (it[0] == null) {
                    tv_matches.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.button_next_color
                        )
                    )
                    tv_calendar.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            android.R.color.black
                        )
                    )
                    mb_info.visibility = GONE
                    cl_toolbar.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), android.R.color.white)
                } else {
                    tv_calendar.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.button_next_color
                        )
                    )
                    tv_matches.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            android.R.color.black
                        )
                    )
                    mb_info.visibility = VISIBLE
                    cl_toolbar.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.background)
                }

                if (it[0] == null) {
                    matchesAdapter!!.updateMatchesList(it)
                } else
                    matchesAdapter!!.updateList(it)

                pb.visibility = GONE
            }else{
                cl_events_not_found.visibility = VISIBLE

            }
        })

        viewModel.nextFragmentOpenClick.observe(viewLifecycleOwner, {
            mainViewModel.setStadiumFragment(bundleOf(EVENT_ID to it!!.event.id, StadiumFragment.CHAMPIONSHIP_TITLE to it.nomTitle))
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

        mb_info.setOnClickListener {
            cl_bottomsheet.visibility = VISIBLE
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
            mainViewModel.hideNavigationBar()
        }

        ib_back.setOnClickListener {
            binding.root.hideKeyboard(requireContext())
            parentFragmentManager.popBackStackImmediate()
        }
    }

}