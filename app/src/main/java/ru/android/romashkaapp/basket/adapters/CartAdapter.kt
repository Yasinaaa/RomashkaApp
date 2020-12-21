package ru.android.romashkaapp.basket.adapters

import android.content.Context
import android.ru.romashkaapp.models.CartModel
import android.ru.romashkaapp.models.EventModel
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import ru.android.romashkaapp.R
import ru.android.romashkaapp.BR
import ru.android.romashkaapp.basket.ItemClickListener
import ru.android.romashkaapp.databinding.ItemEventWithCartBinding
import ru.android.romashkaapp.sector_seat.adapter.CartBottomBarAdapter
import ru.android.romashkaapp.utils.parseTimeStamp


/**
 * Created by yasina on 18.12.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
open class CartAdapter(var listener: ItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mContext: Context

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemEventWithCartBinding? = DataBindingUtil.bind(view)
    }

    private var list: MutableList<OrderEventWithSeats> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_event_with_cart, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            parseName(list[position])
            setDate(list[position])
            setRivalImage(holder, list[position].event!!.thumbnail)

            val adapter = SeatAdapter()
            adapter.updateList(list[position].seats)
            holder.bindingItem?.rvSeats!!.adapter = adapter
            holder.bindingItem.rvSeats.layoutManager = LinearLayoutManager(mContext)


            holder.bindingItem.setVariable(BR.match, list[position])
            holder.bindingItem.executePendingBindings()
            holder.bindingItem.root.setOnClickListener { listener.click(list[position]) }
        }
    }

    open fun updateList(list: MutableList<OrderEventWithSeats>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun setDate(match: OrderEventWithSeats){
        match.date = match.event!!.sdate.parseTimeStamp()
    }

    private fun setRivalImage(holder: ItemViewHolder, thumbnail: String?){
        var circleImage = holder.bindingItem!!.root.findViewById(R.id.civ_logo2) as ShapeableImageView
        if(!thumbnail.isNullOrEmpty()){
            var image = thumbnail
            if (thumbnail.contains("data:")){
                val list = thumbnail.split(",", limit = 2)
                image = list[1]
            }
            val imageByteArray: ByteArray = Base64.decode(image, Base64.DEFAULT)
            Glide.with(mContext)
                .load(imageByteArray)
                .centerInside()
                .into(circleImage)
        }else{
            Glide.with(mContext)
                .load(ContextCompat.getDrawable(mContext, R.drawable.ic_soccer))
                .centerInside()
                .into(circleImage)
        }
    }

    private fun parseName(match: OrderEventWithSeats){
        if(match.event != null) {
            if (match.event!!.name != null) {
                when {
                    match.event!!.name!!.contains(" - ") -> {
                        splitText(match, " - ")
                    }
                    match.event!!.name!!.contains("-") -> {
                        splitText(match, "-")
                    }
                    else -> {
                        match.firstLine = match.event!!.name
                    }
                }
            } else {
                match.firstLine = mContext.getString(R.string.no_event_title)
            }
        }
    }

    private fun splitText(match: OrderEventWithSeats, condition: String){
        var lines = match.event!!.name!!.split(condition, limit = 2)
        if(lines.isNotEmpty()) {
            match.firstLine = lines[0]
            if (lines.size >= 2) {
                match.secondLine = lines[1]
            }
        }
    }

    class OrderEventWithSeats{
        var seats: MutableList<CartModel> = mutableListOf()
        var event: EventModel? = null
        var nomTitle: String? = null
        var location: String? = null
        var firstLine: String? = null
        var secondLine: String? = null
        var date: String? = null
    }
}
