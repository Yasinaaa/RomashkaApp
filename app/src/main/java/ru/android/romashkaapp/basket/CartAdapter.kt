package ru.android.romashkaapp.basket

import android.content.Context
import android.ru.romashkaapp.models.CartModel
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import ru.android.romashkaapp.R


/**
 * Created by yasina on 18.12.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
open class CartAdapter(var listener: ItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mContext: Context

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ViewDataBinding? = DataBindingUtil.bind(view)
    }

    private var list: MutableList<CartModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_event_with_cart, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            //                parseName(list[position]!!)
//                setDate(list[position]!!)
//                setRivalImage(holder, list[position]!!.event.thumbnail)
//
//                holder.bindingItem?.setVariable(BR.match, list[position])
//                holder.bindingItem?.executePendingBindings()
//                holder.bindingItem?.root?.setOnClickListener { listener!!.click(list[position]!!) }

        }
    }


    open fun updateList(list: MutableList<CartModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

//    class Match{
//        lateinit var event: EventModel
//        var nomTitle: String? = null
//        var firstLine: String? = null
//        var secondLine: String? = null
//        var date: String? = null
//    }
}
