package com.haerul.manado.ui.login2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.haerul.manado.BuildConfig;
import com.haerul.manado.R;
import com.haerul.manado.base.BaseActivity2;
import com.haerul.manado.data.api.ConnectionServer;
import com.haerul.manado.databinding.ActivityLogin2Binding;
import com.haerul.manado.databinding.ActivityLoginBinding;
import com.haerul.manado.ui.SplashScreen;
import com.haerul.manado.utils.Constants;
import com.haerul.manado.utils.Util;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

//import androidx.lifecycle.viewModel2Providers;

public abstract class LoginActivity2 extends BaseActivity2<ActivityLogin2Binding, LoginViewModel2> implements LoginViewModel2.LoginNavigator,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_CHECK_LOCATION = 0x129;
    @Inject
    ConnectionServer connectionServer;

    private ActivityLoginBinding binding;
    private LoginViewModel2 viewModel2;
    private boolean permissionNotAllowed;

    private LocationRequest mLocationRequestHigh;
    private LocationCallback mLocationCallback;
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location location;

    protected static final int REQUEST_CODE_RESOLUTION = 1;
    private static final String KEY_IN_RESOLUTION = "is_in_resolution";
    private boolean mIsInResolution;

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

//    @Override
//    public LoginviewModel2 getviewModel2() {
//        return viewModel2;
//    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestPermission();
//
//        if (Util.getBooleanPreference(this, Constants.IS_LOGIN)) {
//            MainActivity.navigateToMain(this);
//            finish();
//        } else {
//            Util.stopJobService(this, "job");
//            initGoogleClient();
//            binding = getViewDataBinding();
//            viewModel2 = viewModelProviders.of(this, new LoginViewModel2(getApplicationContext(), connectionServer)).get(LoginViewModel2.class);
//            binding.setViewModel2(viewModel2);
//            viewModel2.setNavigator(this);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        initGoogleClient();
    }

    private void initGoogleClient() {
        Log.wtf("INIT GOOGLE CLIENT", "CALLED");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    private void requestPermission() {
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.CAMERA)
                .check();
    }

    boolean login() {
        if (binding.username.getText().toString().equals("")) {
            binding.username.setError("Username can't be empty!");
            binding.username.requestFocus();
            return false;
        }
        else if (binding.password.getText().toString().equals("")) {
            binding.password.setError("Password can't be empty!");
            binding.password.requestFocus();
            return false;
        }
        return true;
    }

//    @Override
//    public void onLogin() {
//
//        hideKeyboard();
//
//        if (!permissionNotAllowed) {
//
//            loginProcess();
//
//        } else {
//            requestPermission();
//        }
//    }

    private void loginProcess() {
        createFile();

        if (login()) {

            if (binding.checkbox.isChecked()) {
                Util.putPreference(LoginActivity2.this, "password_save", true);
                Util.putPreference(LoginActivity2.this, Constants.USER_LOGIN_NAME, binding.username.getText().toString());
                Util.putPreference(LoginActivity2.this, Constants.USER_PASSWORD, binding.password.getText().toString());
            }

            JsonObject objectInfo = new JsonObject();
            objectInfo.addProperty("brand", Build.BRAND);
            objectInfo.addProperty("model", Build.MODEL);
            objectInfo.addProperty("sdk", Build.VERSION.SDK_INT);
            objectInfo.addProperty("release", Build.VERSION.RELEASE);
            objectInfo.addProperty("codename", Build.VERSION.CODENAME);
            objectInfo.addProperty("version_app", BuildConfig.VERSION_NAME);

            JsonObject object = new JsonObject();
            object.addProperty(Constants.USER_LOGIN_NAME, binding.username.getText().toString().trim());
            object.addProperty(Constants.USER_PASSWORD, binding.password.getText().toString().trim());
            object.addProperty("lat_login", viewModel2.lat);
            object.addProperty("lon_login", viewModel2.lon);
            object.addProperty("device_info", objectInfo.toString());
            viewModel2.postLogin(object, binding.username.getText().toString().trim(), binding.password.getText().toString().trim());
        }
    }

