package com.firefly.bikerr_compose.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.screens.login.TextFieldState
import com.google.firebase.auth.FirebaseAuth
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Channel

class CommunityCreateActivity : ComponentActivity() {
    private val client = ChatClient.instance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val focusManager = LocalFocusManager.current
            val channelName = remember { TextFieldState() }
            val channelCity = remember { TextFieldState() }
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            Bikerr_composeTheme {
                Scaffold(topBar = {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),

                        ) {
                        Text(text = "Community", fontWeight = FontWeight.Bold, fontSize = 30.sp)
                       
                    }
                }) {
                        Column {
                            ChannelNameTextField(
                                focusManager = focusManager,
                                username = channelName
                            )
                            ChannelcityTextField(
                                focusManager = focusManager,
                                username = channelCity
                            )
                            Button(onClick = {
                                    val name: String = channelName.text
                                    val city = channelCity.text
                                    val id = name + city
                                    val hashMap : HashMap<String, Any>
                                            = HashMap()
                                    //adding elements to the hashMap using
                                    // put() function
                                hashMap["name"] = name
                                    val channelClient = client.channel(channelType = "messaging", channelId = id)
                                    channelClient.create(memberIds = listOf(uid) ,extraData =  hashMap).enqueue { result ->
                                        if (result.isSuccess) {
                                            val newChannel: Channel = result.data()

                                            Log.d("newchannel", newChannel.id)
                                            val intent = Intent(this@CommunityCreateActivity , ChannelActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                            Toast.makeText(this@CommunityCreateActivity, "Channel Created", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Log.d("newchannel", result.error().message.toString())
                                            Toast.makeText(this@CommunityCreateActivity, "Error Creating Channel", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }) {
                                Text(text = "Create")
                            }
                        }
                    
                    
                }
            }
        }
    }
}


@Composable
private fun ChannelNameTextField(username : TextFieldState = remember { TextFieldState() },focusManager: FocusManager) {

    OutlinedTextField(
        value = username.text,
        onValueChange = { username.text = it },
        label = { Text(text = "Name") },
        leadingIcon = { Icon(imageVector = Icons.Filled.Person, contentDescription = "Usericon") },
        modifier = Modifier.fillMaxWidth(),
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Down
            )
        }),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
        maxLines = 1
    )
}

@Composable
private fun ChannelcityTextField(username : TextFieldState = remember { TextFieldState() },focusManager: FocusManager) {

    OutlinedTextField(
        value = username.text,
        onValueChange = { username.text = it },
        label = { Text(text = "City") },
        leadingIcon = { Icon(imageVector = Icons.Filled.Person, contentDescription = "Usericon") },
        modifier = Modifier.fillMaxWidth(),
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Down
            )
        }),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
        maxLines = 1
    )
}