package ru.android.romashkaapp.tickets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_tickets.*
import ru.android.romashkaapp.R
import ru.android.romashkaapp.databinding.FragmentTicketsBinding
import ru.android.romashkaapp.main.MainViewModel
import ru.android.romashkaapp.tickets.adapters.HorizontalMarginItemDecoration
import ru.android.romashkaapp.tickets.adapters.TicketsAdapter
import java.lang.Math.abs

/*
 * Created by yasina on 23.12.2020
*/
class TicketsFragment : Fragment() {

    lateinit var binding: FragmentTicketsBinding
    private val viewModel: TicketsViewModel by viewModels()
    private lateinit var adapter: TicketsAdapter
    private lateinit var mainViewModel: MainViewModel

    fun setViewModel(viewModel: MainViewModel){
        this.mainViewModel = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tickets, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }

    private fun setAdapter(){
        binding.visibilityOfProgressBar = View.VISIBLE
        binding.visibilityOfEmptyBasket = View.GONE
        binding.visibilityOfBasket = View.GONE //todo empty mode

        adapter = TicketsAdapter(viewModel.getListener())

        vp_tickets.adapter = adapter

        // You need to retain one page on each side so that the next and previous items are visible
        vp_tickets.offscreenPageLimit = 1

        // Add a PageTransformer that translates the next and previous items horizontally
        // towards the center of the screen, which makes them visible
        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            // Next line scales the item's height. You can remove it if you don't want this effect
            page.scaleY = 1 - (0.25f * kotlin.math.abs(position))
            // If you want a fading effect uncomment the next line:
            // page.alpha = 0.25f + (1 - abs(position))
        }
        vp_tickets.setPageTransformer(pageTransformer)

        // The ItemDecoration gives the current (centered) item horizontal margin so that
        // it doesn't occupy the whole screen width. Without it the items overlap
        val itemDecoration = HorizontalMarginItemDecoration(
            requireContext(),
            R.dimen.viewpager_current_item_horizontal_margin
        )
        vp_tickets.addItemDecoration(itemDecoration)

        binding.viewModel!!.list.observe(viewLifecycleOwner, {
            adapter.updateList(it)
            binding.visibilityOfProgressBar = View.GONE
            binding.visibilityOfBasket = View.VISIBLE
        })
    }
}