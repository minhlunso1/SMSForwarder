package minhna.android.smsreceiver

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_message.view.*
import minhna.android.smsreceiver.model.Message

/**
 * Created by minhnguyen on 3/27/18.
 */
class MessageAdapter(var list: List<Message>) : RecyclerView.Adapter<MessageAdapter.MessageVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageVH {
        return MessageVH(parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MessageVH, position: Int) {
        holder.bind(list.get(position))
    }

    class MessageVH(parent: ViewGroup): RecyclerView.ViewHolder(parent.inflate(R.layout.item_message)) {
        fun bind(item: Message) = with(itemView) {
            tvFrom.text = item.from
            tvBody.text = item.message
            if (item.created_date.isEmpty())
                tvDate.visibility = View.INVISIBLE
            else {
                tvDate.visibility = View.VISIBLE
                tvDate.text = item.created_date
            }
        }
    }
}