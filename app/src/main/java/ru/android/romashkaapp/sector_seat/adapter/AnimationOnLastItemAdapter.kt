package ru.android.romashkaapp.sector_seat.adapter

import android.ru.romashkaapp.models.CartModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import ru.android.romashkaapp.R
import ru.android.romashkaapp.adapter.SwipeRemoveItemAdapter

/**
 * Created by yasina on 29.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class AnimationOnLastItemAdapter : SwipeRemoveItemAdapter() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ViewDataBinding? = DataBindingUtil.bind(view)
    }

    private var list: MutableList<CartModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_FOOTER -> createFooterViewHolder(parent.context)
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_cart, parent, false)
                return ItemViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
//            holder.binding?.setVariable(BR.email, list!![position])
            holder.binding?.executePendingBindings()
        }
    }

    fun updateList(list: MutableList<CartModel>) {
        this.list = list
        listSize = list.size
        notifyDataSetChanged()
    }
}
