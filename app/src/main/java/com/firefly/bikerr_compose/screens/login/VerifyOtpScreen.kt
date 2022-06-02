package com.firefly.bikerr_compose.screens.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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
import com.firefly.bikerr_compose.R
import com.firefly.bikerr_compose.activities.LoginActivity
import com.firefly.bikerr_compose.viewmodel.ViewModellogin
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun VerifyOtpScreen(
    loginActivity: LoginActivity,
    viewmodel: ViewModellogin,
    username: String,
    email: String,
    phone: String
)
{
    val showProgress = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val otp1 = remember { TextFieldState() }
    val otp2 = remember { TextFieldState() }
    val otp3 = remember { TextFieldState() }
    val otp4 = remember { TextFieldState() }
    val otp5 = remember { TextFieldState() }
    val otp6 = remember { TextFieldState() }
    if (showProgress.value){
        LinearProgressIndicator(color = Color.Black, backgroundColor = Color.LightGray, modifier = Modifier.fillMaxWidth())
    }
    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(48.dp))
        Row() {
            com.skydoves.landscapist.glide.GlideImage(imageModel = R.drawable.bikerr_logo, modifier = Modifier.size(100.dp))
            HeaderTextOTP()
        }
        Spacer(modifier = Modifier.height(48.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceEvenly)
        {
            OTPTextField1(otp1,focusManager)
            OTPTextField1(otp2,focusManager)
            OTPTextField1(otp3,focusManager)
            OTPTextField1(otp4,focusManager)
            OTPTextField1(otp5,focusManager)
            OTPTextField1(otp6,focusManager)
        }
        Spacer(modifier = Modifier.height(48.dp))
        Text(text = "Click here after You've Entered your OTP.", fontSize = 15.sp)
        ButtonVerify_OTP(
            onClick = {
                if(otp1.text.isEmpty() && otp2.text.isEmpty()  && otp3.text.isEmpty() &&otp4.text.isEmpty() &&otp5.text.isEmpty() &&otp6.text.isEmpty()  )
                {

                     Toast.makeText(loginActivity,"Please Enter Your OTP",Toast.LENGTH_LONG).show()
                }
                else
                {
                    showProgress.value = true
                    // initService(loginActivity,email,phone)
                    val otp = otp1.text + otp2.text + otp3.text + otp4.text + otp5.text + otp6.text
                    viewmodel.verifyAuthentication(otpText = otp, loginActivity, username, email, phone)

                }


            })
        }
    }




@Composable
private fun HeaderTextOTP() {
    Column() {
        Text(
            text = "Lets Verify",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = "Your OTP,", fontWeight = FontWeight.Bold, fontSize = 26.sp, color = Color.Red)
    }

}


@Composable
private fun OTPTextField1(
    otp: TextFieldState = remember { TextFieldState() },
    focusManager: FocusManager
) {
    val maxChar = 1
    OutlinedTextField(
        value = otp.text,
        onValueChange = {
            otp.text = it.take(maxChar)
            if (it.length == maxChar){
                focusManager.moveFocus(FocusDirection.Right) // Or receive a lambda function
            }
                        },
        label = { Text(text = "") },
        modifier = Modifier.width(40.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onAny = {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Right
            )
        }),
        maxLines = 1,
    )
}


@Composable
private fun ButtonVerify_OTP(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 1.dp)
    ) {
        Text("VERIFY OTP")
    }
}

