package ru.android.romashkaapp.stadium

import android.app.Dialog
import android.os.Bundle
import android.ru.romashkaapp.models.EventModel
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.android.romashkaapp.R
import ru.android.romashkaapp.databinding.DialogPricesBinding
import ru.android.romashkaapp.stadium.adapters.PricesAdapter


/**
 * Created by yasina on 11.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class PriceFragment: DialogFragment() {

    private var pricesAdapter: PricesAdapter? = null
    private lateinit var mainViewModel: StadiumViewModel
    var binding: DialogPricesBinding? = null

    fun setViewModel(viewModel: StadiumViewModel){
        this.mainViewModel = viewModel
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_prices, null, false
        )
        builder = builder.setView(binding!!.root)
        binding!!.rvPrices.layoutManager = LinearLayoutManager(context)

        val pricesAdapter = PricesAdapter(mainViewModel.getListener())
        binding!!.rvPrices.adapter = pricesAdapter

//        pricesAdapter.updateList(arrayListOf(EventModel(), EventModel(), EventModel(), EventModel(), EventModel()))
        return builder.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        binding!!.rvPrices.layoutManager = LinearLayoutManager(context)
//
//        val pricesAdapter = PricesAdapter(mainViewModel.getListener())
//        binding!!.rvPrices.adapter = pricesAdapter
//
//        pricesAdapter.updateList(arrayListOf(EventModel(), EventModel(), EventModel(), EventModel(), EventModel()))

//        mainViewModel.pricesList.observe(viewLifecycleOwner, {
//            pricesAdapter!!.updateList(it)
//        })

    }
}