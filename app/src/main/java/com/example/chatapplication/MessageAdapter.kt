package com.example.chatapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context:Context,val messageList: ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val Item_Receive=1;
    private val Item_Sent=2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

             if(viewType==1){
                   val view:View=LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
                    return ReceiveViewHolder(view)
             }else{
                 val view:View=LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
                 return SentViewHolder(view)
             }

    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage=messageList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return Item_Sent
        }else{
            return Item_Receive
        }

    }

    override fun getItemCount(): Int {
return messageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMessage=messageList[position]
  if(holder.javaClass==SentViewHolder::class.java){
      val viewHolder=holder as SentViewHolder

      viewHolder.sentMessage.text=currentMessage.message
  }else{
      val viewHolder=holder as ReceiveViewHolder
      viewHolder.receiveMessage.text=currentMessage.message
  }
    }
    class SentViewHolder(itemView: View):ViewHolder(itemView){
    val sentMessage=itemView.findViewById<TextView>(R.id.txt_sent_message)

    }
    class ReceiveViewHolder(itemView: View):ViewHolder(itemView){
        val receiveMessage=itemView.findViewById<TextView>(R.id.txt_receive_message)

    }

}