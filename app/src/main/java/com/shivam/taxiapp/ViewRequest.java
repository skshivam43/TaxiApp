package com.shivam.taxiapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewRequest extends AppCompatActivity {


    private List<AddRequest> req;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mProgressDialog;
    private FirebaseUser appuser;
    private String userID;
    private ListView source_lv;
    private ListView date_lv;
    private ListView destination_lv;
    private ListView passengers_lv;
    private AddRequest user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);

        source_lv = (ListView) findViewById(R.id.requestSource);
        date_lv = (ListView) findViewById(R.id.requestDate);
        destination_lv = (ListView) findViewById(R.id.requestDestination);
        passengers_lv=(ListView)findViewById(R.id.requestPassengers);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();

        mAuth = FirebaseAuth.getInstance();
        appuser = FirebaseAuth.getInstance().getCurrentUser();
        userID = appuser.getUid();
       final ArrayList<String> source_req = new ArrayList<String>();
        final ArrayAdapter<String> source_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,source_req);
        final ArrayList<String> date_req = new ArrayList<String>();
        final ArrayAdapter<String> date_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,date_req);
        final ArrayList<String> destination_req = new ArrayList<String>();
        final ArrayAdapter<String> destination_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,destination_req);
        final ArrayList<String> passengers_req = new ArrayList<String>();
        final ArrayAdapter<String> passengers_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,passengers_req);
      //  mProgressDialog = new ProgressDialog(this);
        // Log.i("lmn",userID);
        // mUsernames.add("Shivam");
      /*  AddRequest r1 = new AddRequest("a","b","4","14/11/2018");
        AddRequest r2 = new AddRequest("c","d","5","15/11/2018");
        src.add(r1);
        src.add(r2);
        mListView.setAdapter(adapter);*/
        mDatabaseReference.child("appusers").child(userID).child("requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {

                    user = postSnapshot.getValue(AddRequest.class);
                    source_req.add(user.getSource());
                    date_req.add(user.getJourneydate());
                    destination_req.add(user.getDestination());
                    passengers_req.add(user.getPassengers());
                    Log.i("jkk", user.getSource());

                }
                source_lv.setAdapter(source_adapter);
                date_lv.setAdapter(date_adapter);
                destination_lv.setAdapter(destination_adapter);
                passengers_lv.setAdapter(passengers_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }






    /*
    private void showRequest(DataSnapshot dataSnapshot)
    {
        for(DataSnapshot ds : dataSnapshot.getChildren())
        {
            for(DataSnapshot ds1 : ds.getChildren())
            {
                AddRequest req = new AddRequest();
                req.setRequestID(ds1.getValue(AddRequest.class).getRequestID());
                req.setSource(ds1.getValue(AddRequest.class).getSource());
                req.setDestination(ds1.getValue(AddRequest.class).getDestination());
                req.setPassengers(ds1.getValue(AddRequest.class).getPassengers());
                req.setJourneydate(ds1.getValue(AddRequest.class).getJourneydate());
                Log.i("jkk", "showData: name: " + req.getSource());
                Log.i("jkk", "showData: email: " + req.getDestination());
                Log.i("jkk", "showData: phone_num: " + req.getPassengers());

                ArrayList<String> array  = new ArrayList<>();
                array.add(req.getSource());
                array.add(req.getDestination());
                //array.add(req.getPassengers());
               // array.add(req.getJourneydate());
               ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);
               mListView.setAdapter(adapter);
            }
        }
    }*/

}
