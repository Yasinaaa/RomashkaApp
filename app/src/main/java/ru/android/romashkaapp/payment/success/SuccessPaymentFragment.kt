package ru.android.romashkaapp.payment.success

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.fragment_success_payment.*
import ru.android.romashkaapp.R
import ru.android.romashkaapp.base.BaseFragment
import ru.android.romashkaapp.basket.BasketFragment
import ru.android.romashkaapp.databinding.FragmentSuccessPaymentBinding
import ru.android.romashkaapp.utils.removeZero

/*
 * Created by yasina on 22.12.2020
*/
class SuccessPaymentFragment : BaseFragment() {

    companion object{
        val ORDER_SUM = "ORDER_SUM"
        val ORDER_EMAIL = "ORDER_EMAIL"
    }

    lateinit var binding: FragmentSuccessPaymentBinding
    private val viewModel: SuccessPaymentViewModel by viewModels()
    private var orderId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_success_payment, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireArguments().containsKey(BasketFragment.ORDER_ID).let {
            orderId = requireArguments().getInt(BasketFragment.ORDER_ID)
            orderId = 700506

            viewModel.setOrderId(orderId)
            tv_order_price.text = String.format(getString(R.string.rub), requireArguments().getFloat(ORDER_SUM).removeZero())
            setEmail(requireArguments().getString(ORDER_EMAIL))
        }

    }

    private fun setEmail(email: String?){
        val text = String.format(getString(R.string.we_send_tickets_to_the_email), email)
        val spannableString = SpannableString(text)

        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            text.lastIndex - email!!.length, text.lastIndex + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        tv_we_send_tickets_to_the_email.setText(spannableString, TextView.BufferType.SPANNABLE)
    }
}