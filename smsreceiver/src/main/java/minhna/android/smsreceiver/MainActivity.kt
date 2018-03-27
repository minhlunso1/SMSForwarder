package minhna.android.smsreceiver

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSet.setOnClickListener(this)
        btnGetList.setOnClickListener (this)
        setupView()
    }

    private fun setupView() {
        if (AP.getStringData(this, Constant.KEY.ID) != null)
            btnGetList.visibility = View.VISIBLE
        else
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
        AP.saveData(this, Constant.KEY.ID, inputId.text.toString())
        btnGetList.visibility = View.VISIBLE
        this.toast(getString(R.string.done))
    }
}
