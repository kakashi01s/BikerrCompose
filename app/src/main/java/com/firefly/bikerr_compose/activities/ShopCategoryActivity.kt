package com.firefly.bikerr_compose.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.model.Product
import com.firefly.bikerr_compose.screens.login.TextFieldState
import com.google.gson.Gson
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage

class ShopCategoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bikerr_composeTheme {
                val categoryName = remember { TextFieldState() }
                categoryName.text = intent.getStringExtra("CategoryName").toString()
                Log.d("hhhh",categoryName.text)
                // A surface container using the 'background' color from the theme
                Scaffold(topBar = {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),

                        ) {
                        Text(text = categoryName.text, fontWeight = FontWeight.Bold, fontSize = 30.sp)
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = {
                                val intent = Intent(this@ShopCategoryActivity,CartActivity::class.java)
                                startActivity(intent)
                            }) {
                                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "cart")
                            }

                        }

                    }
                }) {
                    Column {
                        getShopItems(categoryName)
                    }

                }
            }
        }
    }
}


@Composable
fun getShopItems(categoryName: TextFieldState) {
    var shopItemslist by remember { mutableStateOf(listOf<Product>()) }
    JetFirestore(path = {
        collection(categoryName.text)
    },
        onRealtimeCollectionFetch = { values, _ ->
            //When all documents are fetched
            //booksList = values.getListOfObjects()

            //When documents are fetched based on limit
            shopItemslist = shopItemslist + values.getListOfObjects()
        }) {
        shopItemslist.let {
            LazyColumn {
                items(it){
                    ShopItem(product = it)
                    Log.d("slid",it.toString())
                }
            }
        }
    }
}

@Composable
fun ShopItem(product: Product) {
    val context = LocalContext.current
    Box(
        Modifier
            .padding(5.dp)
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(25.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                val intent = Intent(context, ShopItemActivity::class.java)
               val gson = Gson()
                intent.putExtra("product",gson.toJson(product))
                context.startActivity(intent)

            },
        contentAlignment = Alignment.Center){
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
            elevation = 10.dp
        ) {
            Row() {
                GlideImage(imageModel = product.Image1,
                    shimmerParams = ShimmerParams(
                    baseColor = MaterialTheme.colors.background,
                    highlightColor = Color.LightGray,
                    durationMillis = 1000,
                    dropOff = 0.65f,
                    tilt = 20f),
                    modifier = Modifier.size(150.dp))


                Row (Modifier.padding(top = 5.dp, start = 5.dp)){


                    Column (Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly)
                    {

                        Row (Modifier.fillMaxWidth().fillMaxHeight()){
                            Text(text = product.Name.uppercase(), textAlign = TextAlign.Start, fontWeight = FontWeight.Bold)
                        }


                        Row(Modifier.height(100.dp).fillMaxWidth().padding(end = 10.dp), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom) {
                            Box(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(40.dp)
                                    .padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 5.dp)
                                    .clip(
                                        RoundedCornerShape(5.dp)
                                    )
                                    .background(color = Color(0xFF737373)), contentAlignment = Alignment.Center) {
                                Text(text = "₹"+product.Price)
                            }
                        }
                    }
                }
            }


        }
    }
}

