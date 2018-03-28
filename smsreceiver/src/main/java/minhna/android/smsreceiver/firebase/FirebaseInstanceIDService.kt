package minhna.android.smsreceiver.firebase

import android.os.Build
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import minhna.android.androidarchitecturecomponent.api.ManagerAPI
import minhna.android.smsreceiver.AP
import minhna.android.smsreceiver.Constant
import minhna.android.smsreceiver.model.UserUpdateRequest

/**
 * Created by minhnguyen on 3/28/18.
 */
class FirebaseInstanceIDService : FirebaseInstanceIdService() {
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(Constant.KEY.FIREBASE, "Refreshed token: " + refreshedToken!!)
        sendRegistrationToServer(refreshedToken)
    }

    private fun sendRegistrationToServer(token: String?) {
        if (AP.getStringData(this, Constant.KEY.ID) != null && token != null) {
            val request = UserUpdateRequest(AP.getStringData(this, Constant.KEY.ID), token,
                    Build.DEVICE)
            ManagerAPI().registerReceiver(request)
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        response -> Log.d(Constant.KEY.FIREBASE, "token is sent") },
                        { error -> error.printStackTrace() })
        }
    }
}