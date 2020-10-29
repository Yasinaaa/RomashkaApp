package ru.android.romashkaapp.sector_seat

import android.app.Application
import android.ru.romashkaapp.models.CartModel
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.android.romashkaapp.adapter.helpers.SwipeRemoveActionListener
import ru.android.romashkaapp.base.BaseViewModel

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class SectorSeatViewModel(application: Application) : BaseViewModel(application), View.OnClickListener, SwipeRemoveActionListener {

    val zoomView = MutableLiveData<Boolean>()

    private val _list: MutableLiveData<MutableList<CartModel>> = MutableLiveData()
    val list: LiveData<MutableList<CartModel>> = _list

    private val _deletedPosition: MutableLiveData<Int> = MutableLiveData()
    val removedItemIndex: LiveData<Int> = _deletedPosition

    private var previousDeletedItem: CartModel? = null

    init {
        _list.value = arrayListOf(CartModel(), CartModel(), CartModel())
    }

    fun zoomIn(){
        zoomView.value = true
    }

    fun zoomOut(){
        zoomView.value = false
    }

    override fun onClick(view: View?) {
        TODO("Not yet implemented")
    }

    override fun removeItem(position: Int) {
        previousDeletedItem = _list.value?.get(position)
        _list.value?.removeAt(position)
        _deletedPosition.value = position
    }

}