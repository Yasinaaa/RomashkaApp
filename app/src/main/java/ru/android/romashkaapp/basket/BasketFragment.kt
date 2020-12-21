package ru.android.romashkaapp.basket

import android.graphics.Typeface.BOLD
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_basket.*
import ru.android.romashkaapp.R
import ru.android.romashkaapp.basket.adapters.CartAdapter
import ru.android.romashkaapp.databinding.FragmentBasketBinding
import ru.android.romashkaapp.main.MainViewModel

/**
 * Created by yasina on 05.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class BasketFragment : Fragment() {

    companion object{
        val ORDER_ID = "ORDER_ID"
    }

    lateinit var binding: FragmentBasketBinding
    private val viewModel: BasketViewModel by viewModels()
    private lateinit var mainViewModel: MainViewModel
    private var adapter: CartAdapter? = null
    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null

    fun setViewModel(viewModel: MainViewModel) {
        this.mainViewModel = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_basket, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setExpirationDate()

        requireArguments().containsKey(ORDER_ID).let {
            binding.viewModel!!.setOrderId(requireArguments().getInt(ORDER_ID))
            tv_order.text = String.format(getString(R.string.order),  requireArguments().getInt(ORDER_ID).toString())
        }

        binding.apply {
            adapter = CartAdapter(viewModel!!.getListener())
            rv_cart_items.adapter = adapter
            rv_cart_items.layoutManager = LinearLayoutManager(context)
        }

        viewModel.list.observe(viewLifecycleOwner, {
            adapter!!.updateList(it)
        })

        bottomSheetBehavior = BottomSheetBehavior.from(cl_bottomsheet)
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
        cl_bottomsheet.visibility = View.GONE

        bottomSheetBehavior!!.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // handle onSlide
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        cl_bottomsheet.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {

                    }
                    BottomSheetBehavior.STATE_SETTLING -> {

                    }
                }
            }
        })

        tv_order_promocode.setOnClickListener {
            cl_bottomsheet.visibility = View.VISIBLE
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun setExpirationDate(){
        val text = getString(R.string.tickets_expires_in) + " 29:23"
        val spannableString = SpannableString(text)
        spannableString.setSpan(
            ForegroundColorSpan(getColor(requireContext(), R.color.red)),
            text.lastIndex - 5, text.lastIndex + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannableString.setSpan(
            StyleSpan(BOLD),
            text.lastIndex - 5, text.lastIndex + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        tv_expire_date.setText(spannableString, TextView.BufferType.SPANNABLE)
    }
}