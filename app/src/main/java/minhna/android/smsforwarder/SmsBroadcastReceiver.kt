package minhna.android.smsforwarder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Created by minhnguyen on 3/27/18.
 */

class SmsBroadcastReceiver() : BroadcastReceiver() {

    private var serviceProviderNumber: String = ""
    private var serviceProviderSmsCondition: String = ""

    constructor(defineProviderNumber: String, defineProviderSmsCondition: String): this() {
        serviceProviderNumber = defineProviderNumber
        serviceProviderSmsCondition = defineProviderSmsCondition
    }

    private var listener: Listener? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            var smsSender = ""
            var smsBody = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    smsSender = smsMessage.getDisplayOriginatingAddress()
                    smsBody += smsMessage.getMessageBody()
                }
            } else {
                val smsBundle = intent.getExtras()
                if (smsBundle != null) {
                    val pdus = smsBundle!!.get("pdus") as Array<Any>
                    if (pdus == null) {
                        // Display some error to the user
                        Log.e(TAG, "SmsBundle had no pdus key")
                        return
                    }
                    val messages = arrayOfNulls<SmsMessage>(pdus.size)
                    for (i in messages.indices) {
                        messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                        smsBody += messages[i]?.getMessageBody()
                    }
                    smsSender = messages[0]!!.getOriginatingAddress()
                }
            }


            pushServer(context, smsSender, smsBody)

//            if (smsSender == serviceProviderNumber && smsBody.startsWith(serviceProviderSmsCondition)) {
//                if (listener != null) {
//                    listener!!.onTextReceived(smsBody)
//                }
//            }
        }
    }

    private fun pushServer(context: Context, smsSender: String, smsBody: String) {
        FirebaseApp.initializeApp(context)
        val db = FirebaseFirestore.getInstance()
        val item = mutableMapOf<String, Any>()
        item.put(smsSender, smsBody)

        db.collection(AP.getStringData(context, Constant.KEY.ID))
                .add(item)
                .addOnSuccessListener { documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.id) }
                .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    internal fun setListener(listener: Listener) {
        this.listener = listener
    }

    internal interface Listener {
        fun onTextReceived(text: String)
    }

    companion object {
        private val TAG = "SmsBroadcastReceiver"
    }
}