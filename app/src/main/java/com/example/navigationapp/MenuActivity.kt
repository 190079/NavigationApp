package com.example.navigationapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView

class MenuActivity : AppCompatActivity() {
    private var ViewMap: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(applicationContext, getString(R.string.mapboxaccess))

        setContentView(R.layout.activity_menu)

        ViewMap = findViewById(R.id.viewmap)
        ViewMap?.onCreate(savedInstanceState)
        ViewMap?.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(getString(R.string.defaultStyle)){

            }
        }
    }
    override fun onStart() {
        super.onStart()
        ViewMap?.onStart()
    }

    override fun onResume() {
        super.onResume()
        ViewMap?.onResume()
    }

    override fun onPause() {
        super.onPause()
        ViewMap?.onPause()
    }

    override fun onStop() {
        super.onStop()
        ViewMap?.onStop()
    }


    override fun onLowMemory() {
        super.onLowMemory()
        ViewMap?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        ViewMap?.onDestroy()
    }
}