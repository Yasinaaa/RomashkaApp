package ru.android.romashkaapp.howtobuy.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_user_data.*
import ru.android.romashkaapp.R
import ru.android.romashkaapp.BR
import ru.android.romashkaapp.databinding.ItemHowToBuyBinding

/**
 * Created by yasina on 29.12.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class StepsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_how_to_buy, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.binding?.setVariable(BR.title, context.getString(list[position].title))
            holder.binding?.setVariable(BR.titleText, context.getString(list[position].text))
            holder.binding?.tvText!!.visibility = View.VISIBLE
            if (position == 0) {
                holder.binding.tvText.text = HtmlCompat.fromHtml(context.getString(R.string.step_1_body), HtmlCompat.FROM_HTML_MODE_LEGACY);
            }
            holder.binding.executePendingBindings()
        }
    }

    class Step(var title: Int, var text: Int)

    override fun getItemCount(): Int {
        return list.size
    }
}
