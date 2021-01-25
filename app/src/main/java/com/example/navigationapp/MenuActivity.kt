package com.example.navigationapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView

class MenuActivity : AppCompatActivity() {
    private var ViewMap: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Mapbox.getInstance(this, getString(R.string.mapboxaccess))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        ViewMap = findViewById(R.id.navigationView)

    }
}