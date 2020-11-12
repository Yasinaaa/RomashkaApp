package ru.android.romashkaapp.afisha.adapters

import android.ru.romashkaapp.models.EventModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import ru.android.romashkaapp.R
import ru.android.romashkaapp.matches.ItemClickListener

/**
 * Created by yasina on 02.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class MatchesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    var listener: ItemClickListener? = null

    constructor(l: ItemClickListener){
        listener = l
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ViewDataBinding? = DataBindingUtil.bind(view)
    }

    private var list: MutableList<EventModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
//            holder.binding?.setVariable(BR.email, list!![position])
            holder.binding?.executePendingBindings()
            holder.binding?.root?.setOnClickListener { listener!!.click(list[position]) }
        }
    }

    fun updateList(list: MutableList<EventModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
