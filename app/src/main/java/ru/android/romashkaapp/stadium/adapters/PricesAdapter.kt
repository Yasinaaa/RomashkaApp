package ru.android.romashkaapp.stadium.adapters

import android.content.Context
import android.ru.romashkaapp.models.EventModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.android.romashkaapp.R
import ru.android.romashkaapp.databinding.*
import ru.android.romashkaapp.matches.ItemClickListener
import ru.android.romashkaapp.utils.toDp

/**
 * Created by yasina on 11.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class PricesAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder> {

    var listener: ItemClickListener? = null

    constructor(l: ItemClickListener){
        listener = l
    }

    class PriceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemPriceBinding? = DataBindingUtil.bind(view)
    }

    private lateinit var context: Context
    private var list: MutableList<EventModel?> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_price, parent, false)
        return PriceViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PriceViewHolder) {
//            holder.binding?.setVariable(BR.email, list!![position])
//            holder.binding?.executePendingBindings()
//
//            if(position == list.size - 1){
//                var lp: RecyclerView.LayoutParams = holder.binding?.clMainGameCard?.layoutParams as RecyclerView.LayoutParams
//                lp.bottomMargin = 24.toDp(context)
//            }
            holder.binding?.root?.setOnClickListener { listener!!.click(list[position]) }
        }
    }

    fun updateList(list: MutableList<EventModel?>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
