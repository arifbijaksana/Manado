package com.haerul.manado.utils;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.haerul.manado.BuildConfig;
import com.haerul.manado.R;
import com.haerul.manado.base.BaseActivity;
import com.haerul.manado.base.BaseViewModel;
import com.haerul.manado.databinding.ActivityMapBinding;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapActivity extends BaseActivity<ActivityMapBinding, BaseViewModel>
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private final static int AUTOCOMPLETE_REQUEST_CODE = 2;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static String TAG = "MAP LOCATION";
    private ActivityMapBinding binding;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    Boolean getMyLocation = true;
    private JSONObject json;
    private double LongLocation = 0;
    private double LatLocation = 0;

    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(1000 * 5)         // 5 seconds
            .setFastestInterval(1000 * 5)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    @Override
    public int getBindingVariable() {
        return com.haerul.manado.BR._all;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    public BaseViewModel getViewModel() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();

        String JSON_DATA = getIntent().getStringExtra("JSON");
        Log.d(TAG, "onCreate " + JSON_DATA);
        try {
            json = new JSONObject();
            if (JSON_DATA != null) {
                json = new JSONObject(JSON_DATA);
                double lon = json.getDouble("LONGITUDE");
                double lat = json.getDouble("LATITUDE");
                Log.w("TAG ===>>>>", String.valueOf(json));
                if (lon != 0 && lat != 0) {
                    getMyLocation = false;
                    LongLocation = lon;
                    LatLocation = lat;
                }
            }
            if (LongLocation == 0 && LatLocation == 0) {
                String LAST_LON = Util.getStringPreference(MapActivity.this, "LAST_LON");
                String LAST_LAT = Util.getStringPreference(MapActivity.this, "LAST_LAT");
                if (LAST_LON != null && LAST_LAT != null) {
                    getMyLocation = false;
                    LongLocation = Double.parseDouble(LAST_LON.replaceAll(",", "."));
                    LatLocation = Double.parseDouble(LAST_LAT.replaceAll(",", ""));
                    Log.i(TAG, "Get value from preff");
                    Log.i(TAG, String.valueOf(LongLocation));
                    Log.i(TAG, String.valueOf(LatLocation));
                }

                Log.w("TAG ===>>>> 2", String.valueOf(json));
                getMyLocation = true;

            }
            binding.setLocation.setOnClickListener(v -> {
                showProgress();
                
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                Intent data = new Intent();

                try {
                    json.remove("LONGITUDE");
                    json.remove("LATITUDE");

                    json.put("LONGITUDE", LongLocation);
                    json.put("LATITUDE", LatLocation);

                    Util.putPreference(MapActivity.this, "LAST_LON", String.format("%.9f", LongLocation));
                    Util.putPreference(MapActivity.this, "LAST_LAT", String.format("%.9f", LatLocation));
                    Util.putPreference(MapActivity.this, "SNAPSHOT", "");
                    Util.putPreference(MapActivity.this, "SNAPSHOT_PATH", "");

                    data.putExtra("JSON", String.valueOf(json));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.w("TAG", String.valueOf(json));
                setResult(RESULT_OK, data);
                finish();

                hideProgress();
                
            });
            
            Log.i(TAG, String.valueOf(LongLocation));
            Log.i(TAG, String.valueOf(LatLocation));
            setMap();
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize place API
                if (!Places.isInitialized()) {
                    Places.initialize(getBaseContext(), BuildConfig.G_API_KEY);
                }

                List<Place.Field> fields = Arrays.asList(Place.Field.ID,
                        Place.Field.NAME, Place.Field.LAT_LNG);

                List<TypeFilter> typeFilters = new ArrayList<>(Arrays.asList(TypeFilter.values()));

                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields)
                        .setCountry("ID")
                        .setTypeFilter(typeFilters.get(0))
                        .build(MapActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                LongLocation = place.getLatLng().longitude;
                LatLocation = place.getLatLng().latitude;
                updateMarker(place.getLatLng());
                getMyLocation = false;
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void setMap() {
        Log.d(TAG, "ON Map Init");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            if (!Util.isLocationEnabled(getBaseContext())) {
                // notify user
                AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseContext());
                dialog.setMessage("Location not enabled!");
                dialog.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub
                    }
                });
                dialog.show();
            }
            buildGoogleApiClient();
        } else {
            Toast.makeText(getBaseContext(), "Location not supported in this device", Toast.LENGTH_SHORT).show();
        }
    }

    public void onNormalMap(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void onSatelliteMap(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public void onTerrainMap(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }


    public void onHybridMap(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    protected synchronized void buildGoogleApiClient() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "ON Start");
        super.onStart();
        try {
            mGoogleApiClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        Log.d(TAG, "ON Stop");
        try {

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        ;
        super.onStop();
    }

    private void updateMarker(LatLng latlong) {
        try {
            mMap.clear();
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latlong)
                    .title("Location"));
            marker.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        try {
            if (mGoogleApiClient.isConnected())
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0x123: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                    initLocation();
//                    mMap.setMyLocationEnabled(true);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                0x123);
                    }
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            Log.d(TAG, "ON connected");
            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    REQUEST,
                    this);  // LocationListener
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        try {
            mGoogleApiClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "ON Connection Failed");
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "ON Location Changed");
        try {
            if (getMyLocation && location != null) {
                LongLocation = location.getLongitude();
                LatLocation = location.getLatitude();

                LatLng ll = new LatLng(LatLocation, LongLocation);
                updateMarker(ll);
                CameraUpdate center = CameraUpdateFactory.newLatLng(ll);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

                mMap.moveCamera(center);
                mMap.animateCamera(zoom);

                getMyLocation = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        //Load animation 
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnCameraMoveStartedListener(i -> {
            mMap.clear();
            Log.d(TAG, "onCameraMoveStarted");
            binding.progress.setVisibility(View.VISIBLE);
            binding.marker.setVisibility(View.INVISIBLE);
            binding.setLocation.setText(" ... ");
            binding.setLocation.requestLayout();
            binding.setLayout.startAnimation(slide_down);
            binding.layoutAddress.setVisibility(View.INVISIBLE);
        });

        mMap.setOnCameraIdleListener(() -> {
            Log.d(TAG, "onCameraIdle");
            binding.layoutAddress.setVisibility(View.VISIBLE);
            binding.progress.setVisibility(View.INVISIBLE);
            binding.marker.setVisibility(View.VISIBLE);
            binding.setLocation.setText(" Pick this location ");
            binding.setLocation.requestLayout();
            LongLocation = mMap.getCameraPosition().target.longitude;
            LatLocation = mMap.getCameraPosition().target.latitude;
            updateMarker(mMap.getCameraPosition().target);
            Log.d(TAG + ">", String.valueOf(LongLocation));
            Log.d(TAG + ">", String.valueOf(LatLocation));
            LatLng location = mMap.getCameraPosition().target;
            Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
            String result = null;
            try {
                List<Address> addressList = geocoder.getFromLocation(
                        location.latitude, location.longitude, 1);
                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);
                    result = address.getLocality() != null ? address.getLocality() + ", " : "" +
                            address.getPostalCode() != null ? address.getPostalCode() + ", " : "" +
                            address.getCountryName() != null ? address.getCountryName() : "";
                }
            } catch (IOException e) {
                Log.e(TAG, "Unable connect to Geocoder", e);
                binding.textAddress.setVisibility(View.GONE);
                binding.layoutAddress.setVisibility(View.GONE);
            } finally {
                result = "Latitude: " + location.latitude + "\nLongitude: " + location.longitude +
                        "\n\nAddress:\n" + result;
                binding.textAddress.setText(result);
            }
            binding.setLayout.startAnimation(slide_up);

        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                requestPermissions(new String[]{ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        0x123);
                return;
            }
        }

        googleMap.setMyLocationEnabled(true);

        LatLng ll = new LatLng(LatLocation, LongLocation);
        updateMarker(ll);

        CameraPosition lastPosition = new CameraPosition.Builder()
                .target(ll).zoom(19f).tilt(90).build();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LatLocation, LongLocation), 10));

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(lastPosition));
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //finish();
            }
            return false;
        }
        return true;
    }
}
