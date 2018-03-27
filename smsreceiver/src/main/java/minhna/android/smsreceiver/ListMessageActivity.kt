package minhna.android.smsreceiver

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_list_message.*

/**
 * Created by minhnguyen on 3/27/18.
 */
class ListMessageActivity: AppCompatActivity() {
    val db by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_message)
        FirebaseApp.initializeApp(this)
    }

    override fun onStart() {
        super.onStart()
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
                        rv.layoutManager = LinearLayoutManager(this)
                        rv.adapter = MessageAdapter(list)
                    } else {

                    }
                })

    }
}