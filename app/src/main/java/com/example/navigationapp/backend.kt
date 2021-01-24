package com.example.navigationapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

public class backend{
    public lateinit var fAuth:FirebaseAuth
    public var uname: FirebaseUser? = fAuth.currentUser

}