package ru.android.romashkaapp.afisha.adapters

import android.content.Context
import android.ru.romashkaapp.models.EventModel
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import ru.android.romashkaapp.BR
import ru.android.romashkaapp.R
import ru.android.romashkaapp.matches.ItemClickListener
import ru.android.romashkaapp.utils.parseTimeStamp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


/**
 * Created by yasina on 02.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class MatchesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private lateinit var context: Context
    var listener: ItemClickListener? = null

    constructor(l: ItemClickListener){
        listener = l
    }

    companion object {
        const val VIEW_TEXT_TYPE = 0
        const val VIEW_TYPE_MATCH = 1
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ViewDataBinding? = DataBindingUtil.bind(view)
    }

    private var list: MutableList<Match?> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return when(viewType) {
            VIEW_TEXT_TYPE -> {
                ItemViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_text_event, parent, false)
                )
            }
            else -> {
                ItemViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_event, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            if(position == 0 || position != 1){
                parseName(list[position]!!)
                setDate(list[position]!!)
                setRivalImage(holder, list[position]!!.event.thumbnail)

                holder.binding?.setVariable(BR.match, list[position])
                holder.binding?.executePendingBindings()
                holder.binding?.root?.setOnClickListener { listener!!.click(list[position]!!) }

            }else if(position == 1){

            }
        }
    }

    private fun setDate(match: Match){
        match.date = match.event.sdate.parseTimeStamp()
    }

    private fun setRivalImage(holder: ItemViewHolder, thumbnail: String?){
        var circleImage = holder.binding!!.root.findViewById(R.id.civ_logo2) as ShapeableImageView
        if(!thumbnail.isNullOrEmpty()){
            var image = thumbnail
            if (thumbnail.contains("data:")){
                val list = thumbnail.split(",", limit = 2)
                image = list[1]
            }
            val imageByteArray: ByteArray = Base64.decode(image, Base64.DEFAULT)
            Glide.with(context)
                .load(imageByteArray)
                .centerInside()
                .into(circleImage)
        }else{
            Glide.with(context)
                .load(context.getDrawable(R.drawable.ic_soccer))
                .centerInside()
                .into(circleImage)
        }
    }

    private fun parseName(match: Match){
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
            match.firstLine = context.getString(R.string.no_event_title)
        }
    }

    private fun splitText(match: Match, condition: String){
        var lines = match.event.name!!.split(condition, limit = 2)
        if(lines.isNotEmpty()) {
            match.firstLine = lines[0]
            if (lines.size >= 2) {
                match.secondLine = lines[1]
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 1) VIEW_TEXT_TYPE else VIEW_TYPE_MATCH
    }

    fun updateList(list: MutableList<Match?>) {
        list.add(1, null)
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Match{
        lateinit var event: EventModel
        var nomTitle: String? = null
        var firstLine: String? = null
        var secondLine: String? = null
        var date: String? = null
    }
}
