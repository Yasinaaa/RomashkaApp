package ru.android.romashkaapp.afisha.adapters

import android.content.Context
import android.ru.romashkaapp.models.EventModel
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import ru.android.romashkaapp.BR
import ru.android.romashkaapp.R
import ru.android.romashkaapp.matches.ItemClickListener
import ru.android.romashkaapp.utils.parseTimeStamp


/**
 * Created by yasina on 02.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
open class MatchesAdapter(var listener: ItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected lateinit var mContext: Context

    companion object {
        const val VIEW_TEXT_TYPE = 0
        const val VIEW_TYPE_MATCH = 1
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ViewDataBinding? = DataBindingUtil.bind(view)
    }

    private var list: MutableList<Match?> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
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

                holder.bindingItem?.setVariable(BR.match, list[position])
                holder.bindingItem?.executePendingBindings()
                holder.bindingItem?.root?.setOnClickListener { listener!!.click(list[position]!!) }

            }else if(position == 1){

            }
        }
    }

    fun setDate(match: Match){
        match.date = match.event.sdate.parseTimeStamp()
    }

    fun setRivalImage(holder: ItemViewHolder, thumbnail: String?){
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

    fun parseName(match: Match){
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
        return if(position == 1 && list.size > 4) VIEW_TEXT_TYPE else VIEW_TYPE_MATCH
    }

    open fun updateList(list: MutableList<Match?>) {
        if(list.size > 4)
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
