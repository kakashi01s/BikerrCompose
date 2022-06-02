package com.firefly.bikerr_compose.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.firefly.bikerr_compose.model.Users
import com.firefly.bikerr_compose.screens.login.TextFieldState
import com.pixplicity.easyprefs.library.Prefs

class ViewModelMyProfile : ViewModel(){
    var user : MutableState<Users> = mutableStateOf(Users("","","","",""))



    @Composable
    fun getUserDetails(uid :String)
    {
        val uname  = remember {
            TextFieldState()
        }
        val unumber = remember {
            TextFieldState()
        }
        val uemail = remember {
            TextFieldState()
        }
        val uimg = remember {
            TextFieldState()
        }

                uname.text = Prefs.getString("userName")
                uemail.text = Prefs.getString("userEmail")
                unumber.text = Prefs.getString("userPhone")
                uimg.text = Prefs.getString("userImage")

                user.value = Users(userName = uname.text, userEmail = uemail.text, userId = uid, userImage = uimg.text, userPhone = unumber.text)




    }
}
