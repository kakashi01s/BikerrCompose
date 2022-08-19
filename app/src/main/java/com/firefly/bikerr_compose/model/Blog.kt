package com.firefly.bikerr_compose.model

data class Blog(var blogId: String, val blogImage: String?, val blogData: String?, val blogHeading: String?)
{
    constructor(): this("","","","")
}

