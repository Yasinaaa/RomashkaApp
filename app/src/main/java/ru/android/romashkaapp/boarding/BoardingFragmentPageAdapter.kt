package ru.android.romashkaapp.boarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by yasina on 20.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class BoardingFragmentPageAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val screens = arrayListOf<Fragment>()

    fun setItems(screens: List<Fragment>) {
        this.screens.apply {
            clear()
            addAll(screens)
            notifyDataSetChanged()
        }
    }

    fun getItems(): List<Fragment> {
        return screens
    }

    override fun getItemCount(): Int {
        return screens.size
    }

    override fun createFragment(position: Int): Fragment {
        return screens[position]
    }


}