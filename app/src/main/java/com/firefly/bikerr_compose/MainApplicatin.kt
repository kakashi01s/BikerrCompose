package com.firefly.bikerr_compose

import android.content.SharedPreferences
import androidx.multidex.MultiDexApplication
import com.firefly.bikerr_compose.apiinterface.WebService
import com.firefly.bikerr_compose.model.User
import com.parse.Parse
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.model.message.attachments.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.configuration.Config
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.*

class MainApplication : MultiDexApplication() {
    interface GetServiceCallback {
        fun onServiceReady(client: OkHttpClient?, retrofit: Retrofit?, service: WebService?)
        fun onFailure(): Boolean
    }

    private var preferences: SharedPreferences? = null
    private var client: OkHttpClient? = null
    private var service: WebService? = null
    private var retrofit: Retrofit? = null
    private var user: User? = null
    private val callbacks: MutableList<GetServiceCallback> = LinkedList()

    fun getService(): WebService? {
        return service
    }

    fun getUser(): User? {
        return user
    }

    fun removeService() {
        service = null
        user = null
    }

    override fun onCreate() {
        super.onCreate()
        val offlinePluginFactory = StreamOfflinePluginFactory(
            config = Config(
                backgroundSyncEnabled = true,
                userPresence = true,
                persistenceEnabled = true,
                uploadAttachmentsNetworkType = UploadAttachmentsNetworkType.NOT_ROAMING,
            ),
            appContext = applicationContext,
        )
        // Set up the client for API calls and the domain for offline storage
        val client = ChatClient.Builder("az987pt7e6m5", applicationContext)
            // Change log level
            .logLevel(ChatLogLevel.ALL)
            .withPlugin(offlinePluginFactory)
            .build()



        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build())
    }


}
