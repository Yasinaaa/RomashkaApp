package ru.android.romashkaapp.signin

import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.fragment_basket.*
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_user_data.*
import ru.android.romashkaapp.R
import ru.android.romashkaapp.databinding.FragmentSignInBinding
import ru.android.romashkaapp.databinding.FragmentUserDataBinding
import ru.android.romashkaapp.main.MainViewModel

/**
 * Created by yasina on 05.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class SignInFragment : Fragment() {

    lateinit var binding: FragmentUserDataBinding
    private val viewModel: SignInViewModel by viewModels()
    private lateinit var mainViewModel: MainViewModel
    private var isReadyToSave = mutableMapOf("login" to false, "password" to false)

    fun setViewModel(viewModel: MainViewModel) {
        this.mainViewModel = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_data, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setSupportFieldView()
//        binding.visibilityOfNewUser = View.GONE
//        binding.visibilityOfExistUser = View.VISIBLE

//        viewModel.login.observe(viewLifecycleOwner, {
//            if(it.isNotEmpty()) {
//                isReadyToSave["login"] = it.isNotEmpty()
//                isEmptyTurnstile()
//                tif_email.isErrorEnabled = false
//            }else{
//                tif_email.isErrorEnabled = true
//                tif_email.error = requireContext().getString(R.string.wrong_email_format)
//            }
//        })
//        viewModel.password.observe(viewLifecycleOwner, {
//            if(it.isNotEmpty()) {
//                isReadyToSave["password"] = it.isNotEmpty()
//                isEmptyTurnstile()
//                tif_email.isErrorEnabled = false
//            }else{
//                tif_email.isErrorEnabled = true
//                tif_email.error = requireContext().getString(R.string.wrong_password_format)
//            }
//        })
//        viewModel.error.observe(viewLifecycleOwner, {
//            Utils.hideKeyboardFrom(requireContext(), binding.root)
//            Utils.showSnackBar(requireContext(), requireView(), it)
//        })


        //user data
        tv_create_account.setText(Html.fromHtml(getString(R.string.privacy_policy)))
    }

    private fun setSupportFieldView(){
        val text = getString(R.string.support_phone)
        val spannableString = SpannableString(text)
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            text.lastIndex - 28, text.lastIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        mb_support.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

    private fun isEmptyTurnstile(){
        var needToSave = true
        for((_, value) in isReadyToSave){
            if (!value)
                needToSave = value
        }
        if (needToSave) {
            viewModel.buttonOpacity.set(1f)
        } else {
            viewModel.buttonOpacity.set(0.58f)
        }
    }
}