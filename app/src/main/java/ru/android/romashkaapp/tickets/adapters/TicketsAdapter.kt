package ru.android.romashkaapp.tickets.adapters

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.ru.romashkaapp.models.CartModel
import android.ru.romashkaapp.models.EventModel
import android.util.Base64
import android.view.*
import android.widget.ImageView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.zxing.WriterException
import ru.android.romashkaapp.BR
import ru.android.romashkaapp.R
import ru.android.romashkaapp.tickets.ItemClickListener
import ru.android.romashkaapp.utils.getDay
import ru.android.romashkaapp.utils.getTime
import ru.android.romashkaapp.utils.removeZero
import java.time.Instant
import java.time.ZoneId
import java.util.*


/**
 * Created by yasina on 02.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
open class TicketsAdapter(var listener: ItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mContext: Context

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ViewDataBinding? = DataBindingUtil.bind(view)
    }

    private var list: MutableList<Ticket?> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ticket, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            if(list[position] != null){
                parseName(list[position]!!)
                setDate(list[position]!!)
                setPrice(list[position]!!)
                setRivalImage(holder, list[position]!!.event.thumbnail)

                list[position]!!.qrCode = list[position]!!.qrCode //"7254780087325"

                holder.bindingItem?.setVariable(BR.match, list[position])
                holder.bindingItem?.executePendingBindings()
                //holder.bindingItem?.root?.setOnClickListener { listener!!.click(list[position]!!) }

            }else{

            }
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("app:imageBitmap")
        open fun loadImage(iv: ImageView, img: String?) {
            if (img != null) {
                iv.setImageBitmap(createQrCode(img)!!)
            }
        }

        private fun createQrCode(text: String): Bitmap? {
            val qrgEncoder = QRGEncoder(text, null, QRGContents.Type.TEXT, 330)
            qrgEncoder.colorWhite = Color.TRANSPARENT
            return try {
                qrgEncoder.bitmap
            } catch (e: WriterException) {
                null
            }
        }
    }

    private fun setPrice(match: Ticket){
        match.price = String.format(mContext.getString(R.string.rub), match.cart.price.removeZero())
    }

    private fun setDate(match: Ticket){
        val stamp = Instant.ofEpochSecond(match.event.sdate!!.toLong())
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        listener.setYear(stamp.year.toString())
        match.day = match.event.sdate!!.getDay()
        match.time = match.event.sdate!!.getTime()
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

    private fun parseName(match: Ticket){
        if(match.event.name != null){
            when {
                match.event.name!!.contains(" - ") -> {
                    splitText(match, " - ")
                }
                match.event.name!!.contains("-") -> {
                    splitText(match, "-")
                }
                else -> {
                    match.firstLine = match.event.name
                }
            }
        }else{
            match.firstLine = mContext.getString(R.string.no_event_title)
        }
    }

    private fun splitText(match: Ticket, condition: String){
        var lines = match.event.name!!.split(condition, limit = 2)
        if(lines.isNotEmpty()) {
            match.firstLine = lines[0]
            if (lines.size >= 2) {
                match.secondLine = lines[1]
            }
        }
    }

    open fun updateList(list: MutableList<Ticket?>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Ticket{
        lateinit var cart: CartModel
        lateinit var event: EventModel
        var qrCode: String? = null
        var location: String? = null
        var nomTitle: String? = null
        var firstLine: String? = null
        var secondLine: String? = null
        var day: String? = null
        var time: String? = null
        var price: String? = null
    }
}
