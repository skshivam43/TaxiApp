package com.shivam.taxiapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Owner_Cars extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mProgressDialog;
    private FirebaseUser appuser;
    private String userID;
    private ListView carno_lv;
    private ListView carname_lv;
    private ListView seats_lv;
    private String cno;
    private String cname;
    private String cseats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner__cars);

        carno_lv = (ListView) findViewById(R.id.carno_listView);
        carname_lv = (ListView) findViewById(R.id.carname_listView);
        seats_lv = (ListView) findViewById(R.id.carseats_listView);
       // passengers_lv=(ListView)findViewById(R.id.requestPassengers);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();

        mAuth = FirebaseAuth.getInstance();
        appuser = FirebaseAuth.getInstance().getCurrentUser();
        userID = appuser.getUid();
        final ArrayList<String> carno = new ArrayList<String>();
        final ArrayAdapter<String> carno_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,carno);
        final ArrayList<String> carname = new ArrayList<String>();
        final ArrayAdapter<String> carname_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,carname);
        final ArrayList<String> seats = new ArrayList<String>();
        final ArrayAdapter<String> seats_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,seats);
     //   final ArrayList<String> passengers_req = new ArrayList<String>();
       // final ArrayAdapter<String> passengers_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,passengers_req);
        //  mProgressDialog = new ProgressDialog(this);
        // Log.i("lmn",userID);
        // mUsernames.add("Shivam");
      /*  AddRequest r1 = new AddRequest("a","b","4","14/11/2018");
        AddRequest r2 = new AddRequest("c","d","5","15/11/2018");
        src.add(r1);
        src.add(r2);
        mListView.setAdapter(adapter);*/
        mDatabaseReference.child("appusers").child(userID).child("drivers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {

                    cno = (String) postSnapshot.child("number").getValue();
                    cname = (String) postSnapshot.child("name").getValue();
                    cseats = (String) postSnapshot.child("seats").getValue();
                    carno.add(cno);
                    carname.add(cname);
                    seats.add(cseats);
                    //Log.i("jkk", user.getSource());

                }
                carno_lv.setAdapter(carno_adapter);
                carname_lv.setAdapter(carname_adapter);
                seats_lv.setAdapter(seats_adapter);
                //passengers_lv.setAdapter(passengers_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
