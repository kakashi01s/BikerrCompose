package com.firefly.bikerr_compose.screens.nearby

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.firefly.bikerr_compose.data.remote.placesDto.Places
import com.firefly.bikerr_compose.data.remote.placesDto.Result


@Preview
@Composable
fun Prevvv() {
    Box(Modifier.fillMaxWidth().padding(10.dp)) {
        Column(Modifier.fillMaxWidth()) {
            Image(painter = rememberImagePainter("stores.Image") , contentDescription = "",modifier = Modifier.size(50.dp))
            Text(text = "it.name")
            Row( horizontalArrangement = Arrangement.End , modifier = Modifier.fillMaxWidth()) {
                FloatingActionButton(backgroundColor = Color.White,contentColor = Color.Black,
                    onClick = {

                    },
                ) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.NearMe, contentDescription ="" )
                    }
                }
            }
        }
    }
    
}