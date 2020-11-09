package ru.android.romashkaapp.matches.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import ru.android.romashkaapp.R
import ru.android.romashkaapp.databinding.CalendarEventItemViewBinding
import java.time.LocalDate

/**
 * Created by yasina on 03.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */

data class Event(val id: String, val text: String, val date: LocalDate)

internal val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)

class CalendarDaysAdapter(val onClick: (Event) -> Unit) :
    RecyclerView.Adapter<CalendarDaysAdapter.DayViewHolder>() {

    val events = mutableListOf<Event>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        return DayViewHolder(
            CalendarEventItemViewBinding.inflate(parent.context.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: DayViewHolder, position: Int) {
        viewHolder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    inner class DayViewHolder(binding: CalendarEventItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onClick(events[bindingAdapterPosition])
            }
        }

        fun bind(event: Event) {
//            binding.itemEventText.text = event.text
        }
    }
}