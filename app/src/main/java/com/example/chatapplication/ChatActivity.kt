package com.example.chatapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapplication.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var database: DatabaseReference
    private lateinit var groupId: String
    private var isGroupChat = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve intent extras
        val intent = intent
        val name = intent.getStringExtra("name")
        val uid = intent.getStringExtra("uid")
        groupId = intent.getStringExtra("groupId") ?: ""

        // Check if it's a group chat
        isGroupChat = !groupId.isEmpty()

        // Set the title based on the chat type
        supportActionBar?.title = if (isGroupChat) "Group Chat" else name

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().reference
        // Set up RecyclerView
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        messageAdapter = MessageAdapter(this, ArrayList())
        binding.chatRecyclerView.adapter = messageAdapter

        // Load messages based on chat type
        if (isGroupChat) {
            loadGroupMessages()
        } else {
            if (uid != null) {
                loadIndividualMessages(uid)
            }
        }

        // Send button click listener
        binding.sentButton.setOnClickListener {
            if (uid != null) {
                sendMessage(uid)
            }
        }
    }

    private fun loadIndividualMessages(uid: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val senderId = currentUser?.uid
        val receiverId = uid

        val chatRef = database.child("Chats")

        if (senderId != null) {
            chatRef.child(senderId).child(receiverId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val messageList = ArrayList<Message>()
                    for (snapshot in dataSnapshot.children) {
                        val message = snapshot.getValue(Message::class.java)
                        message?.let { messageList.add(it) }
                    }
                    messageAdapter.updateMessageList(messageList)
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("ChatActivity", "Error: ${databaseError.message}")
                }
            })
        }

        if (senderId != null) {
            chatRef.child(receiverId).child(senderId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val messageList = ArrayList<Message>()
                    for (snapshot in dataSnapshot.children) {
                        val message = snapshot.getValue(Message::class.java)
                        message?.let { messageList.add(it) }
                    }
                    messageAdapter.updateMessageList(messageList)
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("ChatActivity", "Error: ${databaseError.message}")
                }
            })
        }
    }




    private fun loadGroupMessages() {
        Log.d("iamdon","group")
        database.child("Groups").child(groupId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val messageList = ArrayList<Message>()
                for (snapshot in dataSnapshot.children) {
                    val message = snapshot.getValue(Message::class.java)
                    message?.let { messageList.add(it) }
                }
                messageAdapter.updateMessageList(messageList)
                messageAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("ChatActivity", "Error: ${databaseError.message}")
            }
        })
    }

    private fun sendMessage(uid: String) {
        val messageText = binding.messageBox.text.toString().trim()

        if (messageText.isNotEmpty()) {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val senderId = currentUser?.uid
            val receiverId = uid

            val message = Message(messageText, senderId, receiverId, groupId)

            // Save message to database
            val messageId = database.child("Chats").child(senderId ?: "").child(receiverId).push().key
            database.child("Chats").child(senderId ?: "").child(receiverId).child(messageId ?: "").setValue(message)

            // Clear input field
            binding.messageBox.text.clear()
        }
    }
}
