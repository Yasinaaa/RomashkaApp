package ru.android.romashkaapp.howtobuy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_how_to_buy.*
import ru.android.romashkaapp.R
import ru.android.romashkaapp.databinding.FragmentHowToBuyBinding
import ru.android.romashkaapp.howtobuy.adapter.StepsAdapter

/*
 * Created by yasina on 29.12.2020
*/
class HowToBuyFragment : Fragment() {

     lateinit var binding: FragmentHowToBuyBinding
    private val viewModel: HowToBuyViewModel by viewModels()
    private lateinit var stepsAdapter: StepsAdapter

//    private lateinit var mainViewModel: MainViewModel
//
//    fun setViewModel(viewModel: MainViewModel){
//        this.mainViewModel = viewModel
//    }
//
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_how_to_buy, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            stepsAdapter = StepsAdapter()
            rv_steps.adapter = stepsAdapter
            rv_steps.layoutManager = LinearLayoutManager(context)
        }
    }
}