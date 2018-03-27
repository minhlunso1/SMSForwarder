package minhna.android.smsforwarder

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import minhna.android.smsforwarder.Constant.CODE.Companion.SMS_PERMISSION_CODE

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSet.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id)  {
            R.id.btnSet -> {
                if (!isSmsPermissionGranted()) {
                    showRequestPermissionsInfoAlertDialog(true)
                }
            }
        }
    }

    fun isSmsPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
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

    fun showRequestPermissionsInfoAlertDialog(makeSystemRequest: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.permission_alert_dialog_title)
        builder.setMessage(R.string.permission_dialog_message)

        builder.setPositiveButton(R.string.action_ok, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.dismiss()
                if (makeSystemRequest) {
                    requestReadAndSendSmsPermission()
                }
            }
        })

        builder.setCancelable(false)
        builder.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            SMS_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0) {
                    for (index in 0..grantResults.size - 1) {
                        if (grantResults[index] != PackageManager.PERMISSION_GRANTED)
                            break
                    }
                    //ToDo
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }
}
