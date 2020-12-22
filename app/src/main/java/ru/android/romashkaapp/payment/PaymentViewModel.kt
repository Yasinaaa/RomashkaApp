package ru.android.romashkaapp.payment

import android.app.Application
import android.ru.romashkaapp.usecases.OrderUseCase
import android.view.View
import androidx.lifecycle.MutableLiveData
import okhttp3.ResponseBody
import ru.android.romashkaapp.BaseSubscriber
import ru.android.romashkaapp.R
import ru.android.romashkaapp.StartActivity
import ru.android.romashkaapp.base.BaseViewModel
import ru.android.romashkaapp.utils.Utils

/*
 * Created by yasina on 22.12.2020
*/
class PaymentViewModel(application: Application) : BaseViewModel(application), View.OnClickListener {

    private var orderUseCase: OrderUseCase? = null
    private var currentOrderId: Int = 0
    var webView = MutableLiveData<String>()

    init {
        orderUseCase = OrderUseCase(StartActivity.REPOSITORY, Utils.getAccessToken(application)!!)
    }

    fun setOrderId(orderId: Int){
        currentOrderId = orderId
    }

    private fun setOrderId(){
        orderUseCase!!.payOrder(currentOrderId, PayOrderSubscriber())
    }

    private inner class PayOrderSubscriber: BaseSubscriber<ResponseBody>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: ResponseBody) {
            super.onNext(response)
            webView.value = response.string().replace("\"", "")
        }
    }

    override fun onClick(view: View?) {
        if(R.id.mb_pay == view!!.id){
            setOrderId()
        }
    }
}