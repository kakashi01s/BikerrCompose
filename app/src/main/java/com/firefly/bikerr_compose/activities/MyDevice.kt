package com.firefly.bikerr_compose.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.screens.login.*
import com.firefly.bikerr_compose.viewmodel.AddDeviceViewModel

class MyDevice : ComponentActivity() {
    private val addDeviceViewModel by viewModels<AddDeviceViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bikerr_composeTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(topBar = {
                    Text(text = "My Device")
                }) {
                    RegisterDevice(this,addDeviceViewModel)
                }
            }
        }
    }
}


@Composable
fun RegisterDevice(myDevice: MyDevice, addDeviceViewModel: AddDeviceViewModel) {
    val focusManager = LocalFocusManager.current
    val deviceRegistration = remember { TextFieldState() }
    val deviceName = remember { TextFieldState() }
    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(48.dp))
        DeviceHeaderText()
        Spacer(modifier = Modifier.height(64.dp))
        DeviceNameTextField(username = deviceName,focusManager)
        Spacer(modifier = Modifier.height(4.dp))
        DeviceRegistrationTextField(email = deviceRegistration,focusManager)
        Spacer(modifier = Modifier.height(4.dp))
        ButtonToAdd(onClick = {
            if (deviceRegistration.text.isEmpty() or deviceName.text.isEmpty() )
            {
                Log.d("adddd", "Empty Fields")
            }
            else
            {
                addDeviceViewModel.addDevices(myDevice, id = deviceRegistration.text.trim(), deviceName = deviceName.text.trim())
            }
        })
    }
}

@Composable
private fun DeviceHeaderText() {
    Text(
        text = "Welcome,",
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        color = Color.LightGray
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(text = "Lets Add Your GPS Tracker", fontWeight = FontWeight.Bold, fontSize = 26.sp, color = Color.Red)
}

@Composable
private fun DeviceNameTextField(username : TextFieldState = remember { TextFieldState() },focusManager: FocusManager) {

    OutlinedTextField(
        value = username.text,
        onValueChange = { username.text = it },
        label = { Text(text = "Vehicle Registration Number") },
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
private fun DeviceRegistrationTextField(email : TextFieldState = remember { TextFieldState() },focusManager: FocusManager) {
    OutlinedTextField(
        value = email.text,
        onValueChange = { email.text = it },
        label = { Text(text = "IMEI") },
        leadingIcon = { Icon(imageVector = Icons.Filled.GpsFixed, contentDescription = "Usericon") },
        modifier = Modifier.fillMaxWidth(),
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Down
            )
        }),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
        maxLines = 1,
        placeholder = {
            Text(text = "xxxxxxxxxxxxxxxxxxx")
        }
    )
}



@Composable
private fun ButtonToAdd(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 1.dp)
    ) {
        Text("Add Device")
    }
}

