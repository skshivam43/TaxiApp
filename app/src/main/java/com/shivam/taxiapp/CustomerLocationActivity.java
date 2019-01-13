package com.shivam.taxiapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerLocationActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1000;
    Button startLocation, stopLocation, lastLocation;
    TextView viewLocation;
    Location mLastLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_location);
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();

        mAuth = FirebaseAuth.getInstance();

        startLocation = (Button) findViewById(R.id.customerStart);
        stopLocation = (Button) findViewById(R.id.customerStop);
        viewLocation = (TextView) findViewById(R.id.customerLocation);






        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            buildLocationRequest();
            buildLocationCallBack();

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


            startLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ActivityCompat.checkSelfPermission(CustomerLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CustomerLocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        ActivityCompat.requestPermissions(CustomerLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                        return;
                    }
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                    startLocation.setEnabled(!startLocation.isEnabled());
                    stopLocation.setEnabled(!stopLocation.isEnabled());
                     /*double lat =mLastLocation.getLatitude();
                     double lon =mLastLocation.getLongitude();*/



                }
            });
            stopLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ActivityCompat.checkSelfPermission(CustomerLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CustomerLocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        ActivityCompat.requestPermissions(CustomerLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

                        return;
                    }
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                    startLocation.setEnabled(!startLocation.isEnabled());
                    stopLocation.setEnabled(!stopLocation.isEnabled());

                }
            });

        }
    }


    private void buildLocationCallBack() {
        locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                for(Location location: locationResult.getLocations())

                {
                    String lat = String.valueOf(location.getLatitude());
                    String lon = String.valueOf(location.getLongitude());


                    viewLocation.setText(lat
                            +"/"
                            +lon);






                    String userID = mAuth.getCurrentUser().getUid();

                    DatabaseReference ref = mDatabase.getReference("customerRequests");
                    GeoFire geoFire = new GeoFire(ref);
                    geoFire.setLocation(userID, new GeoLocation(location.getLatitude(), location.getLongitude()));


                    // geoFire.setLocation(userID, new GeoLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude()));





                }






              /*  {
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Location location = (Location) locationResult.getLocations();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("driversAvailable");
                    GeoFire geoFire = new GeoFire(ref);
                    geoFire.setLocation(userID, new GeoLocation(location.getLatitude(), location.getLongitude()));
                }*/





            }
        };


    }



    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
    }

}
