package com.firefly.bikerr_compose.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.MutableLiveData
import coil.compose.rememberImagePainter
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.model.CartItem
import com.firefly.bikerr_compose.screens.login.TextFieldState
import com.google.android.libraries.places.internal.it
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ShopItemActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bikerr_composeTheme {
                // A surface container using the 'background' color from the theme
                val itemName = remember { TextFieldState() }
                val itemPrice = remember { TextFieldState() }
                val itemImage = remember { TextFieldState() }
                itemName.text = intent.getStringExtra("itemName").toString()
                itemPrice.text = intent.getStringExtra("itemPrice").toString()
                itemImage.text = intent.getStringExtra("itemImage").toString()
                // A surface container using the 'background' color from the theme
                Scaffold(topBar = {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),

                        ) {

                        Text(text = itemName.text, fontWeight = FontWeight.Bold, fontSize = 30.sp)
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = {
                                val intent = Intent(this@ShopItemActivity,CartActivity::class.java)
                                startActivity(intent)
                            }) {
                                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "cart")
                            }

                        }
                    }
                }) {
                    Column {
                            items(
                                itemImage,
                                itemName,
                                itemPrice,
                                this@ShopItemActivity
                            )
                    }

                }
            }
        }
    }
}

@Composable
fun items(
    itemImage: TextFieldState,
    itemName: TextFieldState,
    itemPrice: TextFieldState,
    shopItemActivity: ShopItemActivity,
) {
    var selectedTextquantity by remember { mutableStateOf("") }
    var selectedTextsize by remember { mutableStateOf("") }
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = rememberImagePainter(itemImage.text), contentDescription = "",modifier = Modifier.size(400.dp))
                }

                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text = itemName.text, fontWeight = FontWeight.Bold)
                        Row() {
                            Text(modifier = Modifier.padding(start = 5.dp), text = "Price :", fontWeight = FontWeight.Bold)
                            Text(modifier = Modifier.padding(start = 5.dp),text = "₹ ${itemPrice.text}")
                        }
                        Row(horizontalArrangement = Arrangement.Center) {
                            val sizeList = listOf("S","M","L", "XL")
                            val quantityList = listOf("1","2","3", "4")

                            //size dropdown
                            var expandedquantity by remember { mutableStateOf(false) }


                            var textfieldquantity by remember { mutableStateOf(Size.Zero) }


                            val iconquantity = if (expandedquantity)
                                Icons.Filled.KeyboardArrowUp
                            else
                                Icons.Filled.KeyboardArrowDown



                            val maxChar = 2
                            Column(Modifier.padding(20.dp)) {
                                OutlinedTextField(
                                    value = selectedTextquantity,
                                    onValueChange = {  selectedTextquantity = it.take(maxChar)
                                                    if (it.length == maxChar){
                                                        Toast.makeText(shopItemActivity,"Please Enter S, M , L or XL",Toast.LENGTH_SHORT).show()
                                                    }},
                                    modifier = Modifier
                                        .width(110.dp)
                                        .onGloballyPositioned { coordinates ->
                                            //This value is used to assign to the DropDown the same width
                                            textfieldquantity = coordinates.size.toSize()
                                        },
                                    label = {Text("Size")},
                                    trailingIcon = {
                                        Icon(iconquantity,"contentDescription",
                                            Modifier.clickable { expandedquantity = ! expandedquantity })
                                    }
                                )
                                DropdownMenu(
                                    expanded = expandedquantity,
                                    onDismissRequest = { expandedquantity = false },
                                    modifier = Modifier
                                        .width(with(LocalDensity.current){textfieldquantity.width.toDp()})
                                ) {
                                    sizeList.forEach { label ->
                                        DropdownMenuItem(onClick = {
                                            selectedTextquantity = label
                                            expandedquantity = false
                                        }) {
                                            Text(text = label)

                                        }
                                    }
                                }
                            }


                           //Quantity dropdown
                            var expandedsize by remember { mutableStateOf(false) }
                            var textfieldSize by remember { mutableStateOf(Size.Zero) }


                            val icon = if (expandedsize)
                                Icons.Filled.KeyboardArrowUp
                            else
                                Icons.Filled.KeyboardArrowDown


                            Column(Modifier.padding(20.dp)) {
                                OutlinedTextField(
                                    value = selectedTextsize,
                                    onValueChange = {  selectedTextsize = it },
                                    modifier = Modifier
                                        .width(110.dp)
                                        .onGloballyPositioned { coordinates ->
                                            //This value is used to assign to the DropDown the same width
                                            textfieldSize = coordinates.size.toSize()
                                        },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                                    ,
                                            label = {Text("Quantity")},
                                    trailingIcon = {
                                        Icon(icon,"contentDescription",
                                            Modifier.clickable { expandedsize = !expandedsize })
                                    }
                                )
                                DropdownMenu(
                                    expanded = expandedsize,
                                    onDismissRequest = { expandedsize = false },
                                    modifier = Modifier
                                        .width(with(LocalDensity.current){textfieldSize.width.toDp()})
                                ) {
                                    quantityList.forEach { label ->
                                        DropdownMenuItem(onClick = {
                                            selectedTextsize = label
                                            expandedsize = false
                                        }) {
                                            Text(text = label)
                                        }
                                    }
                                }
                            }

                        }

                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            onClick = {
                                val uid = FirebaseAuth.getInstance().uid
                                val database = FirebaseDatabase.getInstance()
                                val databaseRef = database.getReference("Cart")
                                val cartitem = CartItem(itemName.text,itemPrice.text,itemImage.text,selectedTextsize , selectedTextquantity)
                                databaseRef.child(uid!!).child(itemName.text).setValue(cartitem)
                                    .addOnSuccessListener {
                                        Toast.makeText(shopItemActivity, " Item Added to Cart", Toast.LENGTH_SHORT).show()
                                        Log.d("addtocart", "Item Added to Cart")}
                                    .addOnFailureListener { Log.d("addtocart", it.message.toString()) }
                            }
                        ) {
                            Text(text = "Add To Cart")
                        }
                    }
    }
}
