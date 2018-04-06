package minhna.android.smsreceiver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_list_message.*
import minhna.android.smsreceiver.api.ManagerAPI
import minhna.android.smsreceiver.model.Message

/**
 * Created by minhnguyen on 3/27/18.
 */
class ListMessageActivity: AppCompatActivity() {
    companion object {
        fun startActivity(context: Context, code: Int) {
            var intent = Intent(context, ListMessageActivity::class.java)
            intent.putExtra(Constant.KEY.ID, code)
            context.startActivity(intent)
        }
    }

    val db by lazy { FirebaseFirestore.getInstance() }
    var code = Constant.CODE.FIRESTORE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_message)

        pb.visibility = View.VISIBLE

        if (intent != null)
            code = intent.getIntExtra(Constant.KEY.ID, Constant.CODE.FIRESTORE)

        when (code) {
            Constant.CODE.FIRESTORE -> {
                FirebaseApp.initializeApp(this)
                db.collection(AP.getStringData(this, Constant.KEY.ID))
                        .get()
                        .addOnCompleteListener( { task ->
                            if (task.isSuccessful) {
                                val list = mutableListOf<Message>()
                                for (document in task.result) {
                                    val pairList: List<Pair<String, Any>> = document.data.toList()
                                    for (item in pairList) {
                                        list.add(Message(item.first, item.second.toString()))
                                    }
                                }
                                setListRV(list)
                            } else {
                                onErrorGetList()
                            }
                })
            }
            Constant.CODE.OWN_SERVER -> {
                ManagerAPI().getListMessages(AP.getStringData(this, Constant.KEY.ID))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ response -> setListRV(response)},
                        { error ->
                            onErrorGetList()
                            error.printStackTrace() }
                )
            }
        }

    }

    private fun onErrorGetList() {
        toast(getString(R.string.error))
        pb.visibility = View.INVISIBLE
    }

    private fun setListRV(list: List<Message>) {
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = MessageAdapter(list)
        pb.visibility = View.INVISIBLE
    }
}
