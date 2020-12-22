package ru.android.romashkaapp.payment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.android.synthetic.main.fragment_stadium.*
import ru.android.romashkaapp.R
import ru.android.romashkaapp.base.BaseFragment
import ru.android.romashkaapp.basket.BasketFragment
import ru.android.romashkaapp.databinding.FragmentPaymentBinding

/*
 * Created by yasina on 22.12.2020
*/
class PaymentFragment : BaseFragment() {

    lateinit var binding: FragmentPaymentBinding
    private val viewModel: PaymentViewModel by viewModels()
    private var orderId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        requireArguments().containsKey(BasketFragment.ORDER_ID).let {
            orderId = requireArguments().getInt(BasketFragment.ORDER_ID)
            binding.viewModel!!.setOrderId(orderId)
        }

        parentFragmentManager.fragments.forEach {
            if(it::class.java.name.contains("BasketFragment")){
                childFragmentManager.popBackStack()
                it.arguments = bundleOf(BasketFragment.SUCCESS_PAYMENT to true)
                return@forEach
            }
        }

        val webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if(url!!.contains("success?Order_ID=$orderId")){ //700506

                    parentFragmentManager.fragments.forEach {
                        if(it::class.java.name.contains("BasketFragment")){
                            childFragmentManager.popBackStack()
                            it.arguments = bundleOf(BasketFragment.SUCCESS_PAYMENT to true)
                            return@forEach
                        }
                    }
                }
            }

        }

        wv.settings.javaScriptEnabled = true
        wv.settings.loadWithOverviewMode = true
        wv.settings.useWideViewPort = true

        binding.viewModel!!.webView.observe(viewLifecycleOwner, {
            wv.visibility = VISIBLE
            iv_event_not_found.visibility = GONE
            tv_you_are_in_a_payment_page.visibility = GONE
            tv_test_payment_text.visibility = GONE
            mb_pay.visibility = GONE

            wv.webViewClient = webViewClient
            wv.loadUrl(it)
        })
    }
}