package com.firefly.bikerr_compose.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firefly.bikerr_compose.R
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.google.android.libraries.places.internal.it
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QuerySort
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.state.channels.list.ChannelItemState
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.channels.header.ChannelListHeader
import io.getstream.chat.android.compose.ui.channels.info.SelectedChannelMenu
import io.getstream.chat.android.compose.ui.channels.list.ChannelItem
import io.getstream.chat.android.compose.ui.channels.list.ChannelList
import io.getstream.chat.android.compose.ui.components.SearchInput
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.channels.ChannelListViewModel
import io.getstream.chat.android.compose.viewmodel.channels.ChannelViewModelFactory
import io.getstream.chat.android.offline.extensions.globalState

class ChannelActivity : AppCompatActivity() {
    private val factory by lazy {
        ChannelViewModelFactory(
            ChatClient.instance(),
            QuerySort.desc("last_updated"),
            Filters.and(
                Filters.eq("type", "messaging"),
                Filters.`in`("members", listOf(ChatClient.instance().getCurrentUser()?.id ?: ""))
            )
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * To use the Compose SDK/Components, simply call [setContent] to provide a Compose UI
         * definition, in which you gain access to all the UI component functions.
         *
         * You can use the default [ChannelsScreen] component that sets everything up for you,
         * or build a custom component yourself, like [MyCustomUi].
         */
        setContent {
            ChatTheme(isInDarkMode = true) {
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
                            IconButton(onClick = {
                                val intent = Intent(this@ChannelActivity,CommunityActivity::class.java)
                                startActivity(intent)
                            }) {
                               Icon(imageVector = Icons.Default.Search, contentDescription = "")
                            }
                        }
                    }
                }) {
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
    /**
     * An example of a screen UI that's much more simple than the ChannelsScreen component, that features a custom
     * ChannelList item.
     */


    private fun openMessages(channel: Channel) {
        startActivity(MessagesActivity.createIntent(this, channel.cid))
    }


}