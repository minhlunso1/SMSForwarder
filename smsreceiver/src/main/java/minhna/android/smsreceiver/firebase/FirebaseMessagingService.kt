package minhna.android.smsreceiver.firebase

import android.annotation.TargetApi
import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build
import android.media.RingtoneManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessagingService
import minhna.android.smsreceiver.Constant
import minhna.android.smsreceiver.MainActivity
import minhna.android.smsreceiver.R

/**
 * Created by minhnguyen on 3/28/18.
 */
class FirebaseMessagingService : FirebaseMessagingService() {
    var notiId = 0

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        Log.d(Constant.KEY.FIREBASE, "From: " + remoteMessage!!.from!!)

        if (remoteMessage.data.size > 0) {
            val data: Map<String, Any> = remoteMessage.data
            Log.d(Constant.KEY.FIREBASE, "Message data payload: " + data)
            sendNotification(data.get("title").toString(), data.get("message").toString())
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(Constant.KEY.FIREBASE, "Message Notification Body: " + remoteMessage.notification!!.body!!)
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun sendNotification(messageFrom: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_bravesoft)
                .setContentTitle(messageFrom)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    "Chat Channel",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notiId++ , notificationBuilder.build())
    }
}
