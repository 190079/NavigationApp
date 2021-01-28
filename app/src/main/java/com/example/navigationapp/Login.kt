package com.example.navigationapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.perfmark.Tag

class Login : AppCompatActivity() {
    private lateinit var Emailvar: EditText
    private lateinit var PassVar: EditText
    private lateinit var SignInBtn: TextView
    private lateinit var FAuth: FirebaseAuth
    private lateinit var Register: TextView
    private lateinit var ForgotPass: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setLayout()

    }

    private fun setLayout() {
        Emailvar = findViewById<EditText>(R.id.EmailEntry)
        PassVar = findViewById<EditText>(R.id.PassEntry)
        SignInBtn = findViewById<TextView>(R.id.SigninBtn)
        Register = findViewById<TextView>(R.id.SignUpTxt)
        ForgotPass = findViewById<TextView>(R.id.passForgot)
        FAuth = Firebase.auth
        ForgotPass.setOnClickListener {
            forgotPass()
        }
        Register.setOnClickListener {
            Signup()

        }
        SignInBtn.setOnClickListener {
            signin()


        }
    }

    private fun forgotPass() {
        startIntent(ForgotPassword())
    }

    private fun Signup() {
        startIntent(SignUP())
    }

    private fun signin() {
        var EmailVal = Emailvar.text.toString().trim()
        var PassVal = PassVar.text.toString().trim()
        if (TextUtils.isEmpty(EmailVal)) {
            Emailvar.setError("Email is Empty")
            return
        } else if (TextUtils.isEmpty(PassVal)) {
            PassVar.setError("Password is Empty")
            return
        } else {
            FAuth.signInWithEmailAndPassword(EmailVal, PassVal)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success")
                            Toast.makeText(this@Login, "Authentication Successful.", Toast.LENGTH_SHORT).show()
                            startIntent(MenuActivity())
                            return@addOnCompleteListener
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.exception)
                            Toast.makeText(this@Login, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                            return@addOnCompleteListener

                            // ...
                        }

                        // ...
                    }
        }
    }
    fun startIntent(activity: Activity) {
        val intent = Intent(applicationContext,activity::class.java)
        startActivity(intent)
        return
    }
}