package ru.android.romashkaapp.matches.adapters

import android.ru.romashkaapp.models.EventModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import ru.android.romashkaapp.R
import ru.android.romashkaapp.databinding.ItemCalendarBinding
import ru.android.romashkaapp.databinding.ItemCalendarBindingImpl
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

/**
 * Created by yasina on 03.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class MatchesAndCalendarAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_CALENDAR = 0
        const val VIEW_TYPE_EVENT = 1
    }

    private var eventsAdapter: CalendarDaysAdapter? = null

    class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingV: ItemCalendarBinding? = DataBindingUtil.bind(view)
    }

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ViewDataBinding? = DataBindingUtil.bind(view)
    }

    private var list: MutableList<EventModel> = mutableListOf()
    //calendar
    private var selectedDate: LocalDate? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_CALENDAR -> {
                eventsAdapter = CalendarDaysAdapter {
//                    AlertDialog.Builder(parent.context)
//                        .setMessage(R.string.example_3_dialog_delete_confirmation)
//                        .setPositiveButton(R.string.delete) { _, _ ->
////                            deleteEvent(it)
//                        }
//                        .setNegativeButton(R.string.close, null)
//                        .show()
                }
                CalendarViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_calendar, parent, false))
            }
            else -> {
                EventViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_event, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EventViewHolder) {
//            holder.binding?.setVariable(BR.email, list!![position])
            holder.binding?.executePendingBindings()
        }else if (holder is CalendarViewHolder) {
            val daysOfWeek = daysOfWeekFromLocale()
            val currentMonth = YearMonth.now()
            holder.bindingV?.exThreeCalendar?.apply {
                setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10), daysOfWeek.first())
                scrollToMonth(currentMonth)
            }
            holder.bindingV?.exThreeCalendar?.post {
                // Show today's events initially.
//                selectDate(holder, today)
            }
        }
    }

    fun updateList(list: MutableList<EventModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0) VIEW_TYPE_CALENDAR else VIEW_TYPE_EVENT
    }

    private fun selectDate(holder: CalendarViewHolder, date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { holder.bindingV?.exThreeCalendar?.notifyDateChanged(it) }
            holder.bindingV?.exThreeCalendar?.notifyDateChanged(date)
//            updateAdapterForDate(date)
        }
    }

    private fun daysOfWeekFromLocale(): Array<DayOfWeek> {
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        var daysOfWeek = DayOfWeek.values()
        // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
        // Only necessary if firstDayOfWeek != DayOfWeek.MONDAY which has ordinal 0.
        if (firstDayOfWeek != DayOfWeek.MONDAY) {
            val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
            val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
            daysOfWeek = rhs + lhs
        }
        return daysOfWeek
    }
}
