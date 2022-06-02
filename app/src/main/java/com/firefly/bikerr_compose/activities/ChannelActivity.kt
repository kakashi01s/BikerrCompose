package com.firefly.bikerr_compose.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.firefly.bikerr_compose.R
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QuerySort
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.channels.ChannelViewModelFactory

class ChannelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            Bikerr_composeTheme {

                val channelId = intent.getStringExtra("channelId")
                val isFromNotification = intent.getBooleanExtra("isFromNotification", false)
                if (isFromNotification) {
                    val intent = Intent(this, MessagesActivity::class.java)
                    intent.putExtra("channelId",channelId)
                    startActivity(intent)

                }

                Scaffold(
                    topBar = {
                        Row(
                            modifier = Modifier
                                .padding(10.dp)
                                .height(40.dp)
                                .fillMaxWidth(),

                            ) {
                            Text(text = "My Chats", fontWeight = FontWeight.Bold, fontSize = 30.sp)
                            Row(Modifier.padding(start = 30.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {

                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                                    Button(onClick = {
                                        val intent = Intent(this@ChannelActivity,CommunityCreateActivity::class.java)
                                        startActivity(intent)
                                    }) {
                                        Text(text = "Create")
                                    }
                                    Button( onClick = {
                                        val intent =
                                            Intent(this@ChannelActivity, CommunityActivity::class.java)
                                            startActivity(intent)
                                    }) {
                                        Text(text = "Join")
                                    }
                                }
                            }
                        }
                    }
                ) {
                    ChatTheme(isInDarkMode = true) {
                        ChannelsScreen(
                            title = stringResource(id = R.string.app_name),
                            isShowingHeader = false,
                            isShowingSearch = false,
                            onItemClick = ::openMessages,
                            onBackPressed = ::finish,

                            )
                    }


                }
            }
        }
    }
    /**
     * An example of a screen UI that's much more simple than the ChannelsScreen component, that features a custom
     * ChannelList item.
     */


    private fun openMessages(channel: Channel) {
        startActivity(MessagesActivity.createIntent(this, channel.cid))
    }


}