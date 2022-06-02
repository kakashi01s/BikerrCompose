package com.firefly.bikerr_compose

import android.app.Application
import android.content.ContextWrapper
import android.content.Intent
import com.firefly.bikerr_compose.activities.MainActivityCompose
import com.pixplicity.easyprefs.library.Prefs
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.notifications.handler.NotificationConfig
import io.getstream.chat.android.client.notifications.handler.NotificationHandlerFactory
import io.getstream.chat.android.offline.model.message.attachments.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.configuration.Config
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.pushprovider.firebase.FirebasePushDeviceGenerator


class MainApplication : Application() {
     private  var chatClient : ChatClient? = null
    companion object {
        private var app: MainApplication? = null

    }
    override fun onCreate() {
        super.onCreate()
            app = this
        // Initialize the Prefs class
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
        val offlinePluginFactory = StreamOfflinePluginFactory(
            config = Config(
                backgroundSyncEnabled = true,
                userPresence = true,
                persistenceEnabled = true,
                uploadAttachmentsNetworkType = UploadAttachmentsNetworkType.NOT_ROAMING,
            ),
            appContext = this,
        )

        val notificationConfig = NotificationConfig(
            pushDeviceGenerators = listOf(FirebasePushDeviceGenerator()), pushNotificationsEnabled = true,
            shouldShowNotificationOnPush = {true}
        )

        val notificationHandler = NotificationHandlerFactory.createNotificationHandler(
            context = this,
            newMessageIntent = {
                    _: String,
                    channelType: String,
                    channelId: String,
                ->
                // Return the intent you want to be triggered when the notification is clicked
                val intent = Intent(this, MainActivityCompose::class.java).putExtra("channelId",channelId).putExtra("isFromNotification",true)
                intent
            }
        )
        // Set up the client for API calls and the domain for offline storage
       var  client = ChatClient.Builder("az987pt7e6m5", this)
            // Change log level
            .logLevel(ChatLogLevel.ALL)
            .notifications(notificationConfig,notificationHandler)
            .withPlugin(offlinePluginFactory)
            .build()


        chatClient =ChatClient.instance()


    }


}
