package com.firefly.bikerr_compose.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firefly.bikerr_compose.R
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.model.MyRides
import com.firefly.bikerr_compose.screens.login.TextFieldState
import com.google.firebase.firestore.FirebaseFirestore
import com.pixplicity.easyprefs.library.Prefs
import java.sql.Time
import java.time.LocalDateTime
import java.util.*

class PlanRidesActivity : ComponentActivity() {
    val loading = mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        val cid = intent.getStringExtra("cid")
        super.onCreate(savedInstanceState)
        setContent {

            Log.d("rdrd", cid.toString())
            Bikerr_composeTheme {
                // A surface container using the 'background' color from the theme
                Scaffold() {
                    if (!loading.value)
                    {
                        PlanRidesBody(cid.toString())
                    }
                    else
                    {
                        if (loading.value){
                            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                }
            }
        }
    }



    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun PlanRidesBody(cid: String) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val focusManager = LocalFocusManager.current
        val name = remember { TextFieldState() }
        val start = remember { TextFieldState() }
        val end = remember { TextFieldState() }
        val meetupPoint = remember { TextFieldState() }
        val rideDetails = remember { TextFieldState() }
        val rideDate = remember { mutableStateOf("") }


        Column(
            Modifier
                .padding(10.dp)
                .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Card(
                elevation = 5.dp,
                shape = RoundedCornerShape(10.dp),
            ) {
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Card(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(5.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = 5.dp,
                        backgroundColor = Color.LightGray,
                    ) {
                        Icon(imageVector = Icons.Outlined.DirectionsBike, contentDescription = "",Modifier.padding(5.dp))

                    }
                    Text(text = "Lets Plan Your Ride", fontSize = 20.sp)
                }
            }


            Card(
                elevation = 5.dp,
                shape = RoundedCornerShape(10.dp),
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp)) {
                    Row(
                        Modifier
                            .fillMaxSize()
                            .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(5.dp),
                            shape = RoundedCornerShape(10.dp),
                            elevation = 5.dp,
                            backgroundColor = Color.LightGray,
                        ) {
                            Icon(imageVector = Icons.Outlined.Title, contentDescription = "",Modifier.padding(5.dp))

                        }
                        Text(text = "Enter Ride Title")
                    }
                    RideName(focusManager = focusManager, name = name)
                }
            }




            Card(
                elevation = 5.dp,
                shape = RoundedCornerShape(10.dp),
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp)) {
                    Row(
                        Modifier
                            .fillMaxSize()
                            .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(5.dp),
                            shape = RoundedCornerShape(10.dp),
                            elevation = 5.dp,
                            backgroundColor = Color.LightGray,
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CalendarToday,
                                contentDescription = ""
                            )

                        }

                        Text(text = "Ride Date")
                        Spacer(modifier = Modifier.width(10.dp))
                        DatePickerRide(
                            mDate = rideDate,
                            activity = this@PlanRidesActivity
                        )
                    }


                }
            }

            Card(
                elevation = 5.dp,
                shape = RoundedCornerShape(10.dp),
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp)) {
                    Row(
                        Modifier
                            .fillMaxSize()
                            .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(5.dp),
                            shape = RoundedCornerShape(10.dp),
                            elevation = 5.dp,
                            backgroundColor = Color.LightGray,
                        ) {
                            Icon(imageVector = Icons.Outlined.LocationCity, contentDescription = "")

                        }

                        Text(text = "Ride Start/From(Location/City")
                    }

                    RideFrom(focusManager = focusManager, name = start)
                }
            }


            Card(
                elevation = 5.dp,
                shape = RoundedCornerShape(10.dp),
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp)) {
                    Row(
                        Modifier
                            .fillMaxSize()
                            .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(5.dp),
                            shape = RoundedCornerShape(10.dp),
                            elevation = 5.dp,
                            backgroundColor = Color.LightGray,
                        ) {
                            Icon(imageVector = Icons.Outlined.LocationCity, contentDescription = "")

                        }

                        Text(text = "Ride End/To(Location/City)")
                    }

                    RideTo(focusManager = focusManager, name = end)
                }
            }

            Card(
                elevation = 5.dp,
                shape = RoundedCornerShape(10.dp),
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp)) {
                    Row(
                        Modifier
                            .fillMaxSize()
                            .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(5.dp),
                            shape = RoundedCornerShape(10.dp),
                            elevation = 5.dp,
                            backgroundColor = Color.LightGray,
                        ) {
                            Icon(imageVector = Icons.Outlined.MyLocation, contentDescription = "")

                        }
                        Text(text = "MeetUP Point")
                    }
                    RideMeetup(focusManager = focusManager, name = meetupPoint)
                }


            }



            Card(
                elevation = 5.dp,
                shape = RoundedCornerShape(10.dp),
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp)) {
                    Row(
                        Modifier
                            .fillMaxSize()
                            .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(5.dp),
                            shape = RoundedCornerShape(10.dp),
                            elevation = 5.dp,
                            backgroundColor = Color.LightGray,
                        ) {
                            Icon(imageVector = Icons.Outlined.Details, contentDescription = "")

                        }
                        Text(text = "Ride Details")
                    }

                    RideDesc(focusManager = focusManager, name = rideDetails)
                }
            }

            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                elevation = 5.dp,
                shape = RoundedCornerShape(10.dp),
                backgroundColor = Color.DarkGray,
                onClick = {

                }
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Row(Modifier.clickable {
                        loading.value = true
                        if (name.text.isEmpty() && start.text.isEmpty() && end.text.isEmpty() && meetupPoint.text.isEmpty() && rideDate.value.isEmpty() && rideDetails.text.isEmpty()){
                            Toast.makeText(this@PlanRidesActivity,"Please Fill All Details",Toast.LENGTH_LONG).show()
                        }
                        else
                        {

                            val id = LocalDateTime.now().toString()
                            val ride = MyRides( id, listOf("AllMembers :"),rideDetails.text,start.text,end.text,rideDate.value,name.text,meetupPoint.text,Prefs.getString("userName"))
                            db.collection("Rides").document(cid).collection("Plans").document(id).set(ride).addOnSuccessListener {
                                loading.value = false
                                val builder = android.app.AlertDialog.Builder(this@PlanRidesActivity)
                                builder.setTitle("Ride Planned")
                                builder.setIcon(R.drawable.bikerr_logo)
                                builder.setPositiveButton("OK") { dialog, which ->
                                    dialog.dismiss()
                                    this@PlanRidesActivity.finish()
                                }
                                builder.show()
                            }
                        }
                    }) {
                        Text(text = "Create this Plan")
                        Icon(imageVector = Icons.Outlined.NavigateNext, contentDescription = "")

                    }

                }
            }
        }
    }
}




