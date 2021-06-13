package hiutrun.example.myweather.firebase

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.RemoteException
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import hiutrun.example.myweather.R
import hiutrun.example.myweather.ui.main.view.MainActivity

class WeatherFirebaseMessage : FirebaseMessagingService() {

    @SuppressLint("WrongConstant")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        var title = ""
        var body = ""
        var name = ""

        remoteMessage.notification?.let { notification ->
            title = notification.title.toString()
            body = notification.body.toString()
        }

        remoteMessage.data.let { data ->
            name = data["name"].toString()
        }

        pushNotification(title,body)
    }

    private fun pushNotification(title:String, body:String){
        val CHANNEL_ID = getString(R.string.default_notification_channel_id)
        var builder = NotificationCompat.Builder(applicationContext,CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_cloud)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        with(NotificationManagerCompat.from(applicationContext)){
            notify(0,builder.build())
        }
    }
}