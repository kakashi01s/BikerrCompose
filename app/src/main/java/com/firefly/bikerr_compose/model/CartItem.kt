package com.firefly.bikerr_compose.model

data class CartItem(var name: String?, val price: Any?, val image: String?, var quantity : String?, var Size: String?)
{
    constructor() : this("", "", "","", "") {}
}
