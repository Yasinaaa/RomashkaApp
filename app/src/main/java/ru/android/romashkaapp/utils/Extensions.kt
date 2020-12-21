package ru.android.romashkaapp.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import ru.android.romashkaapp.R
import java.text.DecimalFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by yasina on 09.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
fun Int.toDp(context: Context):Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
).toInt()

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInVisible() {
    visibility = View.INVISIBLE
}

internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

internal fun TextView.setTextColorRes(@ColorRes color: Int) = setTextColor(context.getColorCompat(color))

fun String?.parseTimeStamp(): String{
    return if(this != null){
        val stamp = Instant.ofEpochSecond(this.toLong())
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
            "dd MMMM yyyy EEE hh:mm",
            Locale("ru")
        )
        val formattedString: String = stamp.format(formatter)
        formattedString.toUpperCase()
    }else{
        ""
//            match.firstLine = context.getString(R.string.no_event_title)
    }
}

fun String?.toLocalDate(): LocalDate?{
    if(this != null){
        return Instant.ofEpochSecond(this.toLong())
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
    return null
}

fun Float.removeZero(): String{
    val format = DecimalFormat()
    format.isDecimalSeparatorAlwaysShown = false
    return format.format(this)
}

fun Int.ticketsCount(context: Context): String{
    return when {
        this == 1 -> {
            String.format(context.getString(R.string.one_ticket_count), this)
        }
        this == 0 -> {
            String.format(context.getString(R.string.zero_ticket_count), this)
        }
        this == 2 || this == 3 || this == 4 -> {
            String.format(context.getString(R.string.tickets_count), this)
        }
        else -> {
            String.format(context.getString(R.string.zero_ticket_count), this)
        }
    }
}

fun View.hideKeyboard(context: Context) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}
