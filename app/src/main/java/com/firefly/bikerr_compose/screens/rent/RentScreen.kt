package com.firefly.bikerr_compose.screens.rent

import android.app.DatePickerDialog
import android.content.Intent
import android.util.Log
import android.widget.DatePicker
import androidx.compose.animation.VectorConverter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.firefly.bikerr_compose.activities.MainActivityCompose
import com.firefly.bikerr_compose.activities.RentalItemActivity
import com.firefly.bikerr_compose.model.rental.Vehicle
import com.firefly.bikerr_compose.screens.login.TextFieldState
import com.firefly.bikerr_compose.viewmodel.ViewModelmain
import com.skydoves.landscapist.glide.GlideImage
import java.util.*


@Composable
fun RentScreen(
    mainActivityCompose: MainActivityCompose,
    mainViewModel: ViewModelmain,
)
{

    var list: List<Vehicle>
    val selectedCity = remember { TextFieldState() }
    val mDatefrom = remember { mutableStateOf("") }
    val mDateto= remember { mutableStateOf("") }

    Scaffold(topBar = {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(5.dp)){
            Text(text = "Rent a Motorcycle", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)

            Spacer(modifier = Modifier.size(5.dp))

            SelectedCityDropDown(mainActivityCompose = mainActivityCompose,selectedCity)

            Spacer(modifier = Modifier.size(5.dp))

            Text(text = "Select Date", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Spacer(modifier = Modifier.size(5.dp))
            Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {

                Column {
                    Text(text = "START DATE", color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    DatePicker(mDate = mDateto, activity = mainActivityCompose)
                }
                Column() {
                    Text(text = "END DATE", color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    DatePicker(mDate = mDatefrom, activity = mainActivityCompose)
                }
            }
        }

    }) {
        Divider(color = Color.LightGray, thickness = 5.dp)

        list = mainViewModel.vehicleList.value

            list.let {
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 50.dp), verticalArrangement = Arrangement.spacedBy(8.dp)){
                    items(it){
                        if (it.location == selectedCity.text.uppercase() && it.verified)
                        {
                            Log.d("lid",it.toString())
                            RentalListItem(vehicle = it) {
                                val intent = Intent(mainActivityCompose, RentalItemActivity::class.java)
                                intent.putExtra("header",it.name)
                                intent.putExtra("reg",it.regNumber)
                                mainActivityCompose.startActivity(intent)
                            }
                        }



                    }
                }
            }

        }
}






@Composable
fun SelectedCityDropDown(
    mainActivityCompose: MainActivityCompose,
    selectedCity: TextFieldState = remember { TextFieldState() }
) {


    val cityList = listOf("Delhi","Udaipur","Rishikesh","Manali")

    //city dropdown
    var expandedquantity by remember { mutableStateOf(false) }


    var textfieldquantity by remember { mutableStateOf(Size.Zero) }


    val arrowDropdown = if (expandedquantity)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(
        Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(Color.Black)
            .onGloballyPositioned { coordinates ->
                //This value is used to assign to the DropDown the same width
                textfieldquantity = coordinates.size.toSize()
            }
            .clickable { expandedquantity = !expandedquantity }) {

            Row(Modifier.padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 2.dp)) {
                Text(
                    text =
                    selectedCity.text.ifEmpty {
                        "Select City"
                    }, color = Color.White, fontSize = 15.sp)
                Icon(imageVector = arrowDropdown, contentDescription = "", tint = Color.White)
            }


        DropdownMenu(
            expanded = expandedquantity,
            onDismissRequest = { expandedquantity = false },
            modifier = Modifier
                .width(with(LocalDensity.current){textfieldquantity.width.toDp()})
        ) {
            cityList.forEach { label ->
                DropdownMenuItem(onClick = {
                    selectedCity.text = label
                    expandedquantity = false
                }) {
                    Text(text = label)

                }
            }
        }

    }
}




@Composable
fun DatePicker(mDate: MutableState<String>,activity : MainActivityCompose) {
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
            mDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
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
        Text(text = mDate.value.ifEmpty {
            "Pick a Date"
        }, modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 2.dp), fontSize = 15.sp)
        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "")
    }
}





@Composable
fun RentalListItem(vehicle: Vehicle,onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
        backgroundColor = Color.White,
        elevation = 10.dp
    ) {
        Column(modifier = Modifier
            .clickable {
                onClick.invoke()
            }
            .fillMaxWidth()
            .padding(10.dp)) {
            Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
                Row() {
                    Column(horizontalAlignment = Alignment.Start) {
                        ImageSlider(vehicle)
                    }

                    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(5.dp)) {
                        Text(text = vehicle.Brand.uppercase(), fontSize = 10.sp, color = Color.DarkGray)
                        Text(text = vehicle.name, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                    }
                }
                Row() {
                    Column(horizontalAlignment = Alignment.End) {
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(40.dp)
                                .padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 5.dp)
                                .clip(
                                    RoundedCornerShape(5.dp)
                                )
                                .background(color = Color(0xFF1cca30)), contentAlignment = Alignment.Center) {
                            Text(text = "₹"+vehicle.price, color = Color.White)
                        }
                    }


                }


            }
        }
    }
}



@Composable
fun ImageSlider(vehicle: Vehicle) {
    val images = listOf(
        vehicle.image1,
        vehicle.image2,
        vehicle.image3
    )

    com.firefly.bikerr_compose.common.Pager(
        items = images,
        modifier = Modifier
            .width(100.dp)
            .height(100.dp),
        itemFraction = .75f,
        overshootFraction = .75f,
        initialIndex = 0,
        itemSpacing = 16.dp,
        contentFactory = { item ->
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                GlideImage(imageModel = item, modifier = Modifier.fillMaxSize())

            }
        }
    )
}





