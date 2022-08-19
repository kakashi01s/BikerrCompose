package com.firefly.bikerr_compose.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.firefly.bikerr_compose.R
import com.firefly.bikerr_compose.common.CustomReactionIconFactory
import com.firefly.bikerr_compose.model.MyRides
import com.firefly.bikerr_compose.viewmodel.ViewModelMessagesActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.pixplicity.easyprefs.library.Prefs
import com.xinto.overlappingpanels.OverlappingPanels
import com.xinto.overlappingpanels.rememberOverlappingPanelsState
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QuerySort
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.Member
import io.getstream.chat.android.compose.ui.components.composer.MessageInput
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.messages.composer.MessageComposer
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.messages.AttachmentsPickerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageComposerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageListViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory

class MessagesActivity : AppCompatActivity() {
    private val client = ChatClient.instance()
    private val member = mutableStateOf(listOf<Member>())
    private val factory by lazy {
        MessagesViewModelFactory(
            context = this,
            channelId = intent.getStringExtra(KEY_CHANNEL_ID) ?: "",
        )
    }
    private val viewModel by viewModels<ViewModelMessagesActivity>()

    private val listViewModel by viewModels<MessageListViewModel>(factoryProducer = { factory })
    private val attachmentsPickerViewModel by viewModels<AttachmentsPickerViewModel>(factoryProducer = { factory })
    private val composerViewModel by viewModels<MessageComposerViewModel>(factoryProducer = { factory })

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val channelId = intent.getStringExtra(KEY_CHANNEL_ID) ?: return

