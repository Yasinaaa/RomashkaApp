package ru.android.romashkaapp.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_afisha.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_sector.*
import ru.android.romashkaapp.R
import ru.android.romashkaapp.basket.BasketFragment
import ru.android.romashkaapp.databinding.FragmentMainBinding
import ru.android.romashkaapp.login.LoginFragment


class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CAMERA
                ),
                0
            )
        }else{
            init()
        }
    }

    fun init() {
        viewModel.createFragment.observe(viewLifecycleOwner, {
            setFragment(it)
            changeNavigationStatus(bottom_navigation.menu.getItem(0))
        })

        bottom_navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        var badge = bottom_navigation.getOrCreateBadge(R.id.basket)
        badge.backgroundColor = ContextCompat.getColor(requireContext(), R.color.green)
        badge.badgeTextColor = ContextCompat.getColor(requireContext(), android.R.color.white)
        badge.isVisible = true
        badge.number = 99

        viewModel.bottomBar.observe(viewLifecycleOwner, {
            if (it) {
                bottom_navigation.visibility = VISIBLE
            } else {
                bottom_navigation.visibility = GONE
            }
        })

        bottom_navigation.itemIconTintList = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {
            init()
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CAMERA
                ),
                0
            )
        }
    }

    private fun setFragment(fragment: Fragment){
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.nav_main_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.play_bill -> {

            }
            R.id.basket -> {

            }
            R.id.my_tickets -> {

            }
            R.id.account -> {
                setFragment(LoginFragment())
            }
            else -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        changeNavigationStatus(menuItem)
        when (menuItem.itemId) {
            R.id.basket -> {
                setFragment(BasketFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.play_bill -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.my_tickets -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.account -> {
                setFragment(LoginFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    var menuOutlinedImages = mutableMapOf(
        R.id.basket to R.drawable.ic_cart_outlined,
        R.id.play_bill to R.drawable.ic_calendar_outlined,
        R.id.my_tickets to R.drawable.ic_ticket_outlined,
        R.id.account to R.drawable.ic_account_outlined
    )

    var menuImages = mutableMapOf(
        R.id.basket to R.drawable.ic_cart,
        R.id.play_bill to R.drawable.ic_calendar,
        R.id.my_tickets to R.drawable.ic_ticket,
        R.id.account to R.drawable.ic_account
    )

    private fun changeNavigationStatus(menuItem: MenuItem){
        menuItem.icon = ContextCompat.getDrawable(
            requireContext(),
            menuOutlinedImages[menuItem.itemId]!!
        )
        bottom_navigation.menu.forEach {
            if(it.itemId != menuItem.itemId){
                it.icon = ContextCompat.getDrawable(requireContext(), menuImages[it.itemId]!!)
            }
        }
    }
}