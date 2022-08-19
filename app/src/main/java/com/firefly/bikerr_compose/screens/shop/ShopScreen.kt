package com.firefly.bikerr_compose.screens.shop

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firefly.bikerr_compose.activities.*
import com.firefly.bikerr_compose.model.Categories
import com.firefly.bikerr_compose.model.Stores
import com.firefly.bikerr_compose.viewmodel.MainViewModel
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ShopScreen(mainActivity: MainActivityCompose, mainViewModel: MainViewModel)
{
    var sparePartslist by remember { mutableStateOf(listOf<Stores>()) }
    var categorylist by remember { mutableStateOf(listOf<Categories>()) }
    categorylist = mainViewModel.categorylist.value
    sparePartslist = mainViewModel.sparePartsList.value
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
                            Card(shape = RoundedCornerShape(20.dp), elevation = 5.dp, backgroundColor = Color.LightGray) {
                                IconButton(onClick = {
                                    val intent = Intent(mainActivity, CartActivity::class.java)
                                    mainActivity.startActivity(intent)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ShoppingCart,
                                        contentDescription = "cart"
                                    )
                                }
                            }
                        }
            }
    }) {
        Divider(color = Color.LightGray, thickness = 5.dp)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            // Spare Parts List
            Row {
                sparePartslist.let {

                    LazyRow {
                        items(it) {
                            SparePartsItem(stores = it)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
            Text(modifier = Modifier.padding(start = 10.dp), text = "Categories", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.size(10.dp))

            // Categories List
            Column {
                val visible by remember { mutableStateOf(true) }
                val density = LocalDensity.current
                    categorylist.let {
                                    LazyColumn {
                                                    items(it)
                                                    {
                                                        AnimatedVisibility(
                                                            visible = true,
                                                            enter = scaleIn(animationSpec = tween(100,100, easing = LinearEasing)),
                                                            exit = slideOutVertically() + shrinkVertically() + fadeOut()
                                                        ) {
                                                            CategoryBanner(category = it, mainActivity = mainActivity)
                                                        }

                                                    }
                                                }
                                    }
                            }
                        }
                }
        }



    @Composable
    fun SparePartsItem(stores: Stores) {
        val context = LocalContext.current
        Box(
            Modifier
                .padding(5.dp)
                .height(100.dp)
                .width(100.dp)
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(25.dp)
                )
                .clip(RoundedCornerShape(20.dp))
                .clickable {
                    val intent = Intent(context, WebActivity::class.java)
                    intent.putExtra("url", stores.url)
                    context.startActivity(intent)
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                GlideImage( // CoilImage, FrescoImage
                        imageModel = stores.Image,
                    modifier = Modifier.size(50.dp),
                // shows a shimmering effect when loading an image.
                shimmerParams = ShimmerParams(
                    baseColor = MaterialTheme.colors.background,
                    highlightColor = Color.LightGray,
                    durationMillis = 350,
                    dropOff = 0.65f,
                    tilt = 20f
                ),
                // shows an error text message when request failed.
                failure = {
                    Text(text = "image request failed.")
                })
                Text(text = stores.Name, textAlign = TextAlign.Center)
            }
        }
    }


    @Composable
    fun CategoryBanner(category: Categories, mainActivity: MainActivityCompose) {
        Column(
            Modifier
                .fillMaxSize()
                .clickable
                {
                    val intent = Intent(mainActivity, ShopCategoryActivity::class.java)
                    intent.putExtra("CategoryName", category.Name)
                    mainActivity.startActivity(intent)
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(Modifier.padding(10.dp)) {
                GlideImage( // CoilImage, FrescoImage
                    imageModel = category.Image,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    // shows a shimmering effect when loading an image.
                    shimmerParams = ShimmerParams(
                        baseColor = MaterialTheme.colors.background,
                        highlightColor = Color.LightGray,
                        durationMillis = 1000,
                        dropOff = 0.65f,
                        tilt = 20f
                    ),
                    // shows an error text message when request failed.
                    failure = {
                        Text(text = "image request failed.")
                    })
            }
        }
    }






