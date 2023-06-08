package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapplication.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
   private lateinit var binding:ActivityChatBinding
   private lateinit var messageAdapter: MessageAdapter
   private lateinit var messageList: ArrayList<Message>
   private  lateinit var  mdb:DatabaseReference
   var receiverRoom:String?=null
    var senderRoom:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name =intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid= FirebaseAuth.getInstance().currentUser?.uid
        mdb=FirebaseDatabase.getInstance().getReference()
        senderRoom=receiverUid+senderUid
        receiverRoom=senderUid+receiverUid
        supportActionBar?.title=name
        messageList=ArrayList()
        messageAdapter=MessageAdapter(this,messageList)

        binding.chatRecyclerView.layoutManager=LinearLayoutManager(this)
       binding.chatRecyclerView.adapter=messageAdapter
        mdb.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnapshot in snapshot.children){
                        val message =postSnapshot.getValue(Message::class.java)
                       messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


        binding.sentButton.setOnClickListener{
            val message=binding.messageBox.text.toString()
            val messageObject=Message(message,senderUid)

            mdb.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mdb.child("chats").child(receiverRoom!!).child("messages").push().setValue(messageObject)
                }
            binding.messageBox.setText("")
        }



    }
}