package com.firefly.bikerr_compose.model

class Order {
    private var keyId: String = ""
    private var orderId: String = ""

    public fun getKeyId(): String {
        return keyId
    }

    public fun getOrderId(): String {
        return orderId
    }
}
