package com.example.navigationapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.example.navigationapp.retrofit.models.IssModel;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

/**
 * Display the space station's real-time location
 */
public class testact extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener {

    private static final String TAG = "SpaceStationActivity";
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private Handler handler;
    private Runnable runnable;
    private PermissionsManager permissionsManager;
    private Call<IssModel> call;

    // apiCallTime is the time interval when we call the API in milliseconds, by default this is set
// to 2000 and you should only increase the value, reducing the interval will only cause server
// traffic, the latitude and longitude values aren't updated that frequently.
    private int apiCallTime = 5000;

    // Map variables
    private MapView mapView;
    private MapboxMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

// Mapbox access token is configured here. This needs to be called either in your application
// object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.mapboxaccess1));

// This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_testact);

// Initialize the MapView
        mapView = findViewById(R.id.mapView);
        mySwipeRefreshLayout = findViewById(R.id.refreshView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("Refresh", "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        myUpdateOperation();
                    }
                }
        );


    }

    private void myUpdateOperation() {
            Intent intent = new Intent(this, testact.class);
            startActivity(intent);
        }


    private void callApi() {

// Build our client, The API we are using is very basic only returning a handful of
// information, mainly, the current latitude and longitude of the International Space Station.
        Retrofit client = new Retrofit.Builder()
                .baseUrl("http://192.168.43.21:2999/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final IssApiService service = client.create(IssApiService.class);

// A handler is needed to called the API every x amount of seconds.
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
// Call the API so we can get the updated coordinates.
                call = service.loadLocation();
                call.enqueue(new Callback<IssModel>() {
                    @Override
                    public void onResponse(Call<IssModel> call, Response<IssModel> response) {
                        Layer layer = map.getStyle().getLayer("layer-id");
// We only need the latitude and longitude from the API.
                        if (response.body() != null) {
                            double latitude = response.body().getIssPosition().getLatitude();
                            double longitude = response.body().getIssPosition().getLongitude();
                            layer.setProperties(visibility(VISIBLE));
                            updateMarkerPosition(new LatLng(latitude, longitude));
                        }else{
                            if (layer != null) {
                                if (VISIBLE.equals(layer.getVisibility().getValue())) {
                                    layer.setProperties(visibility(NONE));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<IssModel> call, Throwable throwable) {
// If retrofit fails or the API was unreachable, an error will be called.
//to check if throwable is null, then give a custom message.
                        if (throwable.getMessage() == null) {
                        } else {
                        }

                    }
                });
// Schedule the next execution time for this runnable.
                handler.postDelayed(this, apiCallTime);
            }
        };

// The first time this runs we don't need a delay so we immediately post.
        handler.post(runnable);
    }

    private void initSpaceStationSymbolLayer(@NonNull Style style) {
        style.addImage("traffic-update",
                BitmapFactory.decodeResource(
                        this.getResources(), R.drawable.iss));

        style.addSource(new GeoJsonSource("source-id"));

        style.addLayer(new SymbolLayer("layer-id", "source-id").withProperties(
                iconImage("traffic-update"),
                iconIgnorePlacement(true),
                iconAllowOverlap(true),
                iconSize(.7f)
        ));

    }

    private void updateMarkerPosition(LatLng position) {
// This method is where we update the marker position once we have new coordinates. First we
// check if this is the first time we are executing this handler, the best way to do this is
// check if marker is null;
        if (map.getStyle() != null) {
            GeoJsonSource spaceStationSource = map.getStyle().getSourceAs("source-id");
            if (spaceStationSource != null) {
                spaceStationSource.setGeoJson(FeatureCollection.fromFeature(
                        Feature.fromGeometry(Point.fromLngLat(position.getLongitude(), position.getLatitude()))
                )
                );

            }

        }

// Lastly, animate the camera to the new position so the user
// wont have to search for the marker and then return.
        //map.animateCamera(CameraUpdateFactory.newLatLng(position));
        CameraUpdateFactory.newLatLng(position);
        getDialog();
    }

    private void getDialog() {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Traffic Alert!");
            alert.setIcon(R.drawable.alert);
            alert.setMessage("Jam Detected At Softwarica College");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.show();
        }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "This application requires location permission", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            map.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        map = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                initSpaceStationSymbolLayer(style);
                enableLocationComponent(style);
                callApi();

            }
        });

    }

    // Interface used for Retrofit.
    public interface IssApiService {
        @GET("status")
        Call<IssModel> loadLocation();

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
// When the user returns to the activity we want to resume the API calling.
        if (handler != null && runnable != null) {
            handler.post(runnable);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
// When the user leaves the activity, there is no need in calling the API since the map
// isn't in view.
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

// Get an instance of the component
            LocationComponent locationComponent = map.getLocationComponent();

// Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
            locationComponent.zoomWhileTracking(2.0);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }
}