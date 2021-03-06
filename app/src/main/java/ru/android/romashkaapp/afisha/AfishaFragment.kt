package ru.android.romashkaapp.afisha

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_afisha.*
import ru.android.romashkaapp.R
import ru.android.romashkaapp.afisha.adapters.MatchesAdapter
import ru.android.romashkaapp.afisha.adapters.ServicesAdapter
import ru.android.romashkaapp.databinding.FragmentAfishaBinding
import ru.android.romashkaapp.main.MainViewModel
import ru.android.romashkaapp.stadium.StadiumFragment.Companion.CHAMPIONSHIP_TITLE
import ru.android.romashkaapp.stadium.StadiumFragment.Companion.EVENT_ID

/**
 * Created by yasina on 02.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class AfishaFragment : Fragment() {

    lateinit var binding: FragmentAfishaBinding
    private val viewModel: AfishaViewModel by viewModels()
    private lateinit var matchesAdapter: MatchesAdapter
    private val servicesAdapter = ServicesAdapter()
    private lateinit var mainViewModel: MainViewModel

    fun setViewModel(viewModel: MainViewModel){
        this.mainViewModel = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_afisha, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            binding.visibilityOfEmptyAfisha = GONE
            matchesAdapter = MatchesAdapter(viewModel!!.getListener())
            rv_matches.adapter = matchesAdapter
            rv_matches.layoutManager = LinearLayoutManager(context)
            rv_services.adapter = servicesAdapter
            rv_services.layoutManager = LinearLayoutManager(context)
        }

        viewModel.matchesList.observe(viewLifecycleOwner, {
            binding.visibilityOfProgressBar = GONE
            if(it != null) {
                binding.visibilityOfAfisha = VISIBLE
                binding.visibilityOfEmptyAfisha = GONE
                if (it.size > 4)
                    mb_view_all_matches.visibility = VISIBLE
                matchesAdapter.updateList(it)
            }else{
                binding.visibilityOfEmptyAfisha = VISIBLE
                binding.visibilityOfAfisha = GONE
            }
        })
        viewModel.servicesList.observe(viewLifecycleOwner, {
            servicesAdapter.updateList(it)
        })
        viewModel.viewAllClick.observe(viewLifecycleOwner, {
            mainViewModel.setMatchesFragment()
        })

        viewModel.nextFragmentOpenClick.observe(viewLifecycleOwner, {
            mainViewModel.setStadiumFragment(bundleOf(EVENT_ID to it!!.event.id, CHAMPIONSHIP_TITLE to it.nomTitle))
//            findNavController().navigate(
//                R.id.nav_stadium,
//                bundleOf(EVENT_ID to it!!.event.id, CHAMPIONSHIP_TITLE to it.nomTitle),
//                NavOptions.Builder().setPopUpTo(
//                    R.id.nav_main,
//                    true
//                ).build()
//            )
        })
        viewModel.getEvents()
    }

}