package com.firefly.bikerr_compose.screens.home

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
import androidx.compose.material.icons.outlined.AddComment
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.ThumbUp
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(mainActivityCompose: MainActivityCompose) {
    var feedlist by remember { mutableStateOf(listOf<Feed>()) }
    val bodyContent = remember { mutableStateOf("Body Content Here") }
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
                val intent = Intent(mainActivityCompose,CreatepostActivity::class.java)
                mainActivityCompose.startActivity(intent)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add post")
            }
            IconButton(onClick = {
                val intent = Intent(mainActivityCompose,ChannelActivity::class.java)
                mainActivityCompose.startActivity(intent)
            }) {
                Icon(imageVector = Icons.Outlined.Chat, contentDescription = "add post")
            }
            Row() {
                TopAppBarDropdownMenu(bodyContent = bodyContent,mainActivityCompose)
            }

        }
    }
})
{

    JetFirestore(path = {
        collection("Feed")
    },
        onRealtimeCollectionFetch = { values, exception ->
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
                        getFeedItem(it,mainActivityCompose)
                    }
                }
            }
        }

    }
}
}

@Composable
fun getFeedItem(feed: Feed, mainActivityCompose: MainActivityCompose) {
    var uname = remember { mutableStateOf("")}
    var uImage = remember { mutableStateOf("")}
    var feedlikes = remember { mutableStateOf(0)}
    Log.d("nnnn", feed.uid)
    val databaseRef: DatabaseReference?
    databaseRef = FirebaseDatabase.getInstance().getReference("Users")
    databaseRef.child(feed.uid).get().addOnSuccessListener {
            if (it.exists())
            {
                 uname.value = it.child("userName").value as String
                 uImage.value = it.child("userImage").value as String
            }
            else
            {
                Log.d("feed", "Failed to get uname")
            }
        }
    FeedItem(feed,uImage.value,uname.value,mainActivityCompose)

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
                Image(
                    alignment = Alignment.Center,
                    painter = rememberImagePainter(uImage),
                    contentDescription = "",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
                Spacer(modifier = Modifier.size(10.dp))
                // Profile Name
                Text(modifier = Modifier.fillMaxWidth(),
                    text = uname,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }
//Feed Image
            Image(
                modifier = Modifier.size(380.dp),
                alignment = Alignment.Center,
                painter = rememberImagePainter(feed.image),
                contentDescription = ""
            )
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
                        val intent = Intent(mainActivityCompose,CommentActivity::class.java)
                        intent.putExtra("feedId", feed.feedId)
                        mainActivityCompose.startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.AddComment,
                            contentDescription = "")
                    }
                }



        }
    }
}



@Composable
fun TopAppBarDropdownMenu(
    bodyContent: MutableState<String>,
    mainActivityCompose: MainActivityCompose
) {
    val expanded = remember { mutableStateOf(false) } // 1

    Box(
        Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = {
            expanded.value = true // 2
            bodyContent.value =  "Menu Opening"
        }) {
            Icon(
                Icons.Filled.MoreVert,
                contentDescription = "More Menu"
            )
        }
    }

    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
    ) {
        DropdownMenuItem(onClick = {
            expanded.value = false // 3

        }) {
            Text("Settings")
            Icon(imageVector = Icons.Default.Settings, contentDescription = null)
        }

        Divider()

        DropdownMenuItem(onClick = {
            expanded.value = false
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(mainActivityCompose,LoginActivity::class.java)
            mainActivityCompose.startActivity(intent)
            mainActivityCompose.finish()
        }) {
            Text("Logout")
            Icon(imageVector = Icons.Default.Logout, contentDescription = null)

        }


    }
}