        setContent {
            viewModel.getMyPlans(channelId)
            val panelsState = rememberOverlappingPanelsState()


            OverlappingPanels(
                modifier = Modifier.fillMaxSize(),
                panelsState = panelsState,
                panelStart = {

                             ChannelDetails(channelId)
                },
                panelCenter = {
                    ChatTheme(isInDarkMode = true,
                        reactionIconFactory = CustomReactionIconFactory()) {
                        MessagesScreen(
                            channelId = channelId,
                            onBackPressed = { finish() },
                        )
                    }
                },
                panelEnd = {
                           PlanRideInChannel(channelId)
                },
            )

        }
    }

    @Composable
    fun MyCustomComposer() {
        MessageComposer(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            viewModel = composerViewModel,
            integrations = {},
            input = { inputState ->
                MessageInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(7f)
                        .padding(start = 8.dp),
                    messageComposerState = inputState,
                    onValueChange = { composerViewModel.setMessageInput(it) },
                    onAttachmentRemoved = { composerViewModel.removeSelectedAttachment(it) },
                    label = {
                        Row(
                            Modifier.wrapContentWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Image, contentDescription = null)

                            Text(
                                modifier = Modifier.padding(start = 4.dp),
                                text = "Type something",
                                color = ChatTheme.colors.textLowEmphasis
                            )
                        }
                    },
                    innerTrailingContent = {
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple()
                                ) {
                                    val state = composerViewModel.messageComposerState.value

                                    composerViewModel.sendMessage(
                                        composerViewModel.buildNewMessage(
                                            state.inputValue, state.attachments
                                        )
                                    )
                                },
                            imageVector = Icons.Default.Send,
                            tint = ChatTheme.colors.primaryAccent,
                            contentDescription = null
                        )
                    },
                )
            },
            trailingContent = { Spacer(modifier = Modifier.size(8.dp)) }
        )
    }


    @Composable
    fun ChannelDetails(channelId: String) {
        Scaffold(topBar = {

            Row(Modifier.fillMaxWidth()) {
                Text(text = "ChannelDetails", fontSize = 30.sp , fontWeight = FontWeight.ExtraBold)
            }

        }) {


            val filterByNone = Filters.neutral()
            val channelClient = client.channel(channelId)

            val offset = 0 // Use this value for pagination
            val limit = 10
            val sort = QuerySort<Member>()

// 2. Call queryMembers with that filter
            channelClient.queryMembers(offset, limit, filterByNone, sort).enqueue { result ->
                if (result.isSuccess) {
                    member.value = result.data()
                } else {
                    Log.e("member", String.format("There was an error %s", result.error()), result.error().cause)
                }
            }
            Column(Modifier.padding(10.dp)) {
                Text(text = "Members:")
                LazyColumn(Modifier.padding(10.dp))
                {

                    items(member.value)
                    { member ->
                        //Need to create channel member composable
                        Text(text = member.user.name)
                    }
                }
            }


        }
    }
    @Composable
    fun PlanRideInChannel(channelId: String) {
        Log.d("rdrd",channelId)
        Scaffold(topBar = {
        Row(
            Modifier
                .background(color = Color.White)
                .padding(10.dp)) {
            Row() {
                Text(text = "Rides", fontSize = 30.sp , fontWeight = FontWeight.ExtraBold)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {


                    OutlinedButton( onClick = {
                        val intent = Intent(this@MessagesActivity,PlanRidesActivity::class.java)
                        intent.putExtra("cid",channelId)
                        this@MessagesActivity.startActivity(intent)
                    }) {
                        Icon(imageVector = Icons.Default.AddRoad, contentDescription ="",tint = Color.DarkGray)
                        Text(text = "Plan Ride", color = Color.DarkGray)
                    }


            }
        }
        }) {

            viewModel.planList.value.let {
                if (it.isNotEmpty())
                {
                    LazyColumn(Modifier.padding(10.dp)){
                        items(it){ plan ->

                            RidesComposable(plan,channelId)
                        }
                    }
                }
                else
                    {
                    Column(verticalArrangement = Arrangement.Center) {
                        Row() {
                            Icon(imageVector = Icons.Default.NotAccessible, contentDescription = "")
                            Text(text = "No Plans Made Yet!")
                        }
                        Text(text = "Create a Plan By clicking on Top right Button")
                    }
                    }

            }
        }
    }


    @Composable
    fun RidesComposable(plan: MyRides, channelId: String) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        var isExpanded by remember { mutableStateOf(false) }
        Card(
            modifier = Modifier
                .padding(5.dp),
            elevation = 5.dp,
            shape = RoundedCornerShape(10.dp),
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)) {
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Card(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(5.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = 5.dp
                    ) {
                        Image(painter = rememberImagePainter(data = R.drawable.bikerr_logo), contentDescription = "",Modifier.size(100.dp))

                    }
                    Column() {
                        Text(text = plan.rideTitle.capitalize(), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                        Text(text = "by:${plan.createdBy}")
                    }
                    
                }
                Box() {
                    Column(Modifier.fillMaxWidth()) {
                        Row(Modifier.fillMaxWidth()) {
                            Text(text = "Ride Date : ", fontWeight = FontWeight.SemiBold)
                            Text(text = plan.rideDate)
                        }
                        Row(Modifier.fillMaxWidth()) {
                            Text(text = "Start/From : ", fontWeight = FontWeight.SemiBold)
                            Text(text = plan.rideStartFrom)
                        }
                        Row(Modifier.fillMaxWidth()) {
                            Text(text = "End/To : ", fontWeight = FontWeight.SemiBold)
                            Text(text = plan.rideEndTo)
                        }
                        Row(Modifier.fillMaxWidth()) {
                            Text(text = "Meet-UP Point : ", fontWeight = FontWeight.SemiBold)
                            Text(text = plan.meetupPoint)
                        }
                        Column(Modifier.fillMaxWidth()) {
                            Text(text = "Description : ", fontWeight = FontWeight.SemiBold)
                            Text(text = plan.rideDesc)
                        }

                        Column(Modifier.fillMaxWidth()) {
                            Row() {
                                OutlinedButton(onClick = {
                                    val members: MutableList<String>? = plan.members?.toMutableList()
                                    val ride = MyRides(plan.id, members,plan.rideDesc,plan.rideStartFrom,plan.rideEndTo,plan.rideDate,plan.rideTitle,plan.meetupPoint,plan.createdBy)
                                    if (plan.members.isNullOrEmpty())
                                    {
                                        val ridei = MyRides(plan.id, listOf(Prefs.getString("userName").toString()),plan.rideDesc,plan.rideStartFrom,plan.rideEndTo,plan.rideDate,plan.rideTitle,plan.meetupPoint,plan.createdBy)
                                        db.collection("Rides").document(channelId).collection("Plans").document(plan.id).set(ridei).addOnSuccessListener {
                                            Log.d("jjjj","Joined")
                                        }.addOnFailureListener {
                                            Log.d("jjjj",it.message.toString())
                                        }
                                    }
                                    else if (!plan.members.isNullOrEmpty())
                                    {
                                        plan.members?.forEach { i ->
                                            if (i == Prefs.getString("userName"))
                                            {
                                                members?.remove(Prefs.getString("userName"))
                                                db.collection("Rides").document(channelId).collection("Plans").document(plan.id).set(ride).addOnSuccessListener {
                                                    Log.d("jjjj","Removed")
                                                }.addOnFailureListener {
                                                    Log.d("jjjj",it.message.toString())
                                                }
                                            }
                                            else{
                                                members?.add(Prefs.getString("userName"))
                                                Log.d("members",members.toString())
                                                db.collection("Rides").document(channelId).collection("Plans").document(plan.id).set(ride).addOnSuccessListener {
                                                    Log.d("jjjj","Joined")
                                                }.addOnFailureListener {
                                                    Log.d("jjjj",it.message.toString())
                                                }
                                            }
                                        }


                                    }


                                }, shape = RoundedCornerShape(20.dp), ) {
                                    Row {

                                        val isJoined = mutableStateOf(false)
                                        plan.members?.forEach { i ->
                                            if (i == Prefs.getString("userName")) {
                                                isJoined.value = true
                                            } else if (i != Prefs.getString("userName")) {
                                                isJoined.value = false
                                            }
                                        }
                                        Text(text = if (isJoined.value) "Joined" else "Join")

                                    }
                                }
                                
                            }
                            
                        }
                        Box(modifier = Modifier.fillMaxWidth()){
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable { isExpanded = !isExpanded }, horizontalArrangement = Arrangement.Start) {
                                Text(text = "All Members :")
                                Icon(imageVector = Icons.Default.ArrowRight, contentDescription ="" )
                                Text(text = plan.members.toString(),
                                maxLines = if (isExpanded) Int.MAX_VALUE else 1)
                            }
                        }
                        
                    }
                    
                }
                
                
            }


        }
    }
    companion object {
        private const val KEY_CHANNEL_ID = "channelId"
        fun createIntent(context: Context, channelId: String): Intent {
            return Intent(context, MessagesActivity::class.java).apply {
                putExtra(KEY_CHANNEL_ID, channelId)
            }
        }
    }
}



