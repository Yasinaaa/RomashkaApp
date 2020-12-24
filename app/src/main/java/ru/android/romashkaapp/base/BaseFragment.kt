package ru.android.romashkaapp.base

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import ru.android.romashkaapp.R


/*
 * Created by yasina on 06.12.2020
*/

open class BaseFragment: Fragment() {

    private var callback: OnBackPressedCallback? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStackImmediate()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback!!)
    }

    fun setFragment(newFragment: Fragment){
        val transaction = parentFragmentManager.beginTransaction()
        transaction.add(R.id.nav_main_fragment, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun removeFragment(fragment: Fragment){
        val transaction = parentFragmentManager.beginTransaction()
        transaction.remove(fragment)
        transaction.commit()
    }

}