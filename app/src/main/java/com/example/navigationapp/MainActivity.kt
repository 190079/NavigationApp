package com.example.navigationapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.reflect.KClass


class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var defLayout:AppCompatActivity = Login()
        firebaseAuth = Firebase.auth
        var user: FirebaseUser? = firebaseAuth.currentUser
        if(user != null){
             defLayout = HomeActivity()
        }
        startIntent(defLayout)

    }
    fun startIntent(activity: Activity) {
        val intent = Intent(applicationContext,activity::class.java)
        startActivity(intent)
        return
    }
}