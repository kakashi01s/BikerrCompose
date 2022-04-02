package com.firefly.bikerr_compose.utils

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun CircularProgressBar(
        isDisplayed: Boolean
) {
    if (isDisplayed) {
        CircularProgressIndicator()
    }
}