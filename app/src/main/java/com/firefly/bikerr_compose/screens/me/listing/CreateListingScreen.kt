package com.firefly.bikerr_compose.screens.me.listing

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.net.toUri
import coil.compose.rememberImagePainter
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.firefly.bikerr_compose.R
import com.firefly.bikerr_compose.activities.CreateListingActivity
import com.firefly.bikerr_compose.screens.login.TextFieldState
import com.firefly.bikerr_compose.viewmodel.ViewModelCreateListing
import com.skydoves.landscapist.glide.GlideImage
import androidx.compose.ui.unit.dp as dp


class ImageUriState: AppCompatActivity(){

    var uri by mutableStateOf<Uri?>("https://pngimage.net/wp-content/uploads/2018/05/add-logo-png-5.png".toUri())

}
@SuppressLint("UnrememberedMutableState")
@Composable
fun CreateListingScreen(activity: CreateListingActivity, viewModel: ViewModelCreateListing) {

    val focusManager = LocalFocusManager.current
    val vehicleImageUri1 = remember { ImageUriState() }
    val vehicleImageUri2 = remember { ImageUriState() }
    val vehicleImageUri3 = remember { ImageUriState() }
    val vehicleRCFrontUri = remember { ImageUriState() }
    val vehicleRCBackUri = remember { ImageUriState() }
    val vehicleInsuranceUri = remember { ImageUriState() }
    val vehicleRegistrationNumberFront = remember { TextFieldState() }
    val vehicleRegistrationNumberEnd = remember { TextFieldState() }
    val vehicleName = remember { TextFieldState() }
    val vehicleDescription = remember { TextFieldState() }
    val vehicleCity = remember { TextFieldState() }
    val vehiclePrice = remember { TextFieldState() }
    val vehiclePickupAddress = remember { TextFieldState() }
    val selectedBrand = remember { TextFieldState() }


    val showProgress = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),

                ) {
                Text(text = "Create Listing", fontWeight = FontWeight.Bold, fontSize = 30.sp)
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = {
                        //send data to firebase
                        if (
                            vehicleImageUri1.uri == "https://pngimage.net/wp-content/uploads/2018/05/add-logo-png-5.png".toUri() &&
                            vehicleImageUri2.uri == "https://pngimage.net/wp-content/uploads/2018/05/add-logo-png-5.png".toUri() &&
                            vehicleImageUri3.uri == "https://pngimage.net/wp-content/uploads/2018/05/add-logo-png-5.png".toUri() &&
                            vehicleRCFrontUri.uri == "https://pngimage.net/wp-content/uploads/2018/05/add-logo-png-5.png".toUri() &&
                            vehicleRCBackUri.uri == "https://pngimage.net/wp-content/uploads/2018/05/add-logo-png-5.png".toUri() &&
                            vehicleInsuranceUri.uri == "https://pngimage.net/wp-content/uploads/2018/05/add-logo-png-5.png".toUri() &&
                            vehicleRegistrationNumberFront.text.isEmpty() &&
                            vehicleRegistrationNumberEnd.text.isEmpty() &&
                            vehicleName.text.isEmpty() &&
                            vehicleDescription.text.isEmpty() &&
                            vehiclePrice.text.isEmpty() &&
                            vehicleCity.text.isEmpty() &&
                            vehiclePickupAddress.text.isEmpty()
                        ) {
                            Toast.makeText(
                                activity,
                                "Please Provide All Details",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            showProgress.value = true
                            viewModel.vehicleFullNumber.text =
                                vehicleRegistrationNumberFront.text + "TA" + vehicleRegistrationNumberEnd.text
                            viewModel.upload1ImageToFirebase(
                                vehicleImageUri1.uri!!,
                                "Image1"
                            )
                            viewModel.upload2ImageToFirebase(
                                vehicleImageUri2.uri!!,
                                "Image2",
                            )
                            viewModel.upload3ImageToFirebase(
                                vehicleImageUri3.uri!!,
                                "Image3",
                            )
                            viewModel.uploadRcFrontImageToFirebase(
                                vehicleRCFrontUri.uri!!,
                                "RCFront",
                            )
                            viewModel.uploadRcBackImageToFirebase(
                                vehicleRCBackUri.uri!!,
                                "RCBack",
                            )
                            viewModel.uploadInsuranceImageToFirebase(
                                vehicleInsuranceUri.uri!!,
                                "Insurance",
                                vehicleCity,
                                vehicleDescription,
                                vehicleName,
                                vehiclePickupAddress,
                                vehiclePrice,
                                selectedBrand,
                                activity
                            )

                        }
                    }
                    ) {
                        Text(text = "Add")
                    }
                }
            }


        }) {
        if (showProgress.value){
            LinearProgressIndicator(color = Color.Black, backgroundColor = Color.LightGray, modifier = Modifier.fillMaxWidth())
        }

        Column(
            Modifier
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            //vehicle Images
            Text(text = "Vehicle Images", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Card(border = BorderStroke(2.dp, color = Color.LightGray)) {
                Row(
                    Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image1Picker(vehicleImageUri1)
                    Spacer(modifier = Modifier.width(10.dp))
                    Image2Picker(vehicleImageUri2)
                    Spacer(modifier = Modifier.width(10.dp))
                    Image3Picker(vehicleImageUri3)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))


            Text(text = "RC", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(5.dp)
                    )
            ) {

                Row(
                    Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ImageRCPicker(imageUri = vehicleRCFrontUri)
                    Spacer(modifier = Modifier.width(10.dp))
                    ImageRCBackPicker(vehicleRCBackUri)

                }

            }
            Spacer(modifier = Modifier.height(20.dp))


            Text(text = "Insurance Papers", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(5.dp)
                    )
            ) {

                ImageInsurancePicker(imageUri = vehicleInsuranceUri)
            }

            Spacer(modifier = Modifier.height(20.dp))


            //vehicle Data Form
            Text(
                text = "Vehicle Registration Number",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Card(
                border = BorderStroke(2.dp, color = Color.LightGray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    vehicleRegistrationStartTextField(
                        focusManager = focusManager,
                        text = vehicleRegistrationNumberFront
                    )
                    Text(
                        text = "TA",
                        modifier = Modifier,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    vehicleRegistrationEndTextField(
                        focusManager = focusManager,
                        text = vehicleRegistrationNumberEnd
                    )
                }
            }
            Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.SpaceEvenly) {
                vehicleNameTextField(focusManager = focusManager, text = vehicleName)
                vehicleCityTextField(focusManager = focusManager, text = vehicleCity)
                vehiclePriceTextField(focusManager = focusManager, text = vehiclePrice)
                vehicleBrand(
                    selectedBrand,
                    activity
                )
                vehiclePickUpTextField(focusManager = focusManager, text = vehiclePickupAddress)
                vehicleDescriptionTextField(focusManager = focusManager, text = vehicleDescription)
                Spacer(modifier = Modifier.height(20.dp))
            }


        }
    }
}


