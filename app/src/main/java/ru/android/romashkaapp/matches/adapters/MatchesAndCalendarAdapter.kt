package ru.android.romashkaapp.matches.adapters

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
import ru.android.romashkaapp.BR
import ru.android.romashkaapp.R
import ru.android.romashkaapp.afisha.adapters.MatchesAdapter
import ru.android.romashkaapp.databinding.*
import ru.android.romashkaapp.matches.ItemClickListener
import ru.android.romashkaapp.utils.makeInVisible
import ru.android.romashkaapp.utils.makeVisible
import ru.android.romashkaapp.utils.setTextColorRes
import ru.android.romashkaapp.utils.toDp
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
    //calendar
    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()
    private val events = mutableMapOf<LocalDate, List<Event>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
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
                CalendarViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_calendar, parent, false))
            }
            else -> {
                MatchesAdapter.ItemViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_event, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MatchesAdapter.ItemViewHolder) {

            parseName(list[position]!!)
            setDate(list[position]!!)
            setRivalImage(holder, list[position]!!.event.thumbnail)

            holder.binding?.setVariable(BR.match, list[position])
            holder.binding?.executePendingBindings()
            holder.binding?.root?.setOnClickListener { listener!!.click(list[position]!!) }

            if(position == 0){
                var lp: RecyclerView.LayoutParams = holder.binding?.root?.layoutParams as RecyclerView.LayoutParams
                lp.topMargin = 30.toDp(mContext)
            }else if(position == list.size - 1){
                var lp: RecyclerView.LayoutParams = holder.binding?.root?.layoutParams as RecyclerView.LayoutParams
                lp.bottomMargin = 24.toDp(mContext)
            }
            holder.binding?.root?.setOnClickListener { listener!!.click(list[position]) }

        }else if (holder is CalendarViewHolder) {
            val daysOfWeek = daysOfWeekFromLocale()
            val currentMonth = YearMonth.now()
            holder.bindingV?.exThreeCalendar?.apply {
                setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10), daysOfWeek.first())
                scrollToMonth(currentMonth)
            }
            holder.bindingV?.exThreeCalendar?.post {
                // Show today's events initially.
                selectDate(holder, today)
            }
            class DayViewContainer(view: View) : ViewContainer(view) {
                lateinit var day: CalendarDay // Will be set when this container is bound.
                val binding = ItemCalendarDayBinding.bind(view)

                init {
                    view.setOnClickListener {
                        if (day.owner == DayOwner.THIS_MONTH) {
                            selectDate(holder, day.date)
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
                        when (day.date) {
                            today -> {
                                dayText.visibility = GONE
//                                textView.setTextColorRes(android.R.color.white)
                                backgroundView.visibility = VISIBLE
                                ivSoccerView.visibility = VISIBLE

                            }
                            selectedDate -> {
                                dayText.setTextColorRes(android.R.color.white)
                                dayText.setBackgroundResource(R.drawable.ic_soccer_calendar)

                                dayText.visibility = GONE
                                backgroundView.visibility = GONE
                                ivSoccerView.visibility = VISIBLE
                            }
                            else -> {
                                dayText.visibility = VISIBLE
                                dayText.setTextColorRes(android.R.color.black)
                                backgroundView.visibility = GONE
                                ivSoccerView.visibility = GONE
//                                dotView.isVisible = events[day.date].orEmpty().isNotEmpty()
                            }
                        }
                    } else {
                        dayText.makeInVisible()
//                        dotView.makeInVisible()
                    }
                }
            }

            holder.bindingV?.exThreeCalendar?.monthScrollListener = {
//                if (it.year == today.year) {
//                    titleSameYearFormatter.format(it.yearMonth)
//                } else {
//                    titleFormatter.format(it.yearMonth)
//                }
                var months = mContext.resources.getStringArray(R.array.months)
                var m = months[it.yearMonth.monthValue-1]

                holder.bindingV?.tvMonth?.text =
                    m.plus(" ").plus(it.year.toString())
//
//                // Select the first day of the month when
//                // we scroll to a new month.
                selectDate(holder, it.yearMonth.atDay(1))
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

//            holder.bindingV?.exThreeAddButton.setOnClickListener {
////                inputDialog.show()
//            }
        }
    }

    override fun updateList(list: MutableList<Match?>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
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
            updateAdapterForDate(date)
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

    private fun updateAdapterForDate(date: LocalDate) {
//        eventsAdapter.apply {
//            events.clear()
//            events.addAll(this@Example3Fragment.events[date].orEmpty())
//            notifyDataSetChanged()
//        }
//        binding.exThreeSelectedDateText.text = selectionFormatter.format(date)
    }
}
