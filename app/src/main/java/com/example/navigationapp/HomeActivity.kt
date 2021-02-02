package com.example.navigationapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style

class HomeActivity : AppCompatActivity(), PermissionsListener, OnMapReadyCallback {
    private lateinit var mapboxMap: MapboxMap
    private var ViewMap: MapView? = null
    private var permissionsManager: PermissionsManager = PermissionsManager(this)
    private lateinit var mylocationBtn: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this,getString(R.string.mapboxaccess))
        setContentView(R.layout.activity_home)
        ViewMap = findViewById(R.id.mapView)
        mylocationBtn = findViewById(R.id.mylocation)
        ViewMap?.onCreate(savedInstanceState)
        ViewMap?.getMapAsync(this)
        mylocationBtn.setOnClickListener{
            enableLocationComponents(mapboxMap.style!!)
        }
        }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(this, "This app requires location permissions to be enabled", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {

        } else {
            Toast.makeText(this,"Please permit location services which is required by this applicaiton", Toast.LENGTH_LONG).show()
            finish()
        }    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(getString(R.string.defaultStyle)){
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponents(loadedMapStyle: Style) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

// Create and customize the LocationComponent's options
            val customLocationComponentOptions = LocationComponentOptions.builder(this)
                    .trackingGesturesManagement(true)
                    .accuracyColor(ContextCompat.getColor(this, R.color.green))
                    .build()

            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(this, loadedMapStyle)
                    .locationComponentOptions(customLocationComponentOptions)
                    .build()

// Get an instance of the LocationComponent and then adjust its settings
            mapboxMap.locationComponent.apply {

// Activate the LocationComponent with options
                activateLocationComponent(locationComponentActivationOptions)

// Enable to make the LocationComponent visible
                isLocationComponentEnabled = true

// Set the LocationComponent's camera mode
                cameraMode = CameraMode.TRACKING

// Set the LocationComponent's render mode
                renderMode = RenderMode.COMPASS
            }
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        ViewMap?.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        ViewMap?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        ViewMap?.onLowMemory()
    }
}

