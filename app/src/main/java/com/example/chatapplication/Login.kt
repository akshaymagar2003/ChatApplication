package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatapplication.databinding.ActivityLoginBinding
import com.example.chatapplication.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()
        binding.Register.setOnClickListener {
            var intent =Intent(this,Register::class.java)
            startActivity(intent)
            finish()
        }


        binding.Login.setOnClickListener {
            if(checking()){
                val email=binding.Email.text.toString()
                val password= binding.Password.text.toString()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            var intent =Intent(this,MainActivity::class.java)
                            intent.putExtra("email",email)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Wrong Details", Toast.LENGTH_LONG).show()
                        }
                    }
            }
            else{
                Toast.makeText(this,"Enter the Details", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun checking():Boolean
    {
        if(binding.Email.text.toString().trim{it<=' '}.isNotEmpty()
            && binding.Password.text.toString().trim{it<=' '}.isNotEmpty())
        {
            return true
        }
        return false
    }
}