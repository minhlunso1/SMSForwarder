package minhna.android.smsreceiver

/**
 * Created by minhnguyen on 3/27/18.
 */
class Constant {
    interface ACTION {
        companion object {
            val STARTFOREGROUND_ACTION = "minhna.android.smsforwarder.action.startforeground"
            val STOPFOREGROUND_ACTION = "minhna.android.smsforwarder.action.stopforeground"
        }
    }

    interface CODE {
        companion object {
            val SMS_PERMISSION_CODE = 1
            val FOREGROUND_SERVICE = 101
            val FIRESTORE = 102
            val OWN_SERVER = 103
        }
    }

    interface KEY {
        companion object {
            val ID = "id"
            val FIREBASE = "firebase"
            val FIREBASE_TOKEN = "firebase_token"
            val APP = "app_receiver"
        }
    }
}