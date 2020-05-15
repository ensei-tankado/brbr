package com.example.brbr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class CustomerRegister extends Activity {

    EditText etEmail, etUserName, etFullName, etMobile, etPassword, etConfirmPassword, etPlace;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_register);
        etEmail = (EditText) findViewById(R.id.editTextCusEmail);
        etUserName = (EditText) findViewById(R.id.editTextCusUserName);
        etFullName = (EditText) findViewById(R.id.editTextCusFullName);
        etMobile = (EditText) findViewById(R.id.editTextCusMobile);
        etPassword = (EditText) findViewById(R.id.editTextCusPassword);
        etConfirmPassword = (EditText) findViewById(R.id.editTextCusConfirmPassword);
        etPlace = (EditText) findViewById(R.id.editTextCusPlace);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        Button button = (Button) findViewById(R.id.buttonCusRegister);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void writeNewUser(String username, String fullname, String mobile, String uid, String place)
    {
        User user = new User(username,fullname,mobile,true,"customer", place);
        mDatabase.child(uid).setValue(user);
    }

    private void registerUser()
    {
        String email = etEmail.getText().toString().trim();
        final String userName = etUserName.getText().toString().trim();
        final String fullName = etFullName.getText().toString().trim();
        final String mobile = etMobile.getText().toString().trim();
        final String place = etPlace.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        if(password.equals(confirmPassword))
        {
            if (isEmail(email)  && isFullName(fullName) && isUserName(userName) && isMobile(mobile) && isPlace(place) && isPassword(password) && isConfirmPassword(confirmPassword))
            {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful())
                        {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            Log.d(TAG,"userID : "+uid);
                            writeNewUser(userName,fullName,mobile,uid,place);
                            Toast.makeText(getApplicationContext(),"Customer Registration Successful...",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(CustomerRegister.this, CustomerLogin.class);
                            startActivity(intent);
//                        mAuth.
//                        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisable(true).build();
//                        user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if(task.isSuccessful())
//                                {
//                                    Toast.makeText(getApplicationContext(),"Barber Registration Successful...",Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        });

                        }
                    }
                });
            }

        }
    }

    private boolean isPassword(String password) {
        if (password.trim().length() == 0) {
            etPassword.setError("Password cannot be blank");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isEmail(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            etEmail.requestFocus();
            return false;
        }
        return true;
    }


    private boolean isPlace(String place) {
        if (place.trim().length() == 0) {
            etPlace.setError("This field cannot be blank");
            etPlace.requestFocus();
            return false;
        }
        return true;
    }


    private boolean isMobile(String mobile) {
        if (!Patterns.PHONE.matcher(mobile).matches()) {
            etMobile.setError("Please enter a valid email");
            etMobile.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isFullName (String fullName){
        if(fullName.trim().length()==0)
        {
            etFullName.setError("This field cannot be blank");
            etFullName.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isUserName (String userName){
        if(userName.trim().length()==0)
        {
            etUserName.setError("This field cannot be blank");
            etUserName.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isConfirmPassword (String confirmPassword){
        if(confirmPassword.trim().length()==0)
        {
            etConfirmPassword.setError("This field cannot be blank");
            etConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }
}
