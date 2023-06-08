package com.example.chatapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.chatapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {
  private lateinit var userList:ArrayList<User>
  private lateinit var adapter: UserAdapter
    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityMainBinding
    private  lateinit var mdb:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        userList = ArrayList()
        mdb=FirebaseDatabase.getInstance().getReference()
        adapter = UserAdapter(this, userList)
       binding.userRecyclerView.layoutManager=LinearLayoutManager(this)
        binding.userRecyclerView.adapter=adapter


        mdb.child("user").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                 userList.clear()
                 for(postSnapshot in snapshot.children){
                     val currentuser=postSnapshot.getValue(User::class.java)
                 if(auth.currentUser?.uid!=currentuser?.uid){
                     userList.add(currentuser!!)
                 }

                 }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            auth.signOut()
            val intent =Intent(this@MainActivity,Login::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }
}