@Composable
private fun RideName(name : TextFieldState = remember { TextFieldState() }, focusManager: FocusManager) {
    OutlinedTextField(
        value = name.text,
        onValueChange = { name.text = it },
        label = { Text(text = "Title of this Ride") },
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
private fun RideFrom(name : TextFieldState = remember { TextFieldState() }, focusManager: FocusManager) {
    OutlinedTextField(
        value = name.text,
        onValueChange = { name.text = it },
        label = { Text(text = "Ride From") },
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
private fun RideTo(name : TextFieldState = remember { TextFieldState() }, focusManager: FocusManager) {
    OutlinedTextField(
        value = name.text,
        onValueChange = { name.text = it },
        label = { Text(text = "Ride To") },
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
private fun RideMeetup(name : TextFieldState = remember { TextFieldState() }, focusManager: FocusManager) {
    OutlinedTextField(
        value = name.text,
        onValueChange = { name.text = it },
        label = { Text(text = "Ride Meetup Point") },
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
private fun RideDesc(name : TextFieldState = remember { TextFieldState() }, focusManager: FocusManager) {
    OutlinedTextField(
        value = name.text,
        onValueChange = { name.text = it },
        label = { Text(text = "Ride Description") },
        modifier = Modifier.fillMaxWidth(),
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Down
            )
        }),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
        maxLines = 5
    )
}

@Composable
fun DatePickerRide(mDate: MutableState<String>, activity : PlanRidesActivity) {

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
            mDate.value = "$mDayOfMonth-${mMonth + 1}-$mYear"
        }, mYear, mMonth, mDay
    )

    Row(
        Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(color = Color.LightGray)
            .clickable {
                mDatePickerDialog.show()
            }, verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "", modifier = Modifier
            .padding(5.dp)
            .size(20.dp))
        Text(text = mDate.value.ifEmpty { "Pick a Date" }, modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 2.dp), fontSize = 15.sp)
        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "")
    }
}