@Composable
fun vehicleBrand(
    selectedBrand: TextFieldState = remember { TextFieldState() },
    activity: CreateListingActivity
) {
    var textfieldBrand by remember { mutableStateOf(Size.Zero) }

    var expandedBrand by remember { mutableStateOf(false) }

    val iconquantity = if (expandedBrand)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val sizeList = listOf("Hero","Royal Enfield","Bajaj", "Ktm","Honda","TVS")

    val maxChar = 1
    Column(Modifier.padding(20.dp)) {
        OutlinedTextField(
            value = selectedBrand.text,
            onValueChange = {  selectedBrand.text = it.take(maxChar)
                if (it.length == maxChar){
                    Toast.makeText(activity,"Please Select From the DropDown Menu.", Toast.LENGTH_SHORT).show()
                }},
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldBrand = coordinates.size.toSize()
                }.clickable { expandedBrand = ! expandedBrand },
            label = {Text("Brand Name")},
            trailingIcon = {
                Icon(iconquantity,"contentDescription")
            },
            enabled = false
        )
        DropdownMenu(
            expanded = expandedBrand,
            onDismissRequest = { expandedBrand = false },
            modifier = Modifier
                .width(with(LocalDensity.current){textfieldBrand.width.toDp()})
        ) {
            sizeList.forEach { label ->
                DropdownMenuItem(onClick = {
                    selectedBrand.text= label
                    expandedBrand = false
                }) {
                    Text(text = label)

                }
            }
        }
    }
}
@Composable
fun ImageInsurancePicker(imageUri: ImageUriState = remember { ImageUriState() }) {

    val launcherMultipleImages = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
    ) { uriList: List<Uri> ->
        uriList.let {
            Log.d("uriii",it.toString())
            imageUri.uri = it[0]
        }
    }
    IconButton(modifier = Modifier.size(150.dp),onClick = { launcherMultipleImages.launch("image/*")  }) {

        GlideImage(imageModel = imageUri.uri, placeHolder = Icons.Default.Add)

    }
}


@Composable
fun ImageRCPicker(imageUri: ImageUriState = remember { ImageUriState() }) {

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
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = "RC Front", textAlign = TextAlign.Center)
        }
        IconButton(modifier = Modifier.size(150.dp),onClick = { launcherMultipleImages.launch("image/*")  }) {

            Image(painter = rememberImagePainter(data = imageUri.uri), contentDescription ="")

        }
    }
}


@Composable
fun ImageRCBackPicker(imageUri: ImageUriState = remember { ImageUriState() }) {

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
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = "Rc Back", textAlign = TextAlign.Center)
        }
        IconButton(modifier = Modifier.size(150.dp),onClick = { launcherMultipleImages.launch("image/*")  }) {

            Image(painter = rememberImagePainter(data = imageUri.uri, builder = {
                placeholder(R.drawable.ic_baseline_add_24)
            }), contentDescription ="")

        }
    }
}

