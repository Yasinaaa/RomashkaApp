package ru.android.romashkaapp.matches

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_matches.*
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

    fun setViewModel(viewModel: MainViewModel){
        this.mainViewModel = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            }else{
                tv_calendar.setTextColor(ContextCompat.getColor(requireContext(), R.color.button_next_color))
                tv_matches.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
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
    }

}