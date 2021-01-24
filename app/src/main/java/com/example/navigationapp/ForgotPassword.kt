package com.example.navigationapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class ForgotPassword : AppCompatActivity() {
    private lateinit var emailVar:EditText
    private lateinit var sendBtn:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        emailVar = findViewById(R.id.emailEntryForgot)
        sendBtn = findViewById(R.id.sendBtn)
        sendBtn.setOnClickListener {
            val email = emailVar.text.toString()
            resetPassword(email)
        }
    }

    private fun resetPassword(email: String) {

    }
}