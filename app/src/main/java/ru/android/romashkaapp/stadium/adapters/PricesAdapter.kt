package ru.android.romashkaapp.stadium.adapters

import android.content.Context
import android.ru.romashkaapp.models.ZoneModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import ru.android.romashkaapp.R
import ru.android.romashkaapp.stadium.ItemClickListener
import ru.android.romashkaapp.BR

/**
 * Created by yasina on 11.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */

class PricesAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder> {

    var listener: ItemClickListener? = null
    var selectedItems: MutableList<ZoneModel> = mutableListOf()

    constructor(l: ItemClickListener){
        listener = l
    }

    class PriceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ViewDataBinding? = DataBindingUtil.bind(view)
    }

    private lateinit var context: Context
    private var list: MutableList<ZoneModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_filter, parent, false)
        return PriceViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PriceViewHolder) {
            if(selectedItems.contains(list[position])){
                holder.binding?.setVariable(BR.isSelected, true)
            }else{
                holder.binding?.setVariable(BR.isSelected, false)
            }
            holder.binding?.setVariable(
                BR.text,
                String.format(
                    context.getString(R.string.zone_price),
                    list[position].price,
                    list[position].all
                )
            ) //todo show free places
            holder.binding?.executePendingBindings()
//            if(position == list.size - 1){
//                var lp: RecyclerView.LayoutParams = holder.binding?.clMainGameCard?.layoutParams as RecyclerView.LayoutParams
//                lp.bottomMargin = 24.toDp(context)
//            }
            holder.binding?.root?.setOnClickListener {
                if(selectedItems.contains(list[position])){
                    selectedItems.remove(list[position])
                }else{
                    selectedItems.add(list[position])
                }
                notifyDataSetChanged()
            }
        }
    }

    fun resetSelectedItems(){
        selectedItems = mutableListOf()
        notifyDataSetChanged()
    }

    fun updateList(list: MutableList<ZoneModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
