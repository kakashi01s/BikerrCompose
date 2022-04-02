package com.firefly.bikerr_compose.activities

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.screens.login.TextFieldState

class UriState(){
    var uri: Uri? by mutableStateOf(null)
}
class AddFeedActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val postCaption = remember { TextFieldState() }
            val imageUri =  remember { UriState() }
            Bikerr_composeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                        Scaffold(topBar = {
                            Row(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth(),
                            ) {
                                Text( text = "New Feed", fontWeight = FontWeight.Bold, fontSize = 30.sp)
                                Row(Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End) {
                                    Button(onClick = { /*TODO*/ }) {
                                        Text(text = "Post")
                                    }
                                }
                            }

                        }) {
                                RequestContentPermission(postCaption, imageUri)

                        }
                }
            }
        }
    }
}



@Composable
fun RequestContentPermission(postCaption: TextFieldState = TextFieldState(), imageUri: UriState = UriState()) {

    val context = LocalContext.current
    val bitmap =  remember {
        mutableStateOf<Bitmap?>(null)
    }

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri.uri = uri
    }
    Column {
        OutlinedTextField(
            value = postCaption.text,
            onValueChange = { postCaption.text = it },
            label = { Text(text = "Post Caption Here") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            launcher.launch("image/*")
        }) {
            Text(text = "Select image")
        }

        Spacer(modifier = Modifier.height(12.dp))

        imageUri.uri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)

            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver,it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }
            bitmap.value?.let {  btm ->
                Image(bitmap = btm.asImageBitmap(),
                    contentDescription =null,
                    modifier = Modifier.size(400.dp))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))



    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Bikerr_composeTheme {

    }
}