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

class FullPricesAdapter(l: ItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listener: ItemClickListener? = l
    var selectedItems: MutableList<ZoneModel> = mutableListOf()

    class PriceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ViewDataBinding? = DataBindingUtil.bind(view)
    }

    private lateinit var context: Context
    private var list: MutableList<FullZone> = mutableListOf()

    class FullZone(var zone: ZoneModel){
        var free: String? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_filter, parent, false)
        return PriceViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PriceViewHolder) {
            if(isContainsInSelectedItems(list[position])){
                holder.binding?.setVariable(BR.isSelected, true)
            }else{
                holder.binding?.setVariable(BR.isSelected, false)
            }
            holder.binding?.setVariable(
                BR.text,
                String.format(
                    context.getString(R.string.zone_price),
                    list[position].zone.price,
                    list[position].free
                )
            ) //todo show free places
            holder.binding?.executePendingBindings()
//            if(position == list.size - 1){
//                var lp: RecyclerView.LayoutParams = holder.binding?.clMainGameCard?.layoutParams as RecyclerView.LayoutParams
//                lp.bottomMargin = 24.toDp(context)
//            }
            holder.binding?.root?.setOnClickListener {
                if(isContainsInSelectedItems(list[position])){
                    removeItemInSelectedItems(list[position])
                }else{
                    selectedItems.add(list[position].zone)
                }
                notifyDataSetChanged()
            }
        }
    }

    private fun isContainsInSelectedItems(model: FullZone): Boolean{
        selectedItems.forEach {
            if(it.id == model.zone.id){
                return true
            }
        }
        return false
    }

    private fun removeItemInSelectedItems(model: FullZone){
        selectedItems.forEachIndexed { index, zoneModel ->
            if(zoneModel.id == model.zone.id){
                selectedItems.removeAt(index)
                return
            }
        }
    }

    fun resetSelectedItems(){
        selectedItems = mutableListOf()
        notifyDataSetChanged()
    }

    fun updateList(list: MutableList<FullZone>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
