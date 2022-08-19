package com.firefly.bikerr_compose.screens.me

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.firefly.bikerr_compose.activities.*
import com.firefly.bikerr_compose.model.Users
import com.firefly.bikerr_compose.screens.login.TextFieldState
import com.firefly.bikerr_compose.screens.me.listing.ImageUriState
import com.firefly.bikerr_compose.viewmodel.MainViewModel
import com.google.android.gms.common.api.Api
import com.google.firebase.auth.FirebaseAuth
import com.pixplicity.easyprefs.library.Prefs
import com.skydoves.landscapist.glide.GlideImage
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Device
import io.getstream.chat.android.client.models.PushProvider

@Composable
fun MeScreen(mainActivityCompose: MainActivityCompose, mainViewModel: MainViewModel) {
    val bodyContent = remember { mutableStateOf("Body Content Here") }
    Scaffold(topBar = {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row( horizontalArrangement = Arrangement.Start) {
                Text(text = mainViewModel.user.value.userName, fontWeight = FontWeight.Bold, fontSize = 30.sp)
            }
            Row(horizontalArrangement = Arrangement.End) {
                TopAppBarDropdownMenu(bodyContent = bodyContent,mainActivityCompose)
            }
        }

    }) {
Column(Modifier.fillMaxWidth()) {

    Divider(color = Color.LightGray, thickness = 5.dp)
        Column(Modifier.padding(15.dp)) {
            ProfileHeaderMe(user = mainViewModel.user, activity = mainActivityCompose)
            Spacer(modifier = Modifier.height(10.dp))
            MyBookings {
                val intent = Intent(mainActivityCompose, MyBookingsActivity::class.java)
                mainActivityCompose.startActivity(intent)
            }
            Spacer(modifier = Modifier.height(10.dp))
            MyOrders {
                val intent = Intent(mainActivityCompose, MyBookingsActivity::class.java)
                mainActivityCompose.startActivity(intent)
            }
        }
        Divider(color = Color.LightGray, thickness = 2.5.dp)

    }
    }
}

@Composable
fun ProfileHeaderMe(user: MutableState<Users>, activity: MainActivityCompose) {
    val email = remember { TextFieldState() }
    val userName = remember { TextFieldState() }
    val phoneNumber = remember { TextFieldState() }
    val profileImageUri = remember { ImageUriState() }
    profileImageUri.uri = user.value.userImage.toUri()
    userName.text = user.value.userName
    phoneNumber.text = user.value.userPhone
    email.text = user.value.userEmail
    Log.d("uriii",profileImageUri.uri.toString())
    Row {
        //image row
        Row (Modifier.fillMaxWidth()){
            Card(modifier = Modifier.fillMaxWidth(),backgroundColor = Color.LightGray
            , border =  BorderStroke(2.dp, Color.LightGray), elevation = 10.dp) {

                Row(modifier = Modifier.padding(10.dp)) {

                    Box(
                        modifier = Modifier
                            .height(80.dp)
                            .width(80.dp)
                            .clip(CircleShape)
                    ) {
                        GlideImage( // CoilImage, FrescoImage
                            imageModel = profileImageUri.uri,
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
                    }

                    Column(
                        modifier = Modifier.padding(start = 10.dp),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Row {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "")
                            Divider(Modifier.size(5.dp), color = Color.Transparent)
                            Text(text = userName.text)
                        }
                        Row() {
                            Icon(imageVector = Icons.Default.Email, contentDescription = "")
                            Divider(Modifier.size(5.dp), color = Color.Transparent)
                            Text(text = email.text)
                        }
                        Row() {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = "")
                            Divider(Modifier.size(5.dp), color = Color.Transparent)
                            Text(text = phoneNumber.text)
                        }
                    }
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
        contentAlignment = Alignment.TopEnd
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
            val intent = Intent(mainActivityCompose,MyProfileActivity::class.java)
            mainActivityCompose.startActivity(intent)
        }) {
            Text("Edit ")
            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
        }
        Divider()
        DropdownMenuItem(onClick = {
            expanded.value = false
            FirebaseAuth.getInstance().signOut()
                    Prefs.clear()
                    val intent = Intent(mainActivityCompose, LoginActivity::class.java)
                    mainActivityCompose.startActivity(intent)
                    mainActivityCompose.finish()
        }) {
            Text("Logout")
            Icon(imageVector = Icons.Default.Logout, contentDescription = null)

        }


    }
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyBookings(onClick: () -> Unit) {

    Card(modifier = Modifier.fillMaxWidth(),border = BorderStroke(2.dp, Color.LightGray), onClick = {
        onClick.invoke()
    }) {
        Row(
            Modifier
                .padding(10.dp)
                .height(50.dp)
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Row(horizontalArrangement = Arrangement.End) {
                Icon(imageVector = Icons.Default.BookOnline, contentDescription = "" )
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "My Bookings",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyOrders(onClick: () -> Unit) {

    Card(shape = RoundedCornerShape(10.dp),modifier = Modifier.fillMaxWidth(),border = BorderStroke(2.dp, Color.LightGray), onClick = {
        onClick.invoke()
    }) {
        Row(
            Modifier
                .padding(10.dp)
                .height(50.dp)
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Row(horizontalArrangement = Arrangement.End) {
                Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = "" )
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "My Orders",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

        }
    }
}

