package ru.android.romashkaapp.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_sector.*
import ru.android.romashkaapp.R
import ru.android.romashkaapp.afisha.AfishaFragment
import ru.android.romashkaapp.boarding.BoardingViewModel
import ru.android.romashkaapp.databinding.FragmentMainBinding
import ru.android.romashkaapp.databinding.FragmentOnboardingBinding
import ru.android.romashkaapp.matches.MatchesFragment


class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.createFragment.observe(viewLifecycleOwner, {
            setFragment(it)
        })
    }

    private fun setFragment(fragment: Fragment){
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_main_fragment, fragment)
        transaction.commitAllowingStateLoss()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.basket ->{

            }
            R.id.play_bill ->{

            }
            R.id.account ->{

            }
            else -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
}