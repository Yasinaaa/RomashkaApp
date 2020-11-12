package ru.android.romashkaapp.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.fragment_login.*
import ru.android.romashkaapp.R
import ru.android.romashkaapp.login.LoginViewModel
import ru.android.romashkaapp.databinding.FragmentLoginBinding
import ru.android.romashkaapp.main.MainViewModel
import ru.android.romashkaapp.utils.Utils

/**
 * Created by yasina on 05.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var mainViewModel: MainViewModel
    private var isReadyToSave = mutableMapOf("login" to false, "password" to false)

    fun setViewModel(viewModel: MainViewModel) {
        this.mainViewModel = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.login.observe(viewLifecycleOwner, {
            if(it.isNotEmpty()) {
                isReadyToSave["login"] = it.isNotEmpty()
                isEmptyTurnstile()
                tif_email.isErrorEnabled = false
            }else{
                tif_email.isErrorEnabled = true
                tif_email.error = requireContext().getString(R.string.wrong_email_format)
            }
        })
        viewModel.password.observe(viewLifecycleOwner, {
            if(it.isNotEmpty()) {
                isReadyToSave["password"] = it.isNotEmpty()
                isEmptyTurnstile()
                tif_email.isErrorEnabled = false
            }else{
                tif_email.isErrorEnabled = true
                tif_email.error = requireContext().getString(R.string.wrong_password_format)
            }
        })
        viewModel.error.observe(viewLifecycleOwner, {
            Utils.hideKeyboardFrom(requireContext(), binding.root)
            Utils.showSnackBar(requireContext(), requireView(), it)
        })
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