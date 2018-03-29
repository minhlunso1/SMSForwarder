package minhna.android.smsreceiver

import android.content.Intent
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
        checkPlayServices()
        setupView()
    }

    private fun setupView() {
        if (AP.getStringData(this, Constant.KEY.ID) != null) {
            btnGetList.visibility = View.VISIBLE
            inputId.setText(AP.getStringData(this, Constant.KEY.ID))
        } else
            btnGetList.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when (v?.id)  {
            R.id.btnSet -> configId()
            R.id.btnGetList -> {
                var intent = Intent(this, ListMessageActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun configId() {
        inputId.isEnabled = false
        AP.saveData(this, Constant.KEY.ID, inputId.text.toString())
        btnGetList.visibility = View.VISIBLE
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
