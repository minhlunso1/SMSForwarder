package minhna.android.smsreceiver

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import minhna.android.smsreceiver.model.UserUpdateRequest
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import minhna.android.smsreceiver.api.ManagerAPI

class MainActivity : AppCompatActivity(), View.OnClickListener {
    val PLAY_SERVICES_RESOLUTION_REQUEST = 9000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSet.setOnClickListener(this)
        btnGetList.setOnClickListener (this)
        btnGetList2.setOnClickListener(this)

        checkPlayServices()
        setupView()
    }

    private fun setupView() {
        if (AP.getStringData(this, Constant.KEY.ID) != null) {
            setBtnGetVisibility(View.VISIBLE)
            inputId.setText(AP.getStringData(this, Constant.KEY.ID))
        } else
            setBtnGetVisibility(View.GONE)
    }

    private fun setBtnGetVisibility(visibility: Int) {
        btnGetList.visibility = visibility
        btnGetList2.visibility = visibility
    }

    override fun onClick(v: View?) {
        when (v?.id)  {
            R.id.btnSet -> configId()
            R.id.btnGetList -> ListMessageActivity.startActivity(this, Constant.CODE.FIRESTORE)
            R.id.btnGetList2 -> ListMessageActivity.startActivity(this, Constant.CODE.OWN_SERVER)
        }
    }

    private fun configId() {
        inputId.isEnabled = false
        AP.saveData(this, Constant.KEY.ID, inputId.text.toString())
        setBtnGetVisibility(View.VISIBLE)
        if (FirebaseInstanceId.getInstance().getToken() != null) {
            val request = UserUpdateRequest(inputId.text.toString(), FirebaseInstanceId.getInstance().getToken()!!,
                    Build.DEVICE)
            ManagerAPI().registerReceiver(request)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ response ->
                            toast(getString(R.string.done))
                            inputId.isEnabled = true },
                        { error ->
                            error.printStackTrace()
                            toast(getString(R.string.try_again))
                            inputId.isEnabled = true }
            )
        } else {
            this.toast(getString(R.string.try_again))
            inputId.isEnabled = true
        }
    }

    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show()
            } else {
                Log.d(Constant.KEY.APP, "This device is not supported.")
                this.toast(getString(R.string.device_not_supported))
                finish()
            }
            return false
        }
        return true
    }
}
