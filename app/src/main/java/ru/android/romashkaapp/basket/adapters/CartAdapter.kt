package ru.android.romashkaapp.basket.adapters

import android.content.Context
import android.graphics.drawable.Drawable
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
import com.bumptech.glide.RequestBuilder
import com.google.android.material.imageview.ShapeableImageView
import ru.android.romashkaapp.R
import ru.android.romashkaapp.BR
import ru.android.romashkaapp.basket.ItemClickListener
import ru.android.romashkaapp.databinding.ItemEventWithCartBinding
import ru.android.romashkaapp.databinding.ItemOrderTotalDataBinding
import ru.android.romashkaapp.utils.parseTimeStamp
import ru.android.romashkaapp.utils.removeZero
import ru.android.romashkaapp.utils.ticketsCount


/**
 * Created by yasina on 18.12.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
open class CartAdapter(var listener: ItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mContext: Context

    companion object {
        const val VIEW_TYPE_EVENT = 0
        const val VIEW_TYPE_TOTAL = 1
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemEventWithCartBinding? = DataBindingUtil.bind(view)
    }

    class TotalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemOrderTotalDataBinding? = DataBindingUtil.bind(view)
    }

    private var list: MutableList<OrderEventWithSeats?> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context

        return when(viewType) {
            VIEW_TYPE_EVENT -> {
                ItemViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_event_with_cart, parent, false)
                )
            }
            else -> {
                TotalViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_order_total_data, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            if (list[position] != null) {
                parseName(list[position]!!)
                setDate(list[position]!!)
                setRivalImage(holder, list[position]!!.event!!.thumbnail)

                val adapter = SeatAdapter()
                adapter.updateList(list[position]!!.seats)
                holder.bindingItem?.rvSeats!!.adapter = adapter
                holder.bindingItem.rvSeats.layoutManager = LinearLayoutManager(mContext)


                holder.bindingItem.setVariable(BR.match, list[position])
                holder.bindingItem.executePendingBindings()
            }
        }else if (holder is TotalViewHolder){
            var ticketCount = 0
            var ticketsTotalPrice = 0f
            var ticketsTotalCommission = 0f

            list.forEach {
                if(it != null) {
                    ticketCount += it.seats.size
                    it.seats.forEach { seat ->
                        ticketsTotalPrice += seat.price
                        ticketsTotalCommission += seat.commission
                    }
                }
            }.apply {
                holder.bindingItem!!.tvItemSeat.text = ticketCount.ticketsCount(mContext)
                holder.bindingItem.tvItemPrice.text = String.format(mContext.getString(R.string.rub), ticketsTotalPrice.removeZero())
                holder.bindingItem.tvOrderCommissionPrice.text = String.format(mContext.getString(R.string.rub), ticketsTotalCommission.removeZero())

                holder.bindingItem.tvOrderPromocode.setOnClickListener {
                    listener.onPromocodeClick()
                }
                holder.bindingItem.mbGPay.setOnClickListener {
                    listener.onGPayClick()
                }
                holder.bindingItem.mbGoToPay.setOnClickListener {
                    listener.onPayClick()
                }
            }
        }
    }

    open fun updateList(list: MutableList<OrderEventWithSeats?>) {
        this.list = mutableListOf()
        this.list.addAll(list)
        this.list.add(null)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun setDate(match: OrderEventWithSeats){
        match.date = match.event!!.sdate.parseTimeStamp()
    }

    private fun setRivalImage(holder: ItemViewHolder, thumbnail: String?){
        val circleImage = holder.bindingItem!!.root.findViewById(R.id.civ_logo2) as ShapeableImageView
        val glide = Glide.with(mContext)
        var requestBuilder: RequestBuilder<Drawable?>? = null

        if(!thumbnail.isNullOrEmpty()){
            var image = thumbnail
            if (thumbnail.contains("data:")){
                val list = thumbnail.split(",", limit = 2)
                image = list[1]
            }
            val imageByteArray: ByteArray = Base64.decode(image, Base64.DEFAULT)
            requestBuilder = glide.load(imageByteArray)
        }else{
            requestBuilder = glide.load(ContextCompat.getDrawable(mContext, R.drawable.ic_soccer))
        }

        if(requestBuilder != null)
            requestBuilder.centerInside().into(circleImage)
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
        val lines = match.event!!.name!!.split(condition, limit = 2)
        if(lines.isNotEmpty()) {
            match.firstLine = lines[0]
            if (lines.size >= 2) {
                match.secondLine = lines[1]
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position] == null) VIEW_TYPE_TOTAL else VIEW_TYPE_EVENT
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
