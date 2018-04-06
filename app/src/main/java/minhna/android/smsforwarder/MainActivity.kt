package minhna.android.smsforwarder

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import minhna.android.smsforwarder.Constant.CODE.Companion.SMS_PERMISSION_CODE
import android.telephony.SubscriptionManager
import android.widget.AdapterView
import minhna.android.smsforwarder.Constant.CODE.Companion.PHONE_PERMISSION_CODE
import android.widget.ArrayAdapter

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSet.setOnClickListener(this)
        setupView()

        if (!isPhoneGranted()) {
            showRequestPermissionsInfoAlertDialog(true, PHONE_PERMISSION_CODE)
        } else
            checkPhoneSim()
    }

    private fun setupView() {
        if (AP.getStringData(this, Constant.KEY.ID) != null)
            inputId.setText(AP.getStringData(this, Constant.KEY.ID))
    }

    private fun checkPhoneSim() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            spinnerSim.visibility = View.VISIBLE
            val manager = SubscriptionManager.from(this)
            if (manager.activeSubscriptionInfoCount >= 1) {
                AP.saveData(this, Constant.KEY.SINGLE_SIM, false)
                val list = mutableListOf<String>()
                list.add(getString(R.string.label_both_sims))
                for (item in manager.activeSubscriptionInfoList) {
                    if (item.number != null)
                        list.add(item.number)
                    else
                        list.add("Sim " + item.simSlotIndex)
                }
                val dataAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list)
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerSim.setAdapter(dataAdapter)

                spinnerSim.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        AP.saveData(applicationContext, Constant.KEY.SIM_MODE, position - 1)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }
            }
            manager.activeSubscriptionInfoList
        }
    }

    override fun onClick(v: View?) {
        when (v?.id)  {
            R.id.btnSet -> {
                if (!isSmsPermissionGranted()) {
                    showRequestPermissionsInfoAlertDialog(true, SMS_PERMISSION_CODE)
                } else
                    configId()
            }
        }
    }

    private fun configId() {
        AP.saveData(this, Constant.KEY.ID, inputId.text.toString())
        this.toast(getString(R.string.done))
    }

    fun isPhoneGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
        else
            return true;
    }

    fun isSmsPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
        else
            return true;
    }

    /**
     * Request runtime SMS permission
     */
    private fun requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
            // You may display a non-blocking explanation here, read more in the documentation:
            // https://developer.android.com/training/permissions/requesting.html
        }
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS), SMS_PERMISSION_CODE)
    }

    private fun requestPermission(permissions: Array<String>, permissionCode: Int) {
        ActivityCompat.requestPermissions(this, permissions, permissionCode)
    }

    fun showRequestPermissionsInfoAlertDialog(makeSystemRequest: Boolean, requestCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.permission_alert_dialog_title)
        builder.setMessage(R.string.permission_dialog_message)

        builder.setPositiveButton(R.string.action_ok, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.dismiss()
                if (makeSystemRequest) {
                    when (requestCode) {
                        SMS_PERMISSION_CODE -> requestReadAndSendSmsPermission()
                        PHONE_PERMISSION_CODE -> requestPermission(arrayOf(Manifest.permission.READ_PHONE_STATE), PHONE_PERMISSION_CODE)
                    }

                }
            }
        })

        builder.setCancelable(false)
        builder.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            SMS_PERMISSION_CODE -> {
                var flag = true
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0) {
                    for (index in 0..grantResults.size - 1) {
                        if (grantResults[index] != PackageManager.PERMISSION_GRANTED)
                            flag = false
                    }
                    if (flag)
                        configId()
                }
                return
            }
            PHONE_PERMISSION_CODE -> {
                var flag = true
                if (grantResults.size > 0) {
                    for (index in 0..grantResults.size - 1) {
                        if (grantResults[index] != PackageManager.PERMISSION_GRANTED)
                            flag = false
                    }
                    if (flag)
                        checkPhoneSim()
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }
}
