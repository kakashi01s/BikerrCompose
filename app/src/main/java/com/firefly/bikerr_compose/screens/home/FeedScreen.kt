package com.firefly.bikerr_compose.screens.home

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.firefly.bikerr_compose.activities.*
import com.firefly.bikerr_compose.model.Blog
import com.firefly.bikerr_compose.model.Feed
import com.firefly.bikerr_compose.viewmodel.MainViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@Composable
fun FeedScreen(mainActivityCompose: MainActivityCompose, mainViewModel: MainViewModel) {
    var feedlist by remember { mutableStateOf(listOf<Feed>()) }

Scaffold(topBar = {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
    ) {

        Text( text = "Bikerr", fontWeight = FontWeight.Bold, fontSize = 30.sp)
        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Card(shape = RoundedCornerShape(20.dp), elevation = 5.dp, backgroundColor = Color.LightGray) {
            IconButton(onClick = {
                    val intent = Intent(mainActivityCompose, ChannelActivity::class.java)
                    mainActivityCompose.startActivity(intent)
                }) {
                    BadgedBox(badge = {
                        if (mainViewModel.unreadMessages.value > 0) {
                            Badge {
                                Text("${mainViewModel.unreadMessages.value}")
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Outlined.Chat, contentDescription = "add post")
                    }
                }
            }


        }
    }
})
{
    Divider(color = Color.LightGray, thickness = 5.dp)

    JetFirestore(path = {
        collection("Feed")
    },
        onRealtimeCollectionFetch = { values, _ ->
            //When all documents are fetched
            //booksList = values.getListOfObjects()
            //When documents are fetched based on limit
            feedlist =  values.getListOfObjects()
        }) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
            feedlist.reversed().let {
                LazyColumn(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly) {
                    items(it){
                   //     getFeedItem(it,mainActivityCompose)
                    }
                }
            }
        }

    }
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
            mainViewModel.blogList.value.let {
                LazyColumn(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly) {
                    items(it){
                        FeedItem(blog = it)
                    }
                }
            }
        }

    Divider(color = Color.Transparent, thickness = 50.dp)



}
}

@Composable
fun FeedItem(blog: Blog) {

    Card(
        Modifier
            .padding(15.dp)
            .fillMaxSize()
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(25.dp)
            )
    ,
        elevation = 5.dp
    )
    {

        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
        )
        {
            Row(Modifier.padding(5.dp)) {
                Text(text = blog.blogHeading!!, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)

            }
//Feed Image
            GlideImage( // CoilImage, FrescoImage
                imageModel = blog.blogImage,
                modifier = Modifier
                    .size(360.dp)
                    .padding(10.dp),
                alignment = Alignment.Center,
                shimmerParams = ShimmerParams(
                    baseColor = MaterialTheme.colors.background,
                    highlightColor = Color.LightGray,
                    durationMillis = 350,
                    dropOff = 0.65f,
                    tilt = 20f
                )
                ,

                // shows an error text if fail to load an image.
                failure = {
                    Text(text = "image request failed.")
                })
// Caption

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(color = Color.DarkGray),
                horizontalArrangement = Arrangement.Start)
            {
                Box(modifier = Modifier.padding(10.dp))
                {
                    Column()
                    {
                        Text(text = "More:")
                        Text(text = blog.blogData!!)
                    }

                }
            }
        }
    }
}



