package com.example.navigationapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

class HomeActivity : AppCompatActivity(), PermissionsListener, OnMapReadyCallback {
    private lateinit var mapboxMap: MapboxMap
    private var ViewMap: MapView? = null
    private lateinit var searchBtn: FloatingActionButton
    private var permissionsManager: PermissionsManager = PermissionsManager(this)
    private lateinit var mylocationBtn: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this,getString(R.string.mapboxaccess))
        setContentView(R.layout.activity_home)
        ViewMap = findViewById(R.id.mapView)
        mylocationBtn = findViewById(R.id.cameraBtn)
        ViewMap?.onCreate(savedInstanceState)
        ViewMap?.getMapAsync(this)
        mylocationBtn.setOnClickListener{
            enableLocationComponents()
        }


        }
    fun View.toggleVisibility(){
        if(visibility == View.VISIBLE){
            visibility = View.GONE
        }
        else{
            visibility = View.VISIBLE
        }
    }
    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(this, "This app requires location permissions to be enabled", Toast.LENGTH_LONG).show()
        return
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

    @SuppressLint("MissingPermission", "NewApi")
    private fun enableLocationComponents() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

// Create and customize the LocationComponent's options
            val customLocationComponentOptions = LocationComponentOptions
                    .builder(this).pulseEnabled(true).pulseColor(getColor(R.color.mapbox_blue)).pulseAlpha(.3f).pulseInterpolator(BounceInterpolator())
                    .trackingGesturesManagement(true)
                    .accuracyColor(ContextCompat.getColor(this, R.color.mapbox_blue))
                    .build()

            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(this, mapboxMap.style!!)
                    .locationComponentOptions(customLocationComponentOptions).useDefaultLocationEngine(true)
                    .build()

// Get an instance of the LocationComponent and then adjust its settings
            
            mapboxMap.locationComponent.apply {

// Activate the LocationComponent with options
                activateLocationComponent(locationComponentActivationOptions)
// Enable to make the LocationComponent visible
                isLocationComponentEnabled = true

                if (cameraMode == CameraMode.TRACKING_COMPASS){
// Set the LocationComponent's camera mode
                cameraMode = CameraMode.TRACKING_GPS_NORTH

// Set the LocationComponent's render mode
                renderMode = RenderMode.GPS
                    zoomWhileTracking(5.0)
                }else{
                    cameraMode = CameraMode.TRACKING_COMPASS

// Set the LocationComponent's render mode
                    renderMode = RenderMode.COMPASS
                    zoomWhileTracking(10.0)

                }
            }

        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
            enableLocationComponents()
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

