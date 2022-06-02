package com.firefly.bikerr_compose.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.model.CartItem
import com.firefly.bikerr_compose.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.skydoves.landscapist.glide.GlideImage

class ShopItemActivity : ComponentActivity() {
    private var product: Product = Product("","","","","")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bikerr_composeTheme {
                val gson = Gson()
                product  = gson.fromJson(intent.getStringExtra("product"), Product::class.java)

                // A surface container using the 'background' color from the theme
                Scaffold(topBar = {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),

                        ) {

                        Text(text = "Shop", fontWeight = FontWeight.Bold, fontSize = 30.sp)
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
                            Items(
                               product = product, this@ShopItemActivity
                            )
                    }

                }
            }
        }
    }
}

@Composable
fun Items(product: Product, shopItemActivity: ShopItemActivity) {
    var selectedTextquantity by remember { mutableStateOf("") }
    var selectedTextsize by remember { mutableStateOf("") }
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {


        Row(Modifier.fillMaxWidth().padding(5.dp),horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
            Text(text = product.Name.uppercase(), fontWeight = FontWeight.Bold, fontSize = 30.sp)
        }
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                   ImageSliderShop( product )
                }


                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Row {
                            Box(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(40.dp)
                                    .padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 5.dp)
                                    .clip(
                                        RoundedCornerShape(5.dp)
                                    )
                                    .background(color = Color(0xFF737373)), contentAlignment = Alignment.Center) {
                                Text(text = "₹"+product.Price, color = Color.White)
                            }
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
                                        }.clickable { expandedquantity = ! expandedquantity },
                                    label = {Text("Size")},
                                    trailingIcon = {
                                        Icon(iconquantity,"contentDescription")
                                    },
                                    enabled = false
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
                                        }.clickable { expandedsize = !expandedsize },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                                    ,
                                            label = {Text("Quantity")},
                                    trailingIcon = {
                                        Icon(icon,"contentDescription",)
                                    },
                                    enabled = false
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
                                if (selectedTextsize.isEmpty() && selectedTextquantity.isEmpty()){
                                    Toast.makeText(shopItemActivity,"Please select Quantity and size",Toast.LENGTH_LONG).show()
                                }
                                else
                                {
                                    val uid = FirebaseAuth.getInstance().uid
                                    val database = FirebaseDatabase.getInstance()
                                    val databaseRef = database.getReference("Cart")
                                    val cartItem = CartItem(product,selectedTextsize , selectedTextquantity)
                                    databaseRef.child(uid!!).child(product.Name).setValue(cartItem)
                                        .addOnSuccessListener {
                                            Toast.makeText(shopItemActivity, " Item Added to Cart", Toast.LENGTH_SHORT).show()
                                            Log.d("addtocart", "Item Added to Cart")}
                                        .addOnFailureListener { Log.d("addtocart", it.message.toString()) }
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Default.AddShoppingCart, contentDescription = "")
                            Text(text = "Add To Cart")
                            }
                    }
    }
}

@Composable
fun ImageSliderShop(
   product: Product
) {
    val images = listOf(
    product.Image1,product.Image2,product.Image3
    )
    Log.d("slid",images.toString())

    com.firefly.bikerr_compose.common.Pager(
        items = images,
        modifier = Modifier
            .width(400.dp)
            .height(300.dp),
        itemFraction = .75f,
        overshootFraction = .75f,
        initialIndex = 0,
        itemSpacing = 60.dp,
        contentFactory = { item ->
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                GlideImage(imageModel = item)

            }
        }
    )
}