@Composable
private fun vehicleDescriptionTextField(text : TextFieldState = remember { TextFieldState() },focusManager: FocusManager) {
    OutlinedTextField(
        value = text.text,
        onValueChange = {  text.text = it  },
        label = { Text(text = "Description") },
        modifier = Modifier
            .fillMaxWidth(),
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
private fun vehiclePickUpTextField(text : TextFieldState = remember { TextFieldState() },focusManager: FocusManager) {
    OutlinedTextField(
        value = text.text,
        onValueChange = {  text.text = it  },
        label = { Text(text = "Pick up Address") },
        modifier = Modifier
            .fillMaxWidth()
        ,
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
private fun vehiclePriceTextField(text : TextFieldState = remember { TextFieldState() },focusManager: FocusManager) {
    OutlinedTextField(
        value = text.text,
        onValueChange = { text.text = it  },
        label = { Text(text = "Price/Day") },
        modifier = Modifier
            .fillMaxWidth()
            ,
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Down
            )
        }),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
        maxLines = 1
    )
}
@Composable
private fun vehicleCityTextField(text : TextFieldState = remember { TextFieldState() },focusManager: FocusManager) {
    OutlinedTextField(
        value = text.text,
        onValueChange = {  text.text = it  },
        label = { Text(text = "City") },
        modifier = Modifier
            .fillMaxWidth()
            ,
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
private fun vehicleNameTextField(text : TextFieldState = remember { TextFieldState() },focusManager: FocusManager) {
    OutlinedTextField(
        value = text.text,
        onValueChange = {  text.text = it  },
        label = { Text(text = "Name") },
        modifier = Modifier
            .fillMaxWidth()
        ,
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
private fun vehicleRegistrationStartTextField(text : TextFieldState = remember { TextFieldState() },focusManager: FocusManager) {
    OutlinedTextField(
        value = text.text,
        onValueChange = { if (it.length <= 4) text.text = it  },
        label = { Text(text = "eg : DL01") },
        modifier = Modifier
            .width(120.dp)
            ,
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
private fun vehicleRegistrationEndTextField(text : TextFieldState = remember { TextFieldState() },focusManager: FocusManager) {
    OutlinedTextField(
        value = text.text,
        onValueChange = { if (it.length <= 4) text.text = it  },
        label = { Text(text = "eg : XXXX") },
        modifier = Modifier.width(120.dp),
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Down
            )
        }),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
        maxLines = 1
    )
}

@Composable
fun Image1Picker(imageUri: ImageUriState = remember { ImageUriState() }) {

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the cropped image
            imageUri.uri = result.uriContent
        } else {
            // an error occurred cropping
            val exception = result.error
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        val cropOptions = CropImageContractOptions(uri, CropImageOptions()).setFixAspectRatio(true).setAspectRatio(2, 2).setAutoZoomEnabled(true)
        imageCropLauncher.launch(cropOptions)
    }


    Column(
        Modifier
            .fillMaxWidth()
            .clickable { }, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = "Image 1", textAlign = TextAlign.Center)
        }
        IconButton(modifier = Modifier.size(150.dp),onClick = { imagePickerLauncher.launch("image/*")  }) {

            Image(painter = rememberImagePainter(data = imageUri.uri), contentDescription ="")

        }
    }


}




@Composable
fun Image2Picker(imageUri: ImageUriState = remember { ImageUriState() }) {


    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the cropped image
            imageUri.uri = result.uriContent
        } else {
            // an error occurred cropping
            val exception = result.error
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        val cropOptions = CropImageContractOptions(uri, CropImageOptions()).setFixAspectRatio(true).setAspectRatio(2, 2).setAutoZoomEnabled(true)
        imageCropLauncher.launch(cropOptions)
    }


    Column(
        Modifier
            .fillMaxWidth()
            .clickable { }, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = "Image 2", textAlign = TextAlign.Center)
        }
        IconButton(modifier = Modifier.size(150.dp),onClick = { imagePickerLauncher.launch("image/*")  }) {

            Image(painter = rememberImagePainter(data = imageUri.uri), contentDescription ="")

        }
    }

}


@Composable
fun Image3Picker(imageUri: ImageUriState = remember { ImageUriState() }) {

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the cropped image
            imageUri.uri = result.uriContent
        } else {
            // an error occurred cropping
            val exception = result.error
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        val cropOptions = CropImageContractOptions(uri, CropImageOptions()).setFixAspectRatio(true).setAspectRatio(2, 2).setAutoZoomEnabled(true)
        imageCropLauncher.launch(cropOptions)
    }


    Column(
        Modifier
            .fillMaxWidth()
            .clickable { }, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = "Image 3", textAlign = TextAlign.Center)
        }
        IconButton(modifier = Modifier.size(150.dp),onClick = { imagePickerLauncher.launch("image/*")  }) {

            Image(painter = rememberImagePainter(data = imageUri.uri), contentDescription ="")

        }
    }


}

