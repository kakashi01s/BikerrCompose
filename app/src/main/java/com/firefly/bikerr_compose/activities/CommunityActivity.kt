package com.firefly.bikerr_compose.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContentProviderCompat.requireContext
import coil.compose.rememberImagePainter
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.google.android.libraries.places.internal.fi
import com.google.firebase.auth.FirebaseAuth
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.client.api.models.QuerySort
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters

class CommunityActivity : ComponentActivity() {
    private val client = ChatClient.instance()
    private val channels = mutableStateOf(listOf<Channel>())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bikerr_composeTheme {
                getChannels()
                // A surface container using the 'background' color from the theme
                Scaffold(topBar = {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),

                        ) {
                        Text(text = "Community", fontWeight = FontWeight.Bold, fontSize = 30.sp)
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                          Button(onClick = {
                              val intent = Intent(this@CommunityActivity,CommunityCreateActivity::class.java)
                              startActivity(intent)
                          }) {
                              Text(text = "Create")
                          }
                        }
                    }
                }) {
                    channels.value.let {
                        LazyColumn(){
                            items(it){
                               ChannelItem(it)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivityCompose::class.java)
        startActivity(intent)
        finish()
    }
    fun getChannels(){
        val request = QueryChannelsRequest(
            filter = Filters.and(
                Filters.eq("type", "messaging"),
            ),
            offset = 0,
            limit = 10,
            querySort = QuerySort.desc("last_message_at")
        ).apply {
            watch = true
            state = true
        }

        client.queryChannels(request).enqueue { result ->
            if (result.isSuccess) {
              channels.value = result.data()
                Log.d("searchch" , channels.toString())
            } else {
                // Handle result.error()
                Log.d("searchch", result.error().message.toString())
               // Toast.makeText(requireContext(), "Error Fetching Channels", Toast.LENGTH_SHORT).show()
            }
        }

    }

    @Composable
    fun ChannelItem(channel: Channel) {
                    Column(Modifier.fillMaxWidth().padding(10.dp)) {
                        Row(Modifier.fillMaxWidth()) {
                            Text(text = channel.name, fontWeight = FontWeight.ExtraBold)
                        }
                        Row(Modifier.fillMaxWidth()) {
                            Text(text = "Created By :")
                            Text(text = channel.createdBy.name)
                        }
                        Row(Modifier.fillMaxWidth()) {
                            Text(text = "Members :")
                            Text(text = channel.memberCount.toString())
                        }
                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            Button(onClick = {
                                val uid = FirebaseAuth.getInstance().currentUser!!.uid
                                val channelClient = client.channel("messaging", channel.id)
                                Log.d("cccc",channel.id)
                                // Add members with ids
                                channelClient.addMembers(listOf(uid)).enqueue { result ->
                                    if (result.isSuccess) {
                                        val channel: Channel = result.data()
                                        val intent =
                                            Intent(this@CommunityActivity, ChannelActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        // Handle result.error()
                                        Log.d("joinchannel", result.error().message.toString())
                                        Toast.makeText(
                                            this@CommunityActivity,
                                            "Error Joining Community",
                                            Toast.LENGTH_SHORT,
                                        ).show()
                                    }
                                }
                            })
                            {
                                Text(text = "Join")
                            }
                        }
                    }

                }
            }





