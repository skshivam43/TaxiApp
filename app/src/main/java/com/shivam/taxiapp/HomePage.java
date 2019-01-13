package com.shivam.taxiapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static android.app.PendingIntent.getActivity;

public class HomePage extends AppCompatActivity {
        private EditText source;
        private EditText destination;
        private EditText passengers;
        private EditText journeydate;
        private EditText journeytime;
       // private EditText driverName;
        //private EditText driverPhone;
        private FirebaseDatabase mDatabase;
        private DatabaseReference mDatabaseReference;
        private DatabaseReference driverLocation;
        private FirebaseAuth mAuth;
        private ProgressDialog mProgressDialog;
        private FirebaseUser appuser;
        private Button submitrequest;
        //private Button viewrequest;
        private DatePickerDialog.OnDateSetListener mDateSetListener;
        private TimePickerDialog.OnTimeSetListener mTimeSetListener;
        private TimePickerDialog timepickerdialog;
        private String format;
        private String lat;
        private String lon;
        private double latit;
        private double longit;
        private LatLng pickUpLocation;
        public String customerName;
        public String customerPhone;
        private TextView dn;
        private TextView dp;
        private int weight;



        private static final int REQUEST_CODE = 1000;
        Button setUserLocation, stopSharing, lastLocation;
      //  TextView viewLocation;
        Location mLastLocation;
        FusedLocationProviderClient fusedLocationProviderClient;
        LocationRequest locationRequest;
        LocationCallback locationCallback;

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



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
        Location mLastLocation;
        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        source = (EditText) findViewById(R.id.source);
        destination = (EditText) findViewById(R.id.destination);
        passengers = (EditText) findViewById(R.id.passengers);
        journeydate = (EditText) findViewById(R.id.journeydate);
        journeytime = (EditText) findViewById(R.id.journeytime);
        submitrequest = (Button) findViewById(R.id.submitrequest);
        dn =(TextView) findViewById(R.id.getdriverName);
        dp=(TextView) findViewById(R.id.getdriverPhone);
      //  driverName = (EditText)findViewById(R.id.driverName);
       // driverPhone = (EditText)findViewById(R.id.driverPhone);

        //viewrequest = (Button) findViewById(R.id.viewrequest);




        setUserLocation = (Button) findViewById(R.id.setUserLocation);
        stopSharing = (Button) findViewById(R.id.stopSharing);

       // lastLocation = (Button) findViewById(R.id.lastLocation);
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            buildLocationRequest();
            buildLocationCallBack();

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


            setUserLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ActivityCompat.checkSelfPermission(HomePage.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomePage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        ActivityCompat.requestPermissions(HomePage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                        return;
                    }
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                    setUserLocation.setEnabled(false);
                    stopSharing.setEnabled(true);
                     /*double lat =mLastLocation.getLatitude();
                     double lon =mLastLocation.getLongitude();*/



                }
            });
            stopSharing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ActivityCompat.checkSelfPermission(HomePage.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomePage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        ActivityCompat.requestPermissions(HomePage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

                        return;
                    }
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                    setUserLocation.setEnabled(true);
                    stopSharing.setEnabled(false);

                }
            });

        }
        journeydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        HomePage.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        journeytime.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

         Calendar calendar = Calendar.getInstance();
        int CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
        int CalendarMinute = calendar.get(Calendar.MINUTE);


        timepickerdialog = new TimePickerDialog(HomePage.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        if (hourOfDay == 0) {

                            hourOfDay += 12;

                             format = "AM";
                        }
                        else if (hourOfDay == 12) {

                            format = "PM";

                        }
                        else if (hourOfDay > 12) {

                            hourOfDay -= 12;

                            format = "PM";

                        }
                        else {

                            format = "AM";
                        }


                        journeytime.setText(hourOfDay + ":" + minute + format);
                    }
                }, CalendarHour, CalendarMinute, false);
        timepickerdialog.show();

    }
    });
        /*journeytime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(HomePage.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedHour>12)
                        {
                            selectedHour=selectedHour-12;
                            journeytime.setText( selectedHour + ":" + selectedMinute+" "+"PM");
                        }
                        else if(selectedHour==0)
                        {
                            journeytime.setText( "12" + ":" + selectedMinute+" "+"AM");
                        }
                        else
                        {
                            journeytime.setText( selectedHour + ":" + selectedMinute+" "+"AM");
                        }
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });*/

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                //Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = day + "/" +month+"/"+ year;
                journeydate.setText(date);
            }
        };
        submitrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                addrequest();


            }


        });
       /* viewrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(HomePage.this,ViewRequest.class));

            }
        });*/

    }
    private void addrequest()
    {
        final String source_var =source.getText().toString().trim();
        final String destination_var =destination.getText().toString().trim();
        final String passengers_var =passengers.getText().toString().trim();
        final String journeydate_var =journeydate.getText().toString().trim();
        final String journeytime_var =journeytime.getText().toString().trim();
        mProgressDialog.setMessage("Submitting Request...");
        mProgressDialog.show();

        if(!TextUtils.isEmpty(source_var) && !TextUtils.isEmpty(destination_var) &&
                !TextUtils.isEmpty(passengers_var) && !TextUtils.isEmpty(journeydate_var) )
        {
            appuser = FirebaseAuth.getInstance().getCurrentUser();
            String user_ID = appuser.getUid();
            DatabaseReference request = mDatabaseReference.child("appusers").child(user_ID).child("requests");
            String requestID = request.push().getKey();
            AddRequest request_var = new AddRequest( source_var, destination_var,passengers_var,journeydate_var,journeytime_var);
            request.child(requestID).setValue(request_var);
            mProgressDialog.dismiss();
            DatabaseReference refer = mDatabase.getReference("customerRequests");
            GeoFire geoFire = new GeoFire(refer);
            geoFire.setLocation(user_ID, new GeoLocation(latit, longit));

            submitrequest.setText("Getting your Driver....");
            getClosestDriver();

        }
        else
        {
            mProgressDialog.dismiss();
            Toast.makeText(HomePage.this, "Incorrect Details!", Toast.LENGTH_LONG).show();
        }
    }
    private int radius =1;
    private boolean driverFound =false;
    private String driverFoundID;
    private void getClosestDriver() {
        DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference().child("driversAvailable");
       // DatabaseReference driverWeight = FirebaseDatabase.getInstance().getReference().child("driversAvailable");
        GeoFire geoFire = new GeoFire(driverLocation);
        GeoQuery geoQuery=  geoFire.queryAtLocation(new GeoLocation(latit,longit),radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                if(!driverFound)
                {
                    driverFoundID = key;
                    Log.i("sss",driverFoundID);
                   driverFound =true;

                    DatabaseReference driverWeight = mDatabaseReference.child("appusers").child(driverFoundID);
                    driverWeight.child("weight").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Log.i("weight",(String)snapshot.getValue());
                            //Log.i("sdd",s);
                            //Log.i("customer",customerName);//prints "Do you have data? You'll love Firebase."
                          //   weight = Integer.parseInt(s);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });


                   /* if(!driverFound && weight!=0)
                    {
                        driverFound =true;
                        driverFoundID = key;
                        Log.i("user",driverFoundID);
                        String userID=appuser.getUid();
                        DatabaseReference currentUserDb = mDatabaseReference.child("appusers").child(userID);

                        submitrequest.setText("DriverFound Check View Requests");
                        currentUserDb.child("details").child("name").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                System.out.println(snapshot.getValue());
                                //  customerName = (String)(snapshot.getValue());
                                DatabaseReference driverBusy = mDatabaseReference.child("driversBusy").child(driverFoundID).child("customerName");
                                driverBusy.setValue(snapshot.getValue());
                                //Log.i("customer",customerName);//prints "Do you have data? You'll love Firebase."
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                        currentUserDb.child("details").child("phone").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                System.out.println(snapshot.getValue());
                                customerPhone = (String)snapshot.getValue();
                                DatabaseReference driverBusyPhone = mDatabaseReference.child("driversBusy").child(driverFoundID).child("customerPhone");
                                driverBusyPhone.setValue(snapshot.getValue());

                                // Log.i("customer",customerPhone);//prints "Do you have data? You'll love Firebase."
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                        // driverBusy.setValue(driverFoundID);

                        //mDatabaseReference.child("driversBusy").child(driverFoundID).child("customerPhone").setValue(customerPhone);

                        mDatabaseReference.child("driversAvailable").child(driverFoundID).removeValue();
                        DatabaseReference currentDriverDb = mDatabaseReference.child("appusers").child(driverFoundID);
                        currentDriverDb.child("details").child("name").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                // System.out.println(snapshot.getValue());
                                //  customerName = (String)(snapshot.getValue());
                                //DatabaseReference driverBusy = mDatabaseReference.child("driversBusy").child(driverFoundID).child("customerName");
                                //driverBusy.setValue(snapshot.getValue());
                                dn.setText((String)snapshot.getValue());
                                //Log.i("customer",customerName);//prints "Do you have data? You'll love Firebase."
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                        currentDriverDb.child("details").child("phone").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                System.out.println(snapshot.getValue());
                                //  customerName = (String)(snapshot.getValue());
                                //DatabaseReference driverBusy = mDatabaseReference.child("driversBusy").child(driverFoundID).child("customerName");
                                //driverBusy.setValue(snapshot.getValue());
                                dp.setText((String)snapshot.getValue());
                                //Log.i("customer",customerName);//prints "Do you have data? You'll love Firebase."
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                    }*/
                }

            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if(!driverFound)
                {
                    radius++;


                    getClosestDriver();;
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

    }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                for(Location location: locationResult.getLocations())

                {
                    lat = String.valueOf(location.getLatitude());
                     lon = String.valueOf(location.getLongitude());


                   /* viewLocation.setText(lat
                            +"/"
                            +lon);*/



                    latit = location.getLatitude();
                    longit =location.getLongitude();

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
