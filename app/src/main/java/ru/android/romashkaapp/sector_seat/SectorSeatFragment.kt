package ru.android.romashkaapp.sector_seat

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_sector.*
import ru.android.romashkaapp.R
import ru.android.romashkaapp.adapter.helpers.SwipeRemoveActionCallback
import ru.android.romashkaapp.adapter.helpers.SwipeRemoveItemDecoration
import ru.android.romashkaapp.base.BaseFragment
import ru.android.romashkaapp.databinding.FragmentSectorBinding
import ru.android.romashkaapp.sector_seat.adapter.CartBottomBarAdapter
import ru.android.romashkaapp.sector_seat.adapter.PricesWithColorAdapter
import ru.android.romashkaapp.sector_seat.sector.SeatShape
import ru.android.romashkaapp.sector_seat.sector.ShapesInteractor
import ru.android.romashkaapp.stadium.StadiumFragment
import ru.android.romashkaapp.utils.removeZero
import ru.android.romashkaapp.utils.ticketsCount

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class SectorSeatFragment : BaseFragment() {

    companion object{
        val AREA_ID = "AREA_ID"
        val SECTOR_ID = "SECTOR_ID"
    }
    private var callback: OnBackPressedCallback? = null
    lateinit var binding: FragmentSectorBinding
    private val viewModel: SectorSeatViewModel by viewModels()
    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null
    private val adapter = CartBottomBarAdapter()
    private lateinit var swipeItemTouchHelper: ItemTouchHelper
    private var pricesAdapter: PricesWithColorAdapter? = null

    private lateinit var background: ColorDrawable
    private var icon: Drawable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sector, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        background = ColorDrawable(
            ContextCompat.getColor(
                requireContext(),
                R.color.red
            )
        )
        icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)

        requireArguments().containsKey(StadiumFragment.EVENT_ID).let {
            viewModel.getSeats(
                requireArguments().getInt(StadiumFragment.EVENT_ID),
                requireArguments().getInt(AREA_ID),
                requireArguments().getString(SECTOR_ID)
            )
        }

        binding.apply {
            rv_cart_items.adapter = adapter
            rv_cart_items.layoutManager = LinearLayoutManager(context)
            rv_cart_items.addItemDecoration(
                SwipeRemoveItemDecoration(
                    background,
                    icon
                )
            )
        }

        viewModel.list.observe(viewLifecycleOwner, {
            adapter.updateList(it)
        })

        binding.apply {
            pricesAdapter = PricesWithColorAdapter(viewModel!!.getListener())
            rv_prices.adapter = pricesAdapter
            rv_prices.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }

        binding.viewModel?.pricesList!!.observe(viewLifecycleOwner, {
            pricesAdapter!!.updateList(it)
        })

        binding.viewModel?.cart!!.observe(viewLifecycleOwner, {
            cl_bottomsheet.visibility = View.VISIBLE
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED

            tv_total_price.text = String.format(getString(R.string.rub), it.amount.plus(it.commision).removeZero())
            tv_total_tickets_count.text = it.count.ticketsCount(requireContext())
        })

        binding.viewModel?.nextFragment!!.observe(viewLifecycleOwner, {
            setFragment(it)
        })

        swipeItemTouchHelper = ItemTouchHelper(
            SwipeRemoveActionCallback(
                background,
                icon,
                arrayListOf(viewModel, adapter),
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            )
        )
        swipeItemTouchHelper.attachToRecyclerView(binding.rvCartItems)


        binding.viewModel?.zoomView!!.observe(viewLifecycleOwner, {
//            if(it){
//                wv_stadium.zoomIn()
//            }else{
//                wv_stadium.zoomOut()
//            }
        })

        binding.viewModel?.seatsCoordinates!!.observe(viewLifecycleOwner, {
            wv_stadium.setSeat(it)
        })

        bottomSheetBehavior = BottomSheetBehavior.from(cl_bottomsheet)
        bottomSheetBehavior!!.peekHeight = 300
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomSheetBehavior!!.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // handle onSlide
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        rv_cart_items.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        rv_cart_items.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        rv_cart_items.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                        rv_cart_items.visibility = View.GONE
                    }
                }
            }
        })

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStackImmediate()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback!!)

        initSeats()

        binding.viewModel?.seatsCoordinates!!.observe(viewLifecycleOwner, {
            it.forEach { seatModel ->
                val shape = SeatShape(seatModel.x!!.toInt()*5, seatModel.y!!.toInt()*5, 30, seatModel.col.toString())
                shape.type = SeatShape.Type.SQUARE
                ShapesInteractor.instance.upDateCanvas(shape)
            }
        })
    }

    private fun initSeats(){
        ShapesInteractor.instance.setCanvas(wv_stadium)
        ShapesInteractor.instance.setContext(context)
        getCanvasWidthAndHeight()
    }

    private fun getCanvasWidthAndHeight() {
//        val viewTreeObserver: ViewTreeObserver = wv_stadium.viewTreeObserver
//        if (viewTreeObserver.isAlive) {
//            viewTreeObserver.addOnGlobalLayoutListener(object :
//                ViewTreeObserver.OnGlobalLayoutListener {
//                override fun onGlobalLayout() {
//                    wv_stadium.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                    var maxY = wv_stadium.width
//                    var maxX = wv_stadium.height
//                    //Reduce radius so that shape isn't left incomplete at the edge
//                    ShapesInteractor.instance.maxX = 100
//
//                    ShapesInteractor.instance.maxY = 100
//                    removeOnGlobalLayoutListener(wv_stadium, this)
//                    Log.d(
//                        "TAG",
//                        " Screen max x= $maxX maxy = $maxY"
//                    )
//                }
//            })
//        }
    }

    /*Since global layout listener is called multiple times, remove it once we get the screen width and height
     */
    fun removeOnGlobalLayoutListener(v: View, listener: ViewTreeObserver.OnGlobalLayoutListener?) {
        v.viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }
}


