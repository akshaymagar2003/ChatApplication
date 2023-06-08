package com.example.chatapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(val context: Context, val userList: ArrayList<User>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_USER = 1
    private val VIEW_TYPE_GROUP = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_USER -> {
                val view = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
                UserViewHolder(view)
            }
            VIEW_TYPE_GROUP -> {
                val view = LayoutInflater.from(context).inflate(R.layout.group_layout, parent, false)
                GroupViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun getItemViewType(position: Int): Int {
        // Return the view type based on the position or other logic
        return VIEW_TYPE_USER
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_USER -> {
                val userViewHolder = holder as UserViewHolder
                val currentUser = userList[position]
                userViewHolder.textName.text = currentUser.name
                userViewHolder.itemView.setOnClickListener {
                    val intent = Intent(context, ChatActivity::class.java)
                    intent.putExtra("name", currentUser.name)
                    intent.putExtra("uid", currentUser.uid)
                    context.startActivity(intent)
                }
            }
            VIEW_TYPE_GROUP -> {
                val groupViewHolder = holder as GroupViewHolder
                groupViewHolder.itemView.setOnClickListener {
                    // Handle the click event for group chat
                    val intent = Intent(context, ChatActivity::class.java)
                    intent.putExtra("chatType", "group")
                    // Pass any necessary information for group chat
                    context.startActivity(intent)
                }
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName = itemView.findViewById<TextView>(R.id.txt_name)
    }

    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define views for group chat if needed
    }
}

