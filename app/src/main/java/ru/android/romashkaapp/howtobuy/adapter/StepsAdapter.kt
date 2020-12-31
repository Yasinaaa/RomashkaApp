package ru.android.romashkaapp.howtobuy.adapter

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.SpannableString
import android.text.style.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.android.romashkaapp.BR
import ru.android.romashkaapp.R
import ru.android.romashkaapp.databinding.ItemHowToBuyBinding


/**
 * Created by yasina on 29.12.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class StepsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context
    var currentLength = 0

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemHowToBuyBinding? = DataBindingUtil.bind(view)
    }

    private var list = mutableListOf(
        Step(R.string.step_1, R.string.step_1_text),
        Step(R.string.step_2, R.string.step_2_text),
        Step(R.string.step_3, R.string.step_3_text),
        Step(R.string.step_4, R.string.step_4_text),
        Step(R.string.step_5, R.string.step_5_text),
        Step(R.string.step_6, R.string.step_6_text)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_how_to_buy,
            parent,
            false
        )
        return ItemViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.binding?.setVariable(BR.title, context.getString(list[position].title))
            holder.binding?.setVariable(BR.titleText, context.getString(list[position].text))

            when(position){
                0 -> {
                    holder.binding?.tvText!!.text = makeFirstStep()
                }
                1 -> {
                    holder.binding?.tvText!!.text = makeSecondStep()
                }
                2 -> {
                    holder.binding?.tvText!!.text = makeFirstStep()
                }
                3 -> {
                    holder.binding?.tvText!!.text = makeFirstStep()
                }
                4 -> {
                    holder.binding?.tvText!!.text = makeFirstStep()
                }
                5 -> {
                    holder.binding?.tvText!!.text = makeFirstStep()
                }
                6 -> {
                    holder.binding?.tvText!!.text = makeFirstStep()
                }
            }

            holder.binding?.mbPlus!!.setOnClickListener {
                if (holder.binding.tvText.visibility == View.GONE) {
                    //todo set minus image
                    holder.binding.tvText.visibility = View.VISIBLE
                }else {
                    holder.binding.tvText.visibility = View.GONE
                }
            }

            holder.binding.executePendingBindings()
        }
    }

    private fun makeFirstStep(): SpannableString{

        currentLength = 0

        val text_1_line_1 = context.getString(R.string.step_1_body_line_1_1)
        val text_1_line_2 = context.getString(R.string.step_1_body_line_1_2)
        val text_1_line_3 = context.getString(R.string.step_1_body_line_1_3)

        val text_2_line_1 = context.getString(R.string.step_1_body_line_2_1)
        val text_2_line_2 = context.getString(R.string.step_1_body_line_2_2)
        val text_2_line_3 = context.getString(R.string.step_1_body_line_2_3)

        val spannableString = SpannableString(text_1_line_1 + text_1_line_2 + text_1_line_3 + text_2_line_1 + text_2_line_2 + text_2_line_3)

        setSpanBlue(spannableString)
        setSpanBlack(spannableString, text_1_line_2)
        setSpanGrey(spannableString, text_1_line_3)

        setSpanBlue(spannableString)
        setSpanBlack(spannableString, text_2_line_2)
        setSpanGrey(spannableString, text_2_line_3)

        return spannableString
    }

    private fun makeSecondStep(): SpannableString{

        currentLength = 0
        val line_11 = context.getString(R.string.step_2_body_line_1_1)
        val line_12 = context.getString(R.string.step_2_body_line_1_2)
        val line_21 = context.getString(R.string.step_2_body_line_2_1)
        val line_3 = context.getString(R.string.step_2_body_line_3)
        val line_4 = context.getString(R.string.step_2_body_line_4)

        val spannableString = SpannableString(line_11 + line_12 + line_21 + line_3 + line_4)

        setSpanBlack(spannableString, line_11)
        setSpanBlueLink(spannableString, line_12)
        setSpanBoldBlack(spannableString, line_21)
        setSpanBlack(spannableString, line_3)
        setSpanGrey(spannableString, line_4)

        return spannableString
    }

    private fun setSpanBlue(spannableString: SpannableString){
        val begin = currentLength
        currentLength += 2
        spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimary)), begin, currentLength, 0)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), begin, currentLength, 0)
    }

    private fun setSpanBlack(spannableString: SpannableString, text: String){
        val begin = currentLength
        currentLength += text.length
        spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, android.R.color.black)), begin, currentLength, 0)
        spannableString.setSpan(StyleSpan(Typeface.NORMAL), begin, currentLength, 0)
    }

    private fun setSpanGrey(spannableString: SpannableString, text: String){
        val begin = currentLength
        currentLength += text.length
        spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.light_grey)), begin, currentLength, 0)
        spannableString.setSpan(StyleSpan(Typeface.NORMAL), begin, currentLength, 0)
    }

    private fun setSpanBlueLink(spannableString: SpannableString, text: String){
        val begin = currentLength
        currentLength += text.length
        spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimary)), begin, currentLength, 0)
        spannableString.setSpan(StyleSpan(Typeface.NORMAL), begin, currentLength, 0)
    }

    private fun setSpanBoldBlack(spannableString: SpannableString, text: String){
        val begin = currentLength
        currentLength += text.length
        spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, android.R.color.black)), begin, currentLength, 0)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), begin, currentLength, 0)
    }

    class Step(var title: Int, var text: Int)

    override fun getItemCount(): Int {
        return list.size
    }
}
