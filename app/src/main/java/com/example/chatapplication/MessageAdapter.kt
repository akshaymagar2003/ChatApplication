package com.example.chatapplication
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, private var messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val Item_Sent = 1
    private val Item_Received = 2
    private val Item_Group = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)

        return when (viewType) {
            Item_Sent -> {
                val view = inflater.inflate(R.layout.sent, parent, false)
                SentViewHolder(view)
            }
            Item_Received -> {
                val view = inflater.inflate(R.layout.receive, parent, false)
                ReceivedViewHolder(view)
            }
            Item_Group -> {
                val view = inflater.inflate(R.layout.group_layout, parent, false)
                GroupViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        when (holder.itemViewType) {
            Item_Sent -> {
                val sentViewHolder = holder as SentViewHolder
                sentViewHolder.sentMessage.text = currentMessage.message
            }
            Item_Received -> {
                val receivedViewHolder = holder as ReceivedViewHolder
                receivedViewHolder.receivedMessage.text = currentMessage.message
            }
            Item_Group -> {
                val groupViewHolder = holder as GroupViewHolder
                groupViewHolder.groupSenderName.text = currentMessage.senderName
                groupViewHolder.groupMessage.text = currentMessage.message
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        return when {
            FirebaseAuth.getInstance().currentUser?.uid == currentMessage.senderId -> Item_Sent
            currentMessage.groupId != null -> Item_Group
            else -> Item_Received
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
    fun updateMessageList(messages: ArrayList<Message>) {
        messageList = messages
    }
    inner class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage: TextView = itemView.findViewById(R.id.txt_sent_message)
    }

    inner class ReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receivedMessage: TextView = itemView.findViewById(R.id.txt_receive_message)
    }

    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupSenderName: TextView = itemView.findViewById(R.id.txt_group_sender_name)
        val groupMessage: TextView = itemView.findViewById(R.id.txt_group_message)
    }
}
