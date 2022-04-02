package com.firefly.bikerr_compose.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.model.Product
import com.firefly.bikerr_compose.screens.login.TextFieldState
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects

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
        onRealtimeCollectionFetch = { values, exception ->
            //When all documents are fetched
            //booksList = values.getListOfObjects()

            //When documents are fetched based on limit
            shopItemslist = shopItemslist + values.getListOfObjects()
        }) {
        shopItemslist.let {
            LazyColumn() {
                items(it){
                    ShopItem(product = it)
                }
            }
        }
    }
}

@Composable
fun ShopItem(product: Product, ) {
    val context = LocalContext.current
    Box(
        Modifier
            .padding(5.dp)
            .background(
                color = Color.White.copy(alpha = .5f),
                shape = RoundedCornerShape(25.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                val intent = Intent(context, ShopItemActivity::class.java)
                intent.putExtra("itemName", product.Name)
                intent.putExtra("itemPrice", product.Price.toString())
                intent.putExtra("itemImage", product.Image)
                context.startActivity(intent)

            },
        contentAlignment = Alignment.Center){
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Row() {
                Image(painter = rememberImagePainter(product.Image) , contentDescription = "", modifier = Modifier.size(150.dp))
                Row() {
                    Column() {
                        Text(text = product.Name, textAlign = TextAlign.Center)
                        Text(text = product.Price.toString(), textAlign = TextAlign.Center)
                    }
                }
            }


        }
    }
}

