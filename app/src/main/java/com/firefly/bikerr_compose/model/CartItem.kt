package com.firefly.bikerr_compose.model

data class CartItem(val product: Product?, var quantity : String?, var Size: String?,)
{
    constructor(): this(null,"","",)

}
