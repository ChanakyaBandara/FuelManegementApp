package com.example.fuelmanegementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.fuelmanegementapp.interfaces.httpDataManager;
import com.example.fuelmanegementapp.models.FuelStation;
import com.example.fuelmanegementapp.services.Backgroundworker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class CustomerViewFuelStations extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, httpDataManager {

    //Map
    private GoogleMap mMap;
    Location lastLocationclnew;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE=1234;
    private static final String TAG ="Mapactivity";
    private Boolean mLocationpermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final float DEAFAULT_ZOOM =8f;
    private List<FuelStation> fuelStationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_fuel_stations);
        getLocationPermission();
    }

    private void getDeviceLoctation(){
        Log.d(TAG,"getDeviceLocation:getting current Location");
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationpermissionGranted){
                Task location=mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener(){
                    public void onComplete(@NonNull Task task){
                        if(task.isSuccessful()){
                            Log.d(TAG,"onComplete:Found Location");
                            Location currentLocation=(Location)task.getResult();
                            lastLocationclnew=currentLocation;
                            assert currentLocation != null;
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEAFAULT_ZOOM);
                            loadStations();
                        }else{
                            Log.d(TAG,"onComplete:current location is null");
                            Toast.makeText(CustomerViewFuelStations.this,"unable to get current location",Toast.LENGTH_SHORT).show();
                        }
                    }


                });
            }

        }catch(SecurityException e){
            Log.e(TAG,"getDeviceLocation:SecurityException: " +e.getMessage());

        }

    }

    private void loadStations() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "load_stations");
        Backgroundworker backgroundworker = new Backgroundworker(this);
        backgroundworker.execute(param);
    }

    private  void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG,"moveCamera:moving the camera to:lat:"+latLng.latitude+",lng:"+latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }

    private void initMap(){
        Log.d(TAG,"initMap:initializingMap");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    private  void getLocationPermission(){
        Log.d(TAG,"getLocationPermission:getting Location Permission");
        String[]permission = {Manifest.permission.ACCESS_FINE_LOCATION};
        //     Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            // if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COURSE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            mLocationpermissionGranted=true;
            initMap();
        }
        else{
            ActivityCompat.requestPermissions(this,permission,LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "OnRequestPermissionResult:called");
        //mLocationpermissionGranted=false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationpermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionResult: permissionFailed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionResult: permission granted");
                    mLocationpermissionGranted = true;
                    initMap();

                }


            }
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        moveCamera(marker.getPosition(), 19);
        Optional<FuelStation> fuelStationOptional = fuelStationList.stream().filter(fuelStation -> marker.getPosition().equals(new LatLng(Double.valueOf(fuelStation.getLat()), Double.valueOf(fuelStation.getLon()))))
                .findFirst();
        if (fuelStationOptional.isPresent()){
            Intent intent = new Intent(this, CustomerFuelStationDetails.class);
            intent.putExtra("FuelStationObj", (Serializable) fuelStationOptional.get());
            this.startActivity(intent);
        }

        return false;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this,"Your Current Location",Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onMapReady:map is ready");
        mMap = googleMap;
        if(mLocationpermissionGranted){
            getDeviceLoctation();
            mMap.setMyLocationEnabled(true);
        }
    }

    private Marker setMarker(String title, double lat, double lan){
        LatLng position = new LatLng(lat, lan);
        MarkerOptions markerOptions = new MarkerOptions()
                .title(title)
                .position(position);
        Marker marker = mMap.addMarker(markerOptions);
//        moveCamera(position,16);
        mMap.setOnMarkerClickListener(this);
        return marker;
    }

    @Override
    public void retrieveData(String type, Optional<String> retrievedData) {
        fuelStationList.clear();
        if (retrievedData.isPresent()){
            FuelStation[] fuelStations = new Gson().fromJson(retrievedData.get(), FuelStation[].class);
            if (fuelStations.length==0) {
                Toast.makeText(this, "No Records Available !", Toast.LENGTH_SHORT).show();
            }
            for (FuelStation fuelStation : fuelStations) {
                setMarker(fuelStation.getName(), Double.valueOf(fuelStation.getLat()), Double.valueOf(fuelStation.getLon()));
                fuelStationList.add(fuelStation);
            }

        }
    }

//    public void goToMap(View view) {
//        Uri googleMapsUri = Uri.parse("google.navigation:q="+emergencie.getElong()+","+emergencie.getElat());
//        Intent mapIntent = new Intent(Intent.ACTION_VIEW,googleMapsUri);
//        mapIntent.setPackage("com.google.android.apps.maps");
//        startActivity(mapIntent);
//    }
}