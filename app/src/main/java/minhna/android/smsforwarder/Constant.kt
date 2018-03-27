package minhna.android.smsforwarder

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
        }
    }
}