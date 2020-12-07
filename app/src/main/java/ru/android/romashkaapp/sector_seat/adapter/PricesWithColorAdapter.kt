package ru.android.romashkaapp.sector_seat.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.ru.romashkaapp.models.ZoneModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import ru.android.romashkaapp.BR
import ru.android.romashkaapp.R
import ru.android.romashkaapp.databinding.ItemCalendarBinding
import ru.android.romashkaapp.databinding.ItemPriceWithColorBinding
import ru.android.romashkaapp.stadium.ItemClickListener


/**
 * Created by yasina on 11.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */

class PricesWithColorAdapter(l: ItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_PRICE = 0
        const val VIEW_TYPE_NOT_SELECTED = 1
    }

    var listener: ItemClickListener? = l

    class PriceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemPriceWithColorBinding? = DataBindingUtil.bind(view)
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
                        .inflate(R.layout.item_price_with_color, parent, false)
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
            holder.binding?.mbPrice!!.iconTint = ColorStateList.valueOf(Color.parseColor(list[position]!!.color))

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

//    @BindingAdapter("app:image_color")
//    fun loadColor(mb: MaterialButton, color: String?) {
//        if(color != null)
//            mb.iconTint = ColorStateList.valueOf(Color.parseColor(color))
//    }

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
