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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class CustomerLogin  extends Activity {

    EditText etEmail, etPassword;
    Button btnLogin,btnRegister;
    ProgressBar progressBar;
    DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_login);

        etEmail = (EditText) findViewById(R.id.editTextCusLoginEmail);
        etPassword = (EditText) findViewById(R.id.editTextCusLoginPassword);
        btnLogin = (Button) findViewById(R.id.buttonCusLogin);
        btnRegister = (Button) findViewById(R.id.buttonRegister);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CustomerLogin.this,CustomerRegister.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if(isEmail(email)&&isPassword(password))
                {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(CustomerLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                final String uid = user.getUid();
                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        User customer;
                                        customer = dataSnapshot.child(uid).getValue(User.class);
                                        boolean status = customer.getStatus();
                                        Log.d(TAG, "customer status is " + Boolean.toString(status));
                                        if (status && flag) {
                                            Intent intent = new Intent(CustomerLogin.this, CustomerHome.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            progressBar.setVisibility(View.GONE);
                                            startActivity(intent);
                                        } else {
                                            flag = false;
                                            Toast.makeText(getApplicationContext(), "Approval Pending... Try again Later...", Toast.LENGTH_SHORT).show();
                                            mAuth.signOut();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.w(TAG, "Failed to read value.", databaseError.toException());
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Wrong Credentials",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean isPassword(String password) {
        if(password.trim().length()==0)
        {
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
        if(email.trim().length()==0)
        {
            etEmail.setError("Email cannot be blank");
            etEmail.requestFocus();
            return false;
        }
        return true;
    }
}