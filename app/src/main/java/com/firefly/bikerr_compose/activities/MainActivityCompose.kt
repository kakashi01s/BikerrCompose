package com.firefly.bikerr_compose.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.navigation.BottomBarScreen
import com.firefly.bikerr_compose.navigation.BottomNavGraph
import com.firefly.bikerr_compose.viewmodel.ViewModelmain
import com.google.firebase.auth.FirebaseAuth


class MainActivityCompose : ComponentActivity() {


    private val mainViewModel by viewModels<ViewModelmain>()
    var email : String= ""
    val uid = FirebaseAuth.getInstance().uid




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            Bikerr_composeTheme {


                    val navController = rememberNavController()
                    mainViewModel.getCategoryItems()
                    mainViewModel.getSpareParts()
                    mainViewModel.CreateStreamUser()
                    mainViewModel.getRentalCategory()
                mainViewModel.getRentals()
                mainViewModel.streamunreadMessages()
                mainViewModel.getUserDetails(uid = uid!!)

                    // A surface container using the 'background' color from the theme
                    BottomAppbarWithFab(navController = navController, this, mainViewModel)
                }
            }

        }




    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }


}


@Composable
fun BottomAppbarWithFab(
    navController: NavHostController,
    mainActivityCompose: MainActivityCompose,
    mainViewModel: ViewModelmain
) {


    val screens = listOf(
        BottomBarScreen.Feed,
        BottomBarScreen.Shop,
        BottomBarScreen.Null,
        BottomBarScreen.Traccar,
        BottomBarScreen.Profile

    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(backgroundColor = Color.Black, onClick = { /*TODO*/ }) {
                IconButton(
                    onClick = {
                        val intent = Intent(mainActivityCompose, TraccarActivity::class.java)
                        mainActivityCompose.startActivity(intent)
                    },
                    content = {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                tint = Color.LightGray,
                                imageVector = Icons.Filled.MyLocation,
                                contentDescription = ""
                            )
                            Text(
                                color = Color.LightGray,
                                text = "Track",
                                style = TextStyle(fontSize = 12.sp)
                            )
                        }
                    },

                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        bottomBar = {
            BottomAppBar(
                backgroundColor = Color.Black,
                cutoutShape = AbsoluteRoundedCornerShape(60),
                content = {
                    BottomNavigation(
                        backgroundColor = Color.Black.copy(alpha = 1f),
                        modifier = Modifier
                            .height(70.dp)
                            .fillMaxWidth()

                    ) {
                            screens.forEach { screen ->
                                AddItem(
                                    screen = screen,
                                    currentDestination = currentDestination,
                                    navController = navController
                                )
                            }
                    }
                },
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()


            )
        }) {
        BottomNavGraph(navHostController = navController, mainActivityCompose,mainViewModel)
    }

}

@Composable
fun RowScope.AddItem(
    screen : BottomBarScreen,
    currentDestination : NavDestination?,
    navController : NavHostController
) {
    BottomNavigationItem(
        modifier = Modifier
            .padding(10.dp)
            .align(Alignment.CenterVertically),
        label = {
            Text(text = screen.title)
        },
        icon = {
            screen.icon?.let { Icon(modifier = Modifier.weight(4f), imageVector = it, contentDescription = "") }
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = { navController.navigate(screen.route) },
        selectedContentColor = Color.White,
        unselectedContentColor = Color.LightGray,
        interactionSource = remember { MutableInteractionSource() },
        alwaysShowLabel = false
    )
}




