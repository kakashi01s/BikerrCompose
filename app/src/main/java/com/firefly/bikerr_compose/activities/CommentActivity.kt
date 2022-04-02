package com.firefly.bikerr_compose.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.model.Comments
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects

class CommentActivity : ComponentActivity() {
    var feed: DocumentSnapshot? = null
    @SuppressLint("RememberReturnType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val feedId = remember {
                mutableStateOf("")
            }
            feedId.value =intent.getStringExtra("feedId").toString()
            val commentList =  remember {
                mutableStateOf(listOf<Comments>())
            }
            getFeed(feedId)
            Bikerr_composeTheme {
                Scaffold(topBar = {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                    ) {
                        Text(text = "Bikerr", fontWeight = FontWeight.Bold, fontSize = 30.sp)
                    }
            }){
                    Image(
                        modifier = Modifier.size(380.dp),
                        alignment = Alignment.Center,
                        painter = rememberImagePainter(feed!!.get("image").toString()),
                        contentDescription = ""
                    )
                    Column() {
                        JetFirestore(path = {
                            collection("Feed").document(feedId.value).collection("commentsList")}
                            ,
                            onRealtimeCollectionFetch = { values, exception ->
                                //When all documents are fetched
                                //booksList = values.getListOfObjects()
                                //When documents are fetched based on limit
                                commentList.value = values.getListOfObjects()

                        }) {

                        }

                        commentList.value.let {
                            LazyColumn(){
                                items(it){
                                    CommentItem(it)
                                }
                            }
                        }
                    }
                    
                }
            }
        }
    }

    fun getFeed( feedId: MutableState<String>) {
        FirebaseFirestore.getInstance().collection("Feed").document(feedId.value).get().addOnSuccessListener {
            if (it.exists())
            {
                feed = it
            }
            else
            {
                Log.d("ffff", "Feed not found")
            }
        }
    }
}

@Composable
fun CommentItem( comment: Comments) {
    Row() {
        Text(text = comment.uid)
        Text(text = comment.comment)
    }
}



