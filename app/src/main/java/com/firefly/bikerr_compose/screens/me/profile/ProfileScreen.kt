package com.firefly.bikerr_compose.screens.me.profile

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberImagePainter
import com.firefly.bikerr_compose.activities.MyProfileActivity
import com.firefly.bikerr_compose.model.Users
import com.firefly.bikerr_compose.screens.login.TextFieldState
import com.firefly.bikerr_compose.screens.me.listing.ImageUriState
import com.firefly.bikerr_compose.viewmodel.ViewModelMyProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.pixplicity.easyprefs.library.Prefs
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun ProfileScreen(viewModel: ViewModelMyProfile, activity: MyProfileActivity) {
    Scaffold(topBar = {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
            ) {
            Row( horizontalArrangement =Arrangement.Start) {
                Text(text = "Profile", fontWeight = FontWeight.Bold, fontSize = 30.sp)
            }
        }

    }) {
        ProfileHeader(user = viewModel.user,activity)
    }
}
@Composable
fun ProfileHeader(user: MutableState<Users>, activity: MyProfileActivity) {
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
        Row {
            Column(modifier = Modifier.padding(10.dp)) {

                Box(modifier = Modifier
                    .height(80.dp)
                    .width(80.dp)
                    .clip(CircleShape)) {
                    ProfileImagePicker(imageUri = profileImageUri)
                }
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    UsernameTextFieldPr(username = userName)
                    EmailTextFieldPr(email)
                    PhoneNumberTextFieldPr(phoneNumber)
                    ButtonToUpdatePr {
                        val firebaseUser = FirebaseAuth.getInstance().currentUser!!.uid
                        Prefs.putString("userName",userName.text)
                        Prefs.putString("userEmail",email.text)
                        Prefs.putString("userPhone",phoneNumber.text)
                        Prefs.putString("userImage",profileImageUri.uri.toString())
                        val refStorage =
                            FirebaseStorage.getInstance().reference.child("Profile/${firebaseUser}")
                        refStorage.putFile(profileImageUri.uri!!)
                            .addOnSuccessListener { taskSnapshot ->
                                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                                    profileImageUri.uri = uri
                                    Log.d("update", profileImageUri.uri.toString())
                                    val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
                                    val users = Users(userName.text,email.text,phoneNumber.text,profileImageUri.uri.toString(),firebaseUser)
                                    updateProfile(users,firebaseUser,databaseRef,activity)
                                }

                            }
                            .addOnFailureListener { e ->
                                Log.d("update",e.message.toString())
                            }

                    }
                }
            }
        }
    }
}

fun updateProfile(
    users: Users,
    firebaseUser: String,
    databaseRef: DatabaseReference,
    activity: MyProfileActivity
) {
    databaseRef.child(firebaseUser).setValue(users).addOnSuccessListener {
        Toast.makeText(activity, "Profile Updated", Toast.LENGTH_LONG).show()
        Log.d("update","Profile Updated Successfully")
    }.addOnFailureListener {
        Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
        Log.d("update", it.message.toString())
    }
}

@Composable
fun UsernameTextFieldPr(
    username: TextFieldState = remember { TextFieldState() }
) {

    OutlinedTextField(
        value = username.text,
        onValueChange = { username.text = it },
        label = { Text(text = "Username") },
        leadingIcon = { Icon(imageVector = Icons.Filled.Person, contentDescription = "Usericon") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
        maxLines = 1
    )
}

@Composable
fun EmailTextFieldPr(
    email: TextFieldState = remember { TextFieldState() }
) {
    OutlinedTextField(
        value = email.text,
        onValueChange = { email.text = it },
        label = { Text(text = "Email") },
        leadingIcon = { Icon(imageVector = Icons.Filled.Email, contentDescription = "Usericon") },
        modifier = Modifier.fillMaxWidth(),

        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
        maxLines = 1
    )
}

@Composable
fun PhoneNumberTextFieldPr(
    phone: TextFieldState = remember { TextFieldState() }
) {
    OutlinedTextField(
        value = phone.text,
        onValueChange = { phone.text= it },
        enabled = false,
        label = { Text(text = "Phone : +91") },
        leadingIcon = { Icon(imageVector = Icons.Filled.Phone, contentDescription = "Usericon") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
        maxLines = 1
    )
}



@Composable
fun ButtonToUpdatePr(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 1.dp)
    ) {
        Text("Update Profile")
    }
}





@Composable
fun ProfileImagePicker(imageUri: ImageUriState = remember { ImageUriState() }) {

    val launcherMultipleImages = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
    ) { uriList: List<Uri> ->
        uriList.let {
            Log.d("uriii",it.toString())
            imageUri.uri = it[0]
        }
    }
    Column(
        Modifier
            .fillMaxWidth()
            .clickable { }, horizontalAlignment = Alignment.CenterHorizontally) {

        IconButton(modifier = Modifier.size(150.dp),onClick = { launcherMultipleImages.launch("image/*")  }) {


            GlideImage( // CoilImage, FrescoImage
                imageModel = imageUri.uri,
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
    }

}







