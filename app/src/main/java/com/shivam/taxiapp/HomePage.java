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

public class HomePage extends AppCompatActivity {
        private EditText source;
        private EditText destination;
        private EditText passengers;
        private EditText journeydate;
        private FirebaseDatabase mDatabase;
        private DatabaseReference mDatabaseReference;
        private FirebaseAuth mAuth;
        private ProgressDialog mProgressDialog;
        private FirebaseUser appuser;
        private Button submitrequest;
        private Button viewrequest;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();

        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        source = (EditText) findViewById(R.id.source);
        destination = (EditText) findViewById(R.id.destination);
        passengers = (EditText) findViewById(R.id.passengers);
        journeydate = (EditText) findViewById(R.id.journeydate);
        submitrequest = (Button) findViewById(R.id.submitrequest);
        viewrequest = (Button) findViewById(R.id.viewrequest);
        submitrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                addrequest();

            }


        });
        viewrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(HomePage.this,ViewRequest.class));
                
            }
        });

    }
    private void addrequest()
    {
        final String source_var =source.getText().toString().trim();
        final String destination_var =destination.getText().toString().trim();
        final String passengers_var =passengers.getText().toString().trim();
        final String journeydate_var =journeydate.getText().toString().trim();
        mProgressDialog.setMessage("Submitting Request...");
        mProgressDialog.show();

        if(!TextUtils.isEmpty(source_var) && !TextUtils.isEmpty(destination_var) &&
                !TextUtils.isEmpty(passengers_var) && !TextUtils.isEmpty(journeydate_var) )
        {
            appuser = FirebaseAuth.getInstance().getCurrentUser();
            String user_ID = appuser.getUid();
            DatabaseReference request = mDatabaseReference.child("appusers").child(user_ID).child("requests");
            String requestID = request.push().getKey();
            AddRequest request_var = new AddRequest( source_var, destination_var,passengers_var,journeydate_var);
            request.child(requestID).setValue(request_var);
            mProgressDialog.dismiss();
            Toast.makeText(HomePage.this, "Request Sent!", Toast.LENGTH_LONG).show();
        }
        else
        {
            mProgressDialog.dismiss();
            Toast.makeText(HomePage.this, "Incorrect Details!", Toast.LENGTH_LONG).show();
        }
    }
}
