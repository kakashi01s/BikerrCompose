package com.firefly.bikerr_compose.model

class Order {
    private var keyId: String = ""
    private var orderId: String = ""

    fun getKeyId(): String {
        return keyId
    }

    fun getOrderId(): String {
        return orderId
    }
}
