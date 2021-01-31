package com.example.navigationapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView

class HomeActivity : AppCompatActivity() {
    private var ViewMap: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this,getString(R.string.defaultStyle))
        setContentView(R.layout.activity_home)
        ViewMap = findViewById(R.id.mapView)
        ViewMap?.onCreate(savedInstanceState)
        ViewMap?.getMapAsync{ mapboxMap ->
            mapboxMap.setStyle(getString(R.string.defaultStyle))
        }
    }
}