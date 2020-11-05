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

/**
 * Created by yasina on 05.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var mainViewModel: MainViewModel

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

    }
}