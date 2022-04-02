package com.firefly.bikerr_compose.screens.login


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.firefly.bikerr_compose.activities.LoginActivity
import com.firefly.bikerr_compose.viewmodel.ViewModellogin


class TextFieldState(){
    var text: String by mutableStateOf(value= "")
}

@Composable
fun RegisterScreen(
    navHostController: NavHostController,
    loginActivity: LoginActivity,
    viewModel: ViewModellogin
) {
    val focusManager = LocalFocusManager.current
    val email = remember { TextFieldState() }
    val userName = remember { TextFieldState() }
    val phoneNumber = remember { TextFieldState() }
    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(48.dp))
        HeaderText()
        Spacer(modifier = Modifier.height(64.dp))
        UsernameTextField(username = userName,focusManager)
        Spacer(modifier = Modifier.height(4.dp))
        EmailTextField(email = email,focusManager)
        Spacer(modifier = Modifier.height(4.dp))
        PhoneNumberTextField(phone = phoneNumber,focusManager)
        Spacer(modifier = Modifier.height(64.dp))
        ButtonToLogin(onClick = {
            if (email.text.isEmpty() or userName.text.isEmpty() or phoneNumber.text.isEmpty())
            {
                Log.d("login", "Empty Fields")
            }
            else
            {
              viewModel.loginTask(loginActivity,phoneNumber.text.trim(),navHostController,email.text,userName.text)
            }
        })
    }
}


@Composable
private fun HeaderText() {
    Text(
        text = "Welcome,",
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        color = Color.LightGray
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(text = "Sign up to create an account,", fontWeight = FontWeight.Bold, fontSize = 26.sp, color = Color.Red)
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



