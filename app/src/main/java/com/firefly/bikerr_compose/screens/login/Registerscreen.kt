package com.firefly.bikerr_compose.screens.login


import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.firefly.bikerr_compose.R
import com.firefly.bikerr_compose.activities.HeaderText
import com.firefly.bikerr_compose.activities.LoginActivity
import com.firefly.bikerr_compose.activities.WebActivity
import com.firefly.bikerr_compose.viewmodel.ViewModellogin
import com.skydoves.landscapist.glide.GlideImage


class TextFieldState {
    var text: String by mutableStateOf(value= "")
}

@Composable
fun RegisterScreen(
    navHostController: NavHostController,
    loginActivity: LoginActivity,
    viewModel: ViewModellogin
) {
    val showProgress = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val email = remember { TextFieldState() }
    val userName = remember { TextFieldState() }
    val phoneNumber = remember { TextFieldState() }
    if (showProgress.value){
        LinearProgressIndicator(color = Color.Black, backgroundColor = Color.LightGray, modifier = Modifier.fillMaxWidth())
    }


            Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(100.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    GlideImage(imageModel = R.drawable.bikerr_logo, modifier = Modifier.size(100.dp))
                    HeaderText()
                }
                Spacer(modifier = Modifier.height(0.dp))
                Card( modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        UsernameTextField(username = userName,focusManager)
                        Spacer(modifier = Modifier.height(4.dp))
                        EmailTextField(email = email,focusManager)
                        Spacer(modifier = Modifier.height(4.dp))
                        PhoneNumberTextField(phone = phoneNumber,focusManager)
                        Spacer(modifier = Modifier.height(10.dp))
                        Spacer(modifier = Modifier.height(2.dp))
                        ButtonToLogin(onClick = {

                            if (email.text.isEmpty() && userName.text.isEmpty() && phoneNumber.text.isEmpty())
                            {
                                Log.d("login", "Empty Fields")
                                android.widget.Toast.makeText(loginActivity,"Please Fill All Fields", android.widget.Toast.LENGTH_LONG).show()
                            }
                            else
                            {
                                viewModel.loginTask(loginActivity,phoneNumber.text.trim(),navHostController,email.text,userName.text)
                                showProgress.value = true
                            }
                        })
                    }
                }
            }

}




@Composable
private fun UsernameTextField(username : TextFieldState = remember { TextFieldState() },focusManager: FocusManager) {

    OutlinedTextField(
        value = username.text,
        onValueChange = { username.text = it },
        label = { Text(text = "Username") },
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
private fun EmailTextField(email : TextFieldState = remember { TextFieldState() },focusManager: FocusManager) {
    OutlinedTextField(
        value = email.text,
        onValueChange = { email.text = it },
        label = { Text(text = "Email") },
        leadingIcon = { Icon(imageVector = Icons.Filled.Email, contentDescription = "Usericon") },
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
private fun PhoneNumberTextField(phone : TextFieldState = remember { TextFieldState() },focusManager: FocusManager) {
    OutlinedTextField(
        value = phone.text,
        onValueChange = { phone.text= it },
        label = { Text(text = "Phone : +91") },
        leadingIcon = { Icon(imageVector = Icons.Filled.Phone, contentDescription = "Usericon") },
        modifier = Modifier.fillMaxWidth(),
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Down
            )
        }),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
        maxLines = 1
    )
}



@Composable
private fun ButtonToLogin(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 1.dp)
    ) {
        Text("GET OTP")
    }
}



