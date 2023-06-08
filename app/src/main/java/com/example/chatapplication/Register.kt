package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatapplication.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityRegisterBinding
    private lateinit var db: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()

        binding.Continue.setOnClickListener {
            if(checking())
            {
                var email=binding.EmailRegister.text.toString()
                var password= binding.PasswordRegister.text.toString()
                var name=binding.Name.text.toString()
                var uid= auth.currentUser?.uid
                var phone=binding.Phone.text.toString()
                val user= hashMapOf(
                    "Name" to name,
                    "uid" to uid,
                    "Phone" to phone,
                    "email" to email
                )
                signUp(name,email,password)

            }
            else{
                Toast.makeText(this,"Enter the Details", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun signUp(name:String,email: String, password: String) {
          auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){task->
              if(task.isSuccessful){
                  auth.currentUser?.uid?.let { addUserToDatabase(name,email, it) }

                val intent =Intent(this@Register,MainActivity::class.java)
                 startActivity(intent)
              } else{
                  Toast.makeText(this@Register,"Some error occured",Toast.LENGTH_SHORT).show()
              }
          }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
          db=FirebaseDatabase.getInstance().getReference()
        db.child("user").child(uid).setValue(User(name,email,uid))
    }


    private fun checking():Boolean{
        if(binding.Name.text.toString().trim{it<=' '}.isNotEmpty()
            && binding.Phone.text.toString().trim{it<=' '}.isNotEmpty()
            && binding.EmailRegister.text.toString().trim{it<=' '}.isNotEmpty()
            && binding.PasswordRegister.text.toString().trim{it<=' '}.isNotEmpty()
        )
        {
            return true
        }
        return false
    }
}