package ru.android.romashkaapp.matches.adapters

import android.graphics.Typeface
import android.ru.romashkaapp.models.EventModel
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.android.synthetic.main.item_event.view.*
import ru.android.romashkaapp.BR
import ru.android.romashkaapp.R
import ru.android.romashkaapp.afisha.adapters.MatchesAdapter
import ru.android.romashkaapp.databinding.*
import ru.android.romashkaapp.matches.ItemClickListener
import ru.android.romashkaapp.utils.*
import ru.android.romashkaapp.utils.setTextColorRes
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

/**
 * Created by yasina on 03.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class MatchesAndCalendarAdapter constructor(l: ItemClickListener) : MatchesAdapter(l) {

    companion object {
        const val VIEW_TYPE_CALENDAR = 0
        const val VIEW_TYPE_EVENT = 1
    }

    private var eventsAdapter: CalendarDaysAdapter? = null

    class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingV: ItemCalendarBinding? = DataBindingUtil.bind(view)
    }

    private var list: MutableList<Match?> = mutableListOf()
    private var matches: MutableList<Match?> = mutableListOf()
    //calendar
    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()
    private val events = mutableMapOf<LocalDate, EventModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        return when(viewType){
            VIEW_TYPE_CALENDAR -> {
                eventsAdapter = CalendarDaysAdapter {}
                CalendarViewHolder(
                    LayoutInflater.from(mContext)
                        .inflate(R.layout.item_calendar, parent, false)
                )
            }
            else -> {
                ItemViewHolder(
                    LayoutInflater.from(mContext)
                        .inflate(R.layout.item_event, parent, false)
                )
            }
        }
    }

    private fun showEvent(holder: ItemViewHolder, position: Int){
        parseName(list[position]!!)
        setDate(list[position]!!)
        setRivalImage(holder, list[position]!!.event.thumbnail)

        holder.bindingItem?.setVariable(BR.match, list[position])
        holder.bindingItem?.executePendingBindings()
        holder.bindingItem?.root?.setOnClickListener { listener.click(list[position]!!) }

        if(position == 0 && list[0] != null){
            var lp: RecyclerView.LayoutParams = holder.bindingItem?.root?.layoutParams as RecyclerView.LayoutParams
            lp.topMargin = 30.toDp(mContext)
        }else if(position == list.size - 1){
            var lp: RecyclerView.LayoutParams = holder.bindingItem?.root?.layoutParams as RecyclerView.LayoutParams
            lp.bottomMargin = 24.toDp(mContext)
        }
    }

    private fun showEvent(holder: ItemViewHolder, match: Match){
        parseName(match)
        setDate(match)
        setRivalImage(holder, match.event.thumbnail)

        holder.bindingItem?.setVariable(BR.match, match)
        holder.bindingItem?.executePendingBindings()
        holder.bindingItem?.root?.setOnClickListener { listener.click(match) }

        var lp: RecyclerView.LayoutParams = holder.bindingItem?.root?.layoutParams as RecyclerView.LayoutParams
        lp.topMargin = 16.toDp(mContext)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {

            if (list[0] == null){
                matches.forEach { match ->
                    if(match != null) {
                        if (selectedDate != null) {
                            if (selectedDate == match.event.sdate.toLocalDate()) {
                                showEvent(holder, match)
                                return@forEach
                            }
                        }
                    }
                }
            }else{
                  showEvent(holder, position)
            }

        }else if (holder is CalendarViewHolder) {

            val daysOfWeek = daysOfWeekFromLocale()
            val currentMonth = YearMonth.now()
            holder.bindingV?.exThreeCalendar?.apply {
                setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10), daysOfWeek.first())
                scrollToMonth(currentMonth)
            }

            class DayViewContainer(view: View) : ViewContainer(view) {
                lateinit var day: CalendarDay // Will be set when this container is bound.
                val binding = ItemCalendarDayBinding.bind(view)

                init {
                    view.setOnClickListener {
                        if(events.containsKey(day.date)) {
                            if (day.owner == DayOwner.THIS_MONTH) {
                                selectDate(holder, day.date)
                            }
                        }
                    }
                }
            }

            holder.bindingV?.exThreeCalendar?.dayBinder = object : DayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)

                override fun bind(container: DayViewContainer, day: CalendarDay) {
                    container.day = day
                    val dayText = container.binding.tvDay
                    val ivSoccerView = container.binding.ivSoccer
                    val backgroundView = container.binding.backgroundRectangle

                    dayText.text = day.date.dayOfMonth.toString()

                    if (day.owner == DayOwner.THIS_MONTH) {

                        dayText.makeVisible()

                        if (day.date == today){
                            dayText.visibility = VISIBLE
                            dayText.setTypeface(dayText.typeface, Typeface.BOLD)
                            dayText.setTextColorRes(android.R.color.black)
                            backgroundView.visibility = GONE
                            ivSoccerView.visibility = GONE
                        }

                        if(events.containsKey(day.date)){
                            dayText.visibility = GONE
                            backgroundView.visibility = GONE
                            ivSoccerView.visibility = VISIBLE
                        }
                        else{
                            dayText.visibility = VISIBLE
                            dayText.setTextColorRes(android.R.color.black)
                            dayText.setTypeface(dayText.typeface, Typeface.NORMAL)
                            backgroundView.visibility = GONE
                            ivSoccerView.visibility = GONE
                        }

                        if (day.date == selectedDate){
                            dayText.visibility = GONE
                            backgroundView.visibility = VISIBLE
                        }

                    } else {
                        dayText.makeInVisible()
                    }
                }
            }

            holder.bindingV?.exThreeCalendar?.monthScrollListener = {
                var months = mContext.resources.getStringArray(R.array.months)
                var m = months[it.yearMonth.monthValue - 1]

                holder.bindingV?.tvMonth?.text = m.plus(" ").plus(it.year.toString())

                matches.forEach { match ->
                    if(match != null){
                        var date = match.event.sdate.toLocalDate()
                        if(it.month == date!!.month.value){
                            if(events.isEmpty()){
                                selectedDate = date
                                notifyDataSetChanged()
                            }
                            events[date] = match.event
                            holder.bindingV?.exThreeCalendar?.notifyDateChanged(date)
                        }
                    }
                }.also {
                    holder.bindingV?.exThreeCalendar?.notifyDateChanged(today)
                }
            }

            class MonthViewContainer(view: View) : ViewContainer(view) {
                val legendLayout = ItemCalendarHeaderBinding.bind(view).legendLayout
            }
            holder.bindingV?.exThreeCalendar?.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                    // Setup each header day text if we have not done that already.
                    if (container.legendLayout.tag == null) {
                        container.legendLayout.tag = month.yearMonth
                        container.legendLayout.children.map { it as TextView }.forEachIndexed { index, tv ->
//                            tv.text = daysOfWeek[index].name.first().toString()
//                            tv.setTextColorRes(android.R.color.black)
                        }
                    }
                }
            }

            var lp = holder.bindingV?.clMainGameCard?.layoutParams as RecyclerView.LayoutParams
            lp.bottomMargin = 0.toDp(mContext)

        }
    }

    fun updateMatchesList(list: MutableList<Match?>) {
        this.matches = mutableListOf()
        this.matches.addAll(list)
        this.list = mutableListOf(null)
        notifyDataSetChanged()
    }

    override fun updateList(list: MutableList<Match?>) {
        this.list = mutableListOf()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if(list.size == 0){
            0
        }else {
            if (list[0] != null)
                list.size
            else
                2
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0 && list[0] == null) VIEW_TYPE_CALENDAR else VIEW_TYPE_EVENT
    }

    private fun selectDate(holder: CalendarViewHolder, date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { holder.bindingV?.exThreeCalendar?.notifyDateChanged(it) }
            holder.bindingV?.exThreeCalendar?.notifyDateChanged(date)
            notifyDataSetChanged()
        }
    }

    private fun daysOfWeekFromLocale(): Array<DayOfWeek> {
        val firstDayOfWeek = WeekFields.of(Locale("ru")).firstDayOfWeek
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
