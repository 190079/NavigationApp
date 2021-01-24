package com.example.navigationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap

class SignUP : AppCompatActivity() {
    private lateinit var fnameget: EditText
    private lateinit var emailget: EditText
    private lateinit var Phoneget: EditText
    private lateinit var passget: EditText
    private lateinit var registerBtn: Button
    private lateinit var FAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_u_p)
        fnameget = findViewById(R.id.nameEntry)
        emailget = findViewById(R.id.emailEntry)
        Phoneget = findViewById(R.id.phoneEntry)
        passget = findViewById(R.id.passEntryRegister)
        registerBtn = findViewById(R.id.registerBtn)
        FAuth = Firebase.auth
        registerBtn.setOnClickListener {
            val fname = fnameget.text.toString()
            val email = emailget.text.toString()
            val Phone = Phoneget.text.toString()
            val password = passget.text.toString()
            if (TextUtils.isEmpty(fname)) {
                fnameget.setError("Full Name is Empty")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(email)) {
                emailget.setError("Email is Empty")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(Phone)) {
                Phoneget.setError("Phone Number is empty")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(password)) {
                passget.setError("Password is Empty")
                return@setOnClickListener
            } else {
                createEmailUser(fname, email, Phone, password)
            }
        }
    }

    private fun createEmailUser(fname: String, email: String, phone: String, password: String) {
        FAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isComplete) {
                val userID = FAuth.currentUser!!.uid
                storeUserInfo(fname, email, phone, userID)
                Log.d("TAG", "createUserWithEmail:success")
                startIndent(MenuActivity())
                return@addOnCompleteListener
            } else {
                // If sign in fails, display a message to the user.
                Log.w("TAG", "createUserWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                return@addOnCompleteListener

            }
        }

    }

    private fun startIndent(menuActivity: AppCompatActivity) {
        val indent = Intent(applicationContext, MenuActivity::class.java)
        startActivity(indent)
        return
    }

    private fun storeUserInfo(fname: String, email: String, phone: String, userID: String) {
        db = Firebase.firestore
        val DocRef = db.collection("users").document(userID)
        val user = hashMapOf<String,String>("Fname" to fname,
        "Email" to email,
        "Phone" to phone)
        db.collection("users").add(user).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("TAG","onSuccess: Profile is created" + userID)
            }
        }
    }
}