package ru.android.romashkaapp.payment.success

import android.app.Application
import android.ru.romashkaapp.usecases.OrderUseCase
import android.util.Log
import android.view.View
import okhttp3.ResponseBody
import ru.android.romashkaapp.BaseSubscriber
import ru.android.romashkaapp.R
import ru.android.romashkaapp.StartActivity
import ru.android.romashkaapp.base.BaseViewModel
import ru.android.romashkaapp.utils.SvgFromXmlCreater.Companion.savePdfToTheDevice
import ru.android.romashkaapp.utils.Utils

/*
 * Created by yasina on 22.12.2020
*/
class SuccessPaymentViewModel(application: Application) : BaseViewModel(application), View.OnClickListener {

    private var orderUseCase: OrderUseCase? = null
    private var currentOrderId: Int = 0

    init {
        orderUseCase = OrderUseCase(StartActivity.REPOSITORY, Utils.getAccessToken(application)!!)
    }

    fun setOrderId(orderId: Int){
        currentOrderId = orderId
    }

    override fun onClick(view: View?) {
        if(view!!.id == R.id.mb_download){
            orderUseCase!!.getTickets(currentOrderId, TicketsSubscriber())
        }
    }

    private inner class TicketsSubscriber: BaseSubscriber<ResponseBody>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: ResponseBody) {
            super.onNext(response)
            Log.d("f", "is=" + savePdfToTheDevice(context, response, currentOrderId))
        }
    }

}