package android.ru.romashkaapp.models.enums

/**
 * Created by yasina on 12.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
enum class OrderStatus(var data: Int) {
    CREATED_STATUS(-1),
    EXPECT_STATUS(12),
    EXPECT_BO_STATUS(11),
    EXPECT_PAY_STATUS(1),
    PAYED_STATUS(22),
    SUCCESS_STATUS(21),
    SENDING_STATUS(211),
    SENT_STATUS(2),
    LOYALTY_STATUS(20),
    CANCEL_MERCHANT_STATUS(32),
    CANCEL_STATUS(31),
    CANCEL_BO_STATUS(3),
    CANCEL_LOYALTY_STATUS(30),
    RETURN_STATUS(4)
}