//    @Override
//    public void loginResult(boolean isLogin, String message) {
//        Log.w("TAG", message);
//        hideKeyboard();
//        if (isLogin) {
//            viewModel2.setIsLoading(false);
//
//            Snackbar.make(binding.cardLogin, message, Snackbar.LENGTH_SHORT).show();
//            startActivity(new Intent(LoginActivity2.this, SQLiteDownloaderActivity.class));
//            finish();
//        } else {
//            viewModel2.setIsLoading(false);
//            Util.showDialog(this, "Login Failed", message);
//        }
//    }

    private void createFile() {
        File Basefile = new File(Constants.PATH);
        if (!Basefile.exists()) {
            Basefile.mkdirs();
        }

        File file = new File(Constants.PATH_IMG);
        if (!file.exists()) {
            file.mkdir();
        }

        File file2 = new File(Constants.PATH_DOWNLOAD);
        if (!file2.exists()) {
            file2.mkdir();
        }

        File file3 = new File(Constants.PATH_DATA_EXPORT);
        if (!file3.exists()) {
            file3.mkdir();
        }
    }

    public static void navigateToLogin(Context context) {
        context.startActivity(new Intent(context, SplashScreen.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.getBooleanPreference(this, "password_save")) {
            binding.checkbox.setChecked(true);
            binding.username.setText(Util.getStringPreference(this, Constants.USER_LOGIN_NAME));
            binding.password.setText(Util.getStringPreference(this, Constants.USER_PASSWORD));
        }
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            permissionNotAllowed = false;
            if (!permissionNotAllowed) {
                createFile();
            }
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            permissionNotAllowed = true;
            TedPermission.with(LoginActivity2.this)
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.CAMERA)
                    .check();
        }
    };

    public void initLocation() {
        final Task<LocationSettingsResponse> locationSetting = requestLocation();
        locationSetting.addOnSuccessListener(this, locationSettingsResponse -> getLastLocation());
        locationSetting.addOnFailureListener(this, e -> {
            if (e instanceof ResolvableApiException) {
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(LoginActivity2.this, REQUEST_CHECK_LOCATION);
                } catch (IntentSender.SendIntentException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mGoogleApiClient.connect();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(this, "Please check your GPS", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case 0x128:
                initLocation();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private Task<LocationSettingsResponse> requestLocation() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location loc = locationResult.getLastLocation();
                    if (loc != null) {
                        location = loc;
                        setLocationToText();
                        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
                    }

                }
            }
        };

        mLocationRequestHigh = new LocationRequest();
        mLocationRequestHigh.setInterval(5000);
        mLocationRequestHigh.setFastestInterval(2500);
        mLocationRequestHigh.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequestHigh);

        SettingsClient client = LocationServices.getSettingsClient(this);
        return client.checkLocationSettings(builder.build());
    }

    private void getLastLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        this.location = location;
                        setLocationToText();
                    }
                })
                .addOnFailureListener(e ->
                        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequestHigh, mLocationCallback, null));
    }

    private void setLocationToText() {
        viewModel2.lat = String.valueOf(location.getLatitude());
        viewModel2.lon = String.valueOf(location.getLongitude());
        Util.putPreference(this, Constants.LAT_CONNECT, String.valueOf(location.getLatitude()));
        Util.putPreference(this, Constants.LON_CONNECT, String.valueOf(location.getLongitude()));
    }

    public void retryConnecting() {
        mIsInResolution = false;
        if (!mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.wtf("CONNECTED", "CALLED");
        initLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        retryConnecting();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Toast.makeText(this, "Google Service not connected\n" + result.toString(), Toast.LENGTH_SHORT).show();
        if (!result.hasResolution()) {
            // Show a localized error dialog.
            GooglePlayServicesUtil.getErrorDialog(
                    result.getErrorCode(), this, 0, dialog -> retryConnecting()).show();
            return;
        }
        // If there is an existing resolution error being displayed or a resolution
        // activity has started before, do nothing and wait for resolution
        // progress to be completed.
        if (mIsInResolution) {
            return;
        }
        mIsInResolution = true;
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            retryConnecting();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IN_RESOLUTION, mIsInResolution);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            stopRequestUpdate();
        }
    }

    private void stopRequestUpdate() {
        if (mFusedLocationProviderClient != null)
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
