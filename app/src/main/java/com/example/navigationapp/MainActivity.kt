package com.example.navigationapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAuth = Firebase.auth
        var user: FirebaseUser? = firebaseAuth.currentUser
        if(user == null){
            val intent = Intent(applicationContext,Login::class.java)
            startActivity(intent)
            return
        }
        else{
            val intent = Intent(applicationContext,MenuActivity::class.java)
            startActivity(intent)
            return
        }

    }
}