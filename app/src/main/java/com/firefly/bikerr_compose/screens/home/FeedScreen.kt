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
import androidx.compose.material.icons.filled.*
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
import com.firefly.bikerr_compose.model.Feed
import com.firefly.bikerr_compose.viewmodel.ViewModelmain
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects
import com.skydoves.landscapist.glide.GlideImage
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.ChatEventListener
import io.getstream.chat.android.client.events.ChatEvent
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@Composable
fun FeedScreen(mainActivityCompose: MainActivityCompose, mainViewModel: ViewModelmain) {
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
            IconButton(onClick = {
                val intent = Intent(mainActivityCompose,ChannelActivity::class.java)
                mainActivityCompose.startActivity(intent)
            }) {
                BadgedBox(badge = { if (mainViewModel.unreadMessages.value > 0) {
                    Badge {
                        Text("${mainViewModel.unreadMessages.value}")
                    }
                } }) {
                    Icon(imageVector = Icons.Outlined.Chat, contentDescription = "add post")
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
            .height(650.dp)) {
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
}
}

@Composable
fun FeedItem(feed: Feed, uImage: String, uname: String, mainActivityCompose: MainActivityCompose) {
    val coroutineScope = rememberCoroutineScope()
    val db = FirebaseFirestore.getInstance()
    val feedLikes = remember {
        mutableStateOf(0)
    }

    feedLikes.value = feed.likes

    Box(
        Modifier
            .padding(5.dp)
            .height(500.dp)
            .fillMaxWidth()
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(25.dp)
            ))
    {

        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceAround)
        {

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
// Profile Image
Image(painter = rememberImagePainter(data = uImage), contentDescription = "",
    modifier = Modifier
        .size(30.dp)
        .clip(RoundedCornerShape(20.dp)),
    alignment = Alignment.Center)
                Spacer(modifier = Modifier.size(10.dp))
                // Profile Name
                Text(modifier = Modifier.fillMaxWidth(),
                    text = uname,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }
//Feed Image
            GlideImage( // CoilImage, FrescoImage
                imageModel = feed.image,
                modifier = Modifier.size(380.dp),
                alignment = Alignment.Center,
                // shows an indicator while loading an image.
                loading = {
                    Box(modifier = Modifier.matchParentSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                // shows an error text if fail to load an image.
                failure = {
                    Text(text = "image request failed.")
                })
// Caption
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = uname,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(text = feed.caption)
            }
// Like and Comment Button
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start)
            {
                //Like Button
                Box(Modifier.clickable {
                }) {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            feedLikes.value = feedLikes.value + 1
                            val post = Feed(feed.caption,   0, feed.image,  feedLikes.value,  uid = feed.uid,feed.feedId)
                            db.collection("Feed").document(feed.feedId).set(post).addOnCompleteListener {
                                Log.d("like", " you liked Post : ${feed.feedId}")
                            }
                        }

                    }) {
                        Icon(
                            imageVector = Icons.Outlined.ThumbUp,
                            contentDescription = "")
                    }
                }

                Text(text = feedLikes.value.toString())

                Spacer(modifier = Modifier.size(5.dp))

                    IconButton(onClick = {

                    }) {
                        Icon(
                            imageVector = Icons.Outlined.AddComment,
                            contentDescription = "")
                    }
                }
        }
    }
}



