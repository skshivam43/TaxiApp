package com.shivam.taxiapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
{


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private EditText email_ID;
    private EditText password;
    private Button signInButton;
    private Button signUpButton;
   private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         mAuth = FirebaseAuth.getInstance();
         email_ID = (EditText) findViewById(R.id.email_ID);
         password = (EditText) findViewById(R.id.password);
         signInButton = (Button) findViewById(R.id.signInButton);
         signUpButton = (Button) findViewById(R.id.signUpButton);

         signUpButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v)
             {
                    startActivity(new Intent(MainActivity.this,CreateAccountActivity.class));
                    finish();
             }
         });
         mAuthListener=  new FirebaseAuth.AuthStateListener() {
             @Override
             public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
             {
                    mUser = firebaseAuth.getCurrentUser();
                    if(mUser!=null)
                    {
                        Toast.makeText(MainActivity.this, "Signed in", Toast.LENGTH_LONG).show();
                       // Intent intent = new Intent(MainActivity.this,HomePage.class);
                        //startActivity(intent);
                    }

             }
         };
         signInButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v)
             {
                 String email = email_ID.getText().toString() ;
                 String pwd = password.getText().toString();
                 if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd))
                 {
                     login(email,pwd);
                 }
             }
         });

    }

    private void login (String email, String pwd)
    {
         mAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            onAuthSuccess(task.getResult().getUser());
                           // Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_LONG).show();
                            /*Intent intent = new Intent(MainActivity.this,HomePage.class);
                            startActivity(intent);*/


                        } else {
                            Toast.makeText(MainActivity.this, "Wrong Credentials", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void onAuthSuccess(FirebaseUser user) {

        //String username = usernameFromEmail(user.getEmail())
        if (user != null) {
            //Toast.makeText(signinActivity.this, user.getUid(), Toast.LENGTH_SHORT).show();
            ref = FirebaseDatabase.getInstance().getReference().child("appusers").child(user.getUid()).child("details").child("type");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    //for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    // Toast.makeText(signinActivity.this, value, Toast.LENGTH_SHORT).show();
                    if ( value.equals("User")) {

                        Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, HomePage.class);
                        startActivity(intent);
                    } else if (value.equals("Driver")) {

                        Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, HomePage_Driver.class);
                        startActivity(intent);
                    } else if (value.equals("Owner")) {

                        Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, HomePage_Owner.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
      //  mAuth.addAuthStateListener(mAuthListener);
        // Check if user is signed in (non-null) and update UI accordingly.
       //FirebaseUser currentUser = mAuth.getCurrentUser();
       // updateUI(currentUser);
    }




}
