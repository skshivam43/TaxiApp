package com.shivam.taxiapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomePage_Owner extends AppCompatActivity {
    private EditText driver_name;
    private EditText driver_phone;
    private EditText car_no;
    private EditText car_name;
    private EditText seats;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    private FirebaseUser appuser;
    private Button add_driver;
    private Button add_car;
    private Button show_cars;
    private Button show_drivers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page__owner);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();

        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        driver_name = (EditText) findViewById(R.id.driver_name);
        driver_phone = (EditText) findViewById(R.id.driver_mobile);
        car_no = (EditText) findViewById(R.id.car_no);
        car_name = (EditText) findViewById(R.id.car_name);
        seats = (EditText) findViewById(R.id.seats);
        add_car = (Button) findViewById(R.id.add_car);
        add_driver = (Button) findViewById(R.id.add_driver);
        show_cars = (Button) findViewById(R.id.show_cars);
        show_drivers = (Button) findViewById(R.id.show_drivers);
        add_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                addDriver();

            }


        });
        add_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                addCar();

            }


        });
        show_cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(HomePage_Owner.this,Owner_Cars.class));

            }
        });



    }
    private void addDriver()
    {
        final String dname =driver_name.getText().toString().trim();
        final String dmobile =driver_phone.getText().toString().trim();

        mProgressDialog.setMessage("Adding Driver...");
        mProgressDialog.show();

        if(!TextUtils.isEmpty(dname) && !TextUtils.isEmpty(dmobile))
        {
            appuser = FirebaseAuth.getInstance().getCurrentUser();
            String user_ID = appuser.getUid();
            DatabaseReference adddriver = mDatabaseReference.child("appusers").child(user_ID).child("drivers");
            String driverID = adddriver.push().getKey();
            adddriver.child(driverID).child("mobile").setValue(dmobile);
            adddriver.child(driverID).child("name").setValue(dname);
            mProgressDialog.dismiss();
            Toast.makeText(HomePage_Owner.this, "Driver Added!", Toast.LENGTH_LONG).show();
        }
        else
        {
            mProgressDialog.dismiss();
            Toast.makeText(HomePage_Owner.this, "Incorrect Details!", Toast.LENGTH_LONG).show();
        }
    }
    private void addCar()
    {
        final String cname =car_name.getText().toString().trim();
        final String cno =car_no.getText().toString().trim();
        final String st = seats.getText().toString().trim();

        mProgressDialog.setMessage("Adding Driver...");
        mProgressDialog.show();

        if(!TextUtils.isEmpty(cname) && !TextUtils.isEmpty(cno)&& !TextUtils.isEmpty(st))
        {
            appuser = FirebaseAuth.getInstance().getCurrentUser();
            String user_ID = appuser.getUid();
            DatabaseReference addcar = mDatabaseReference.child("appusers").child(user_ID).child("cars");
            String carID = addcar.push().getKey();
            addcar.child(carID).child("number").setValue(cno);
            addcar.child(carID).child("name").setValue(cname);
            addcar.child(carID).child("seats").setValue(st);
            mProgressDialog.dismiss();
            Toast.makeText(HomePage_Owner.this, "Car Added!", Toast.LENGTH_LONG).show();
        }
        else
        {
            mProgressDialog.dismiss();
            Toast.makeText(HomePage_Owner.this, "Incorrect Details!", Toast.LENGTH_LONG).show();
        }
    }
}
