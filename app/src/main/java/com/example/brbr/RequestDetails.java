package com.example.brbr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class RequestDetails extends Activity {

    TextView tvFullname, tvDate, tvTime,tvPlace;
    Button btnConfirm, btnCancel;
    DatabaseReference mDatabase;
    String userFullName, userPlace, bookingID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_details);
        tvFullname = (TextView) findViewById(R.id.customerName);
        tvPlace = (TextView) findViewById(R.id.customerPlace);
        tvDate = (TextView) findViewById(R.id.requestDate);
        tvTime = (TextView) findViewById(R.id.requestTime);
        btnConfirm = (Button) findViewById(R.id.request_confirm);
        btnCancel = (Button) findViewById(R.id.request_cancel);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        Bundle bundle = getIntent().getExtras();
        String userID = bundle.getString("userID");
        bookingID = bundle.getString("bookingID");
        String date = bundle.getString("date");
        String time = bundle.getString("time");
        Query query = mDatabase.child("users").child(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userFullName = dataSnapshot.child("fullname").getValue().toString();
                userPlace = dataSnapshot.child("place").getValue().toString();
                Log.d(TAG, "user full name = "+dataSnapshot.child("fullname").getValue().toString());
                Log.d(TAG, "user place = "+dataSnapshot.child("place").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
        tvDate.setText(date);
        tvTime.setText(time);
        tvFullname.setText(userFullName);
        tvPlace.setText(userPlace);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("bookings").child(bookingID).child("status").setValue("confirmed");
                Toast.makeText(getApplicationContext(),"Booking Confirmed...",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RequestDetails.this, BarberHome.class);
                startActivity(intent);
                Log.d(TAG, "testing");
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("bookings").child(bookingID).child("status").setValue("cancelled");
                Toast.makeText(getApplicationContext(),"Booking Cancelled...",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RequestDetails.this, BarberHome.class);
                startActivity(intent);
            }
        });
    }
}
