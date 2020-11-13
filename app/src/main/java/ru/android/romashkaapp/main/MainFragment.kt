package ru.android.romashkaapp.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.LogPrinter
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_sector.*
import ru.android.romashkaapp.R
import ru.android.romashkaapp.afisha.AfishaFragment
import ru.android.romashkaapp.basket.BasketFragment
import ru.android.romashkaapp.boarding.BoardingViewModel
import ru.android.romashkaapp.databinding.FragmentMainBinding
import ru.android.romashkaapp.databinding.FragmentOnboardingBinding
import ru.android.romashkaapp.login.LoginFragment
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

        bottom_navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        var badge = bottom_navigation.getOrCreateBadge(R.id.account)
        badge.backgroundColor = ContextCompat.getColor(requireContext(), R.color.button_next_color)
        badge.isVisible = true
        badge.number = 99

        viewModel.bottomBar.observe(viewLifecycleOwner, {
            if(it){
                bottom_navigation.visibility = VISIBLE
            }else{
                bottom_navigation.visibility = GONE
            }
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
            R.id.my_tickets ->{

            }
            R.id.account ->{
                setFragment(LoginFragment())
            }
            else -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.basket ->{
                setFragment(BasketFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.play_bill ->{
                return@OnNavigationItemSelectedListener true
            }
            R.id.my_tickets ->{
                return@OnNavigationItemSelectedListener true
            }
            R.id.account ->{
                setFragment(LoginFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}