package ru.android.romashkaapp.basket.adapters

import android.content.Context
import android.ru.romashkaapp.models.CartModel
import android.ru.romashkaapp.models.EventModel
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import ru.android.romashkaapp.R
import ru.android.romashkaapp.BR
import ru.android.romashkaapp.basket.ItemClickListener
import ru.android.romashkaapp.utils.parseTimeStamp
import ru.android.romashkaapp.utils.removeZero


/**
 * Created by yasina on 18.12.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
open class SeatAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mContext: Context

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ViewDataBinding? = DataBindingUtil.bind(view)
    }

    private var list: MutableList<CartModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cart, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {

            holder.bindingItem?.setVariable(BR.item, list[position])
            holder.bindingItem?.setVariable(
                BR.price,
                String.format(
                    mContext.getString(R.string.rub),
                    list[position].price.removeZero()
                )
            )

            if(list.size == 1 || list[position] == list.last()){
                holder.bindingItem?.setVariable(BR.line, GONE)
            }
        }
    }

    open fun updateList(list: MutableList<CartModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
