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

class PricesAdapter(l: ItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_PRICE = 0
        const val VIEW_TYPE_NOT_SELECTED = 1
    }

    var listener: ItemClickListener? = l

    class PriceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ViewDataBinding? = DataBindingUtil.bind(view)
    }

    class NotSelectedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ViewDataBinding? = DataBindingUtil.bind(view)
    }

    private lateinit var context: Context
    private var list: MutableList<ZoneModel?> = mutableListOf()

    init {
        setList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return when(viewType){
            VIEW_TYPE_PRICE -> {
                PriceViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.item_price, parent, false)
                )
            }
            else -> {
                NotSelectedViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.item_not_selected, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PriceViewHolder) {
            holder.binding?.setVariable(
                BR.text,
                String.format(
                    context.getString(R.string.rub),
                    list[position]!!.price
                )
            )
            holder.binding?.executePendingBindings()
            holder.binding?.root?.setOnClickListener {
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(list[position] != null) VIEW_TYPE_PRICE else VIEW_TYPE_NOT_SELECTED
    }

    fun updateList(list: MutableList<ZoneModel>) {
        this.list = mutableListOf()
        this.list.addAll(list)
        this.list.add(list.size, null)
        notifyDataSetChanged()
    }

    private fun setList() {
        this.list.add(0, null)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
