package com.firefly.bikerr_compose.screens.rent.rentalActivity

import android.app.DatePickerDialog
import android.content.Intent
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firefly.bikerr_compose.activities.CheckoutBookingActivity
import com.firefly.bikerr_compose.activities.RentalBookingActivity
import com.firefly.bikerr_compose.model.Booking
import com.firefly.bikerr_compose.model.Users
import com.firefly.bikerr_compose.model.rental.Vehicle
import com.firefly.bikerr_compose.screens.login.TextFieldState
import com.firefly.bikerr_compose.viewmodel.ViewModelRentalBooking
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import java.util.*

@Composable
fun RentalBookingScreen(
    viewModel: ViewModelRentalBooking,
    rentalBookingActivity: RentalBookingActivity,
    value: Vehicle?
) {
    // Declaring a string value to
    // store date in string format
    val mDatefrom = remember { mutableStateOf("") }
    val mDateto= remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val email = remember { TextFieldState() }
    val userName = remember { TextFieldState() }
    val phoneNumber = remember { TextFieldState() }
    Scaffold(topBar = {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
        ) {
            Text( text = "Book Your Bike", fontWeight = FontWeight.Bold, fontSize = 30.sp)
        }

    }, bottomBar = {
        Button(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),onClick = {
            // Booking Button
            if (value != null) {
                val vehicle = Vehicle(value.name, value.Brand,value.regNumber,value.location,value.price,value.description,value.image1,value.image2,value.image3,value.pickupAddress, owner = Users(value.owner.userName,value.owner.userEmail,value.owner.userPhone,value.owner.userImage,value.owner.userId),value.verified,value.insurancePapers,value.RcFrontImage,value.RcBackImage)
                val gson = Gson()
                val intent = Intent(rentalBookingActivity, CheckoutBookingActivity::class.java)
                intent.putExtra("vehicle", gson.toJson(vehicle))
                intent.putExtra("startDate",mDatefrom.value)
                intent.putExtra("endDate",mDateto.value)
                rentalBookingActivity.startActivity(intent)
            }
        }) {
            Text(text = "Continue To Booking")
        }
    })
    {

        Column {
            BookingNameTextField(name = userName, focusManager = focusManager)
            BookingPhoneNumberTextField(phoneNumber,focusManager = focusManager)
            BookingEmailTextField(focusManager = focusManager, email = email)
            DatePicker(mDatefrom,rentalBookingActivity)
            DatePicker(mDateto,rentalBookingActivity)
        }

    }
}


@Composable
fun DatePicker(mDate: MutableState<String>,activity : RentalBookingActivity) {
    // Fetching the Local Context
    val mContext = LocalContext.current

    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
        }, mYear, mMonth, mDay
    )

    Row(Modifier.clickable {
        mDatePickerDialog.show()
    }) {
        Text(text = mDate.value.ifEmpty {
            "START DATE"
        })
        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "")
    }

//    OutlinedTextField(
//        value = mDate.value,
//        onValueChange = {  mDate.value = it.take(maxChar)
//            if (it.length == maxChar){
//                Toast.makeText(activity,"Please Select From the Date Picker.", Toast.LENGTH_SHORT).show()
//            }},
//        label = { Text(text = "Date") },
//        leadingIcon = { Icon(imageVector = Icons.Filled.CalendarToday, contentDescription = "Usericon") },
//        trailingIcon = {  IconButton(modifier = Modifier
//            .width(50.dp)
//            .height(50.dp), onClick = { mDatePickerDialog.show() }) {
//            Icon(imageVector = Icons.Default.DateRange, contentDescription ="" )
//        }
//        },
//        modifier = Modifier.fillMaxWidth(),
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
//        maxLines = 1
//    )



}

@Composable
private fun BookingNameTextField(name : TextFieldState = remember { TextFieldState() }, focusManager: FocusManager) {

    OutlinedTextField(
        value = name.text,
        onValueChange = { name.text = it },
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
private fun BookingEmailTextField(email : TextFieldState = remember { TextFieldState() },focusManager: FocusManager) {
    OutlinedTextField(
        value = email.text,
        onValueChange = { email.text = it },
        label = { Text(text = "Email") },
        leadingIcon = { Icon(imageVector = Icons.Filled.Email, contentDescription = "") },
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
private fun BookingPhoneNumberTextField(phone : TextFieldState = remember { TextFieldState() },focusManager: FocusManager) {
    OutlinedTextField(
        value = phone.text,
        onValueChange = { phone.text= it },
        label = { Text(text = "Phone : +91") },
        leadingIcon = { Icon(imageVector = Icons.Filled.Phone, contentDescription = "") },
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

