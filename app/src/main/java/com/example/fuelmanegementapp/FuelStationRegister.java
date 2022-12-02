package com.example.fuelmanegementapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.services.BackgroundWorker;
import com.example.fuelmanegementapp.util.Constants;
import com.example.fuelmanegementapp.util.WorkaroundMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Optional;

public class FuelStationRegister extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, httpDataManager {


    private ScrollView mScrollView;

    SupportMapFragment supportMapFragment;
    private GoogleMap mMap;
    Location lastLocationclnew;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final String TAG = "FuelStationRegister";
    private Boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final float DEAFAULT_ZOOM = 15f;

    private String etxtLat = "", etxtLan = "";
    private EditText txtStationName, txtStationEmail, txtStationRegNo, txtStationPhone, txtStationAddress, txtStationCity, txtStationPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_station_register);


        txtStationName = findViewById(R.id.txtStationName);
        txtStationEmail = findViewById(R.id.txtStationEmail);
        txtStationRegNo = findViewById(R.id.txtStationRegNo);
        txtStationPhone = findViewById(R.id.txtStationPhone);
        txtStationAddress = findViewById(R.id.txtStationAddress);
        txtStationCity = findViewById(R.id.txtStationCity);
        txtStationPassword = findViewById(R.id.txtStationPassword);

        getLocationPermission();
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map_register);
        mScrollView = findViewById(R.id.scroll_view_register); //parent scrollview in xml, give your scrollview id value
    }

    private void getDeviceLoctation() {
        Log.d(TAG, "getDeviceLocation:getting current Location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete:Found Location");
                            Location currentLocation = (Location) task.getResult();
                            lastLocationclnew = currentLocation;
                            assert currentLocation != null;
                            double lat = currentLocation.getLatitude();
                            double lan = currentLocation.getLongitude();
                            moveCamera(new LatLng(lat, lan),
                                    DEAFAULT_ZOOM);
                            etxtLat = String.valueOf(lat);
                            etxtLan = String.valueOf(lan);

                        } else {
                            Log.d(TAG, "onComplete:current location is null");
                            Toast.makeText(FuelStationRegister.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }


                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation:SecurityException: " + e.getMessage());

        }

    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera:moving the camera to:lat:" + latLng.latitude + ",lng:" + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initMap() {
        Log.d(TAG, "initMap:initializingMap");
//        final SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
//        assert mapFragment != null;
//        mapFragment.getMapAsync(EnterPark.this);


        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //.findFragmentById(R.id.google_map);
        //assert mapFragment != null;
        //mapFragment.getMapAsync(this);

        // check if we have got the googleMap already
        if (mMap == null) {
            SupportMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map_register);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.getUiSettings().setZoomControlsEnabled(true);

                    if (mLocationPermissionGranted) {
                        getDeviceLoctation();
                        if (ActivityCompat.checkSelfPermission(FuelStationRegister.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FuelStationRegister.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mMap.setMyLocationEnabled(true);
                        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                            @Override
                            public void onCameraIdle() {
                                //get latlng at the center by calling
                                LatLng midLatLng = mMap.getCameraPosition().target;
                                etxtLat = String.valueOf(midLatLng.latitude);
                                etxtLan = String.valueOf(midLatLng.longitude);
                            }
                        });
                    }

                    mScrollView = findViewById(R.id.scroll_view_register); //parent scrollview in xml, give your scrollview id value
                    ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map_register))
                            .setListener(new WorkaroundMapFragment.OnTouchListener() {
                                @Override
                                public void onTouch() {
                                    mScrollView.requestDisallowInterceptTouchEvent(true);
                                }
                            });
                }
            });
        }

    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission:getting Location Permission");
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION};
        //     Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COURSE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            mLocationPermissionGranted = true;
            initMap();
        } else {
            ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "OnRequestPermissionResult:called");
        //mLocationpermissionGranted=false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionResult: permissionFailed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionResult: permission granted");
                    mLocationPermissionGranted = true;
                    initMap();
                }
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    public void registerStation(View view) {
        String stationName = txtStationName.getText().toString();
        String stationEmail = txtStationEmail.getText().toString();
        String stationRegNo = txtStationRegNo.getText().toString();
        String stationPhone = txtStationPhone.getText().toString();
        String stationAddress = txtStationAddress.getText().toString();
        String stationCity = txtStationCity.getText().toString();
        String stationPassword = txtStationPassword.getText().toString();

        if (!(TextUtils.isEmpty(stationName) && TextUtils.isEmpty(stationEmail) && TextUtils.isEmpty(stationRegNo) && TextUtils.isEmpty(stationPhone) && TextUtils.isEmpty(stationAddress) && TextUtils.isEmpty(stationCity) && TextUtils.isEmpty(stationPassword))) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("type", Constants.ADD_FUEL_STATION);
            param.put("name", stationName);
            param.put("email", stationEmail);
            param.put("reg_no", stationRegNo);
            param.put("phone", stationPhone);
            param.put("address", stationAddress);
            param.put("city", stationCity);
            param.put("lat", etxtLat);
            param.put("lon", etxtLan);
            param.put("Password", stationPassword);
            BackgroundWorker backgroundworker = new BackgroundWorker(FuelStationRegister.this);
            backgroundworker.execute(param);

        } else {
            Toast.makeText(this, "Empty field not allowed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        if (retrievedData.isPresent()) {
            try {
                JSONObject jsonObj = new JSONObject(retrievedData.get());
                String status = jsonObj.getString("Status");
                if (status.equals("1")) {
                    String LID = jsonObj.getString("LID");
                    Intent intent = new Intent(this, FuelStationDash.class);
                    intent.putExtra("LID", LID);
                    this.startActivity(intent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("testingerror", e.toString());
            }
        }
    }
}