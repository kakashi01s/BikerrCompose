package com.firefly.bikerr_compose.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartActivity : ComponentActivity() {


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
                        GetCartItems(this@CartActivity, orderList)
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
    var quantity = 0
    for (i in orderList)
    {
        total += i.product?.Price!!.toLong()


    }
    Log.d("total", total.toString())
    db.getReference("Orders").child(uid!!).setValue(orderList)
    val intent = Intent(cartActivity,CheckoutOrderActivity::class.java)
    intent.putExtra("total", total)
    intent.putExtra("total", total)
    cartActivity.startActivity(intent)
}

@Composable
fun GetCartItems(cartActivity: CartActivity,  orderList: SnapshotStateList<CartItem> = remember { mutableStateListOf() }) {
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
        LazyColumn {
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
    Column {
        Row(Modifier.padding(5.dp)) {

            Image(painter = rememberImagePainter(data = item.product?.Image1), contentDescription = "",modifier = Modifier.size(150.dp))
            Column(Modifier.padding(5.dp)) {
                Row {
                    Text(text = item.product?.Name.toString(), fontWeight = FontWeight.Bold)
                }

                Row {
                    Text(text = "Price :       ", fontWeight = FontWeight.Bold)
                    Text(text = item.product?.Price.toString())
                }
                Row {
                    Text(text = "Size :         ")
                    Text(text = size.text)
                }
                
                Row {
                    Text(text = "Quantity :  ")
                    Row(
                        Modifier
                            .height(30.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {

                        Text(text = quantity.text)

                    }
                    Spacer(modifier = Modifier.size(5.dp))
                    Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.End) {
                        IconButton(onClick = {

                            val builder = androidx.appcompat.app.AlertDialog.Builder(cartActivity)
                            builder.setTitle("Remove Item")
                            builder.setMessage("Do You Want to Remove This Item")
                            builder.setPositiveButton("CANCEL"
                            ) { dialog, _ -> dialog?.cancel() }
                            builder.setNegativeButton("REMOVE"
                            ) { _, _ ->
                                val db = FirebaseDatabase.getInstance()
                                val uid = FirebaseAuth.getInstance().currentUser?.uid
                                db.getReference("Cart").child(uid!!).child(item.product?.Name.toString()).removeValue()
                                Log.d("removeitem", "Removed ${item.product?.Name}")
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

}
