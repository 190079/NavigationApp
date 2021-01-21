package com.example.navigationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.perfmark.Tag

class Login : AppCompatActivity() {
    private lateinit var Emailvar:EditText
    private lateinit var PassVar:EditText
    private lateinit var SignInBtn:Button
    private lateinit var FAuth:FirebaseAuth
    private lateinit var Register:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Emailvar = findViewById(R.id.EmailEntry)
        PassVar = findViewById(R.id.PassEntry)
        SignInBtn = findViewById(R.id.SigninBtn)
        Register = findViewById(R.id.SignUpTxt)
        FAuth = Firebase.auth
        SignInBtn.setOnClickListener {
            var EmailVal=Emailvar.text.toString().trim()
            var PassVal=PassVar.text.toString().trim()
            if (TextUtils.isEmpty(EmailVal)){
                Emailvar.setError("Email is Empty")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(PassVal)){
                PassVar.setError("Password is Empty")
                return@setOnClickListener
            }
            else{
                FAuth.signInWithEmailAndPassword(EmailVal, PassVal)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success")
                            Toast.makeText(this@Login, "Authentication Successful.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext,MenuActivity::class.java)
                            startActivity(intent)
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
        Register.setOnClickListener{
            val intent = Intent(applicationContext,SignUP::class.java)
            startActivity(intent)
            return@setOnClickListener
        }
    }

}