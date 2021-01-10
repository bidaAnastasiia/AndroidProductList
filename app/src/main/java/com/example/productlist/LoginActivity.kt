package com.example.productlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()


        btSignup.setOnClickListener {
            auth.createUserWithEmailAndPassword(et_login.text.toString(),et_pass.text.toString())
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this,"Registration completed successfully",Toast.LENGTH_SHORT).show()

                    } else{
                        Toast.makeText(this,"Registration failed",Toast.LENGTH_SHORT).show()
                    }
                }
        }

        btLogin.setOnClickListener {
            auth.signInWithEmailAndPassword(et_login.text.toString(),et_pass.text.toString())
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this,"Login completed successfully",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))
                    } else{
                        Toast.makeText(this,"Login failed",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}