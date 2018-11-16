package com.shivam.taxiapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity
{

    private EditText name;
    private EditText email_ID;
    private EditText phoneNo;
    private EditText userType;
    private EditText department;
    private EditText password;
    private Button signUpButton;
    private FirebaseUser appuser;
    private FirebaseDatabase mDatabase;
   private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();

        mAuth = FirebaseAuth.getInstance();

        mProgressDialog = new ProgressDialog(this);

        name = (EditText) findViewById(R.id.fullName);
        email_ID = (EditText) findViewById(R.id.email_ID);
        phoneNo = (EditText) findViewById(R.id.phoneNo);
        userType = (EditText) findViewById(R.id.userType);
        department = (EditText) findViewById(R.id.department);
        password = (EditText) findViewById(R.id.password);
        signUpButton = (Button) findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                register();

            }


        });

    }
    private void register()
    {        final String fullName =name.getText().toString().trim();
        final String email = email_ID.getText().toString().trim();
        final String mobile = phoneNo.getText().toString().trim();
        final String type = userType.getText().toString().trim();
        final String dpt = department.getText().toString().trim();
        String pwd = password.getText().toString().trim();

        if(!TextUtils.isEmpty(fullName) && !TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(type) && !TextUtils.isEmpty(dpt) && !TextUtils.isEmpty(pwd))
        {
            mProgressDialog.setMessage("Creating Account...");
            mProgressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {

                                appuser = FirebaseAuth.getInstance().getCurrentUser();


                                    String user_ID = appuser.getUid();
                                    DatabaseReference currentUserDb = mDatabaseReference.child("appusers").child(user_ID).child("details");

                                    Details details = new Details(user_ID,fullName,email,type,mobile,dpt);
                                    currentUserDb.setValue(details);







                                mProgressDialog.dismiss();
                                Toast.makeText(CreateAccountActivity.this, "Account Created Successfully!", Toast.LENGTH_LONG).show();
                                /*Intent intent = new Intent(CreateAccountActivity.this,HomePage.class);
                                startActivity(intent);*/

                            }
                            else
                            {
                                mProgressDialog.dismiss();
                                Toast.makeText(CreateAccountActivity.this, "Incorrect Details!", Toast.LENGTH_LONG).show();
                            }


                        }
                    });
        }
        else
        { mProgressDialog.dismiss();
            Toast.makeText(CreateAccountActivity.this, "Incorrect Details!", Toast.LENGTH_LONG).show();
        }
    }
}



  
