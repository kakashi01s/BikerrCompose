package com.firefly.bikerr_compose.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.model.CartItem
import com.firefly.bikerr_compose.screens.login.TextFieldState
import com.google.android.libraries.places.internal.i
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartActivity : ComponentActivity() {

    var checkout : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bikerr_composeTheme {
                val orderList = remember { mutableStateListOf<CartItem>() }

                Scaffold(topBar = {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),

                        ) {

                        Text(text = "Cart", fontWeight = FontWeight.Bold, fontSize = 30.sp)
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(onClick = {
                                sendDatatoorders(this@CartActivity, orderList)
                            }) {
                                Text(text = "Proceed to Payment")
                            }

                        }

                    }
                }) {
                    Column {
                        getCartItems(this@CartActivity, orderList)
                    }

                }
            }
        }
    }



}

fun sendDatatoorders(cartActivity: CartActivity, orderList: SnapshotStateList<CartItem>) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val db = FirebaseDatabase.getInstance()
    var total : Long = 0
    for (i in orderList)
    {
        total += i.price.toString().toLong()
    }
    Log.d("total", total.toString())
    db.getReference("Orders").child(uid!!).setValue(orderList)
    val intent = Intent(cartActivity,CheckoutActivity::class.java)
    intent.putExtra("total", total)
    cartActivity.startActivity(intent)
}

@Composable
fun getCartItems(cartActivity: CartActivity,  orderList: SnapshotStateList<CartItem> = remember { mutableStateListOf() }) {
    val db = FirebaseDatabase.getInstance()
    val cartitemList = remember { mutableStateListOf<CartItem>() }
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    db.getReference("Cart").child(uid!!)
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                for (i in snapshot.children){
                    val items = i.getValue(CartItem::class.java)
                    cartitemList.add(items!!)
                    orderList.add(items)
                }
                }
                }

            override fun onCancelled(error: DatabaseError) {
                Log.w("cartitems", "loadPost:onCancelled", error.toException())
            }
        })
    cartitemList.let {
        LazyColumn(){
            items(it){
              CartItem(item = it, cartActivity, cartitemList)
            }
        }
    }

}

@Composable
fun CartItem(item : CartItem, cartActivity: CartActivity,cartitemList : SnapshotStateList<CartItem>) {
    val quantity = remember {
        TextFieldState()
    }
    val size = remember {
        TextFieldState()
    }
    quantity.text = item.quantity.toString()
    size.text = item.Size.toString()
    Column() {
        Row(Modifier.padding(5.dp)) {

            Image(painter = rememberImagePainter(item.image), contentDescription ="" , modifier = Modifier.size(150.dp))
            Column(Modifier.padding(5.dp)) {
                Row() {
                    Text(text = item.name.toString(), fontWeight = FontWeight.Bold)
                }

                Row() {
                    Text(text = "Price :       ", fontWeight = FontWeight.Bold)
                    Text(text = item.price.toString())
                }
                Row() {
                    Text(text = "Size :         ")
                    Text(text = size.text)
                }
                
                Row() {
                    Text(text = "Quantity : ")
                    Row(
                        Modifier
                            .background(Color.LightGray)
                            .height(30.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {


                             }) {
                            Icon(imageVector = Icons.Default.Remove, contentDescription = "")
                        }
                        Text(text = quantity.text)
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "")
                        }
                    }
                    Spacer(modifier = Modifier.size(5.dp))
                    IconButton(onClick = {

                        val builder = androidx.appcompat.app.AlertDialog.Builder(cartActivity)
                        builder.setTitle("Remove Item")
                        builder.setMessage("Do You Want to Remove This Item")
                        builder.setPositiveButton("CANCEL"
                        ) { dialog, which -> dialog?.cancel() }
                        builder.setNegativeButton("REMOVE"
                        ) { dialog, which ->
                            val db = FirebaseDatabase.getInstance()
                            val uid = FirebaseAuth.getInstance().currentUser?.uid
                            db.getReference("Cart").child(uid!!).child(item.name.toString()).removeValue()
                            Log.d("removeitem", "Removed ${item.name}")
                            cartitemList.remove(item)
                            Log.d("cartList", cartitemList.toString())
                        }
                        builder.show()
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                    }
                }
                

            }

        }
    }

}
