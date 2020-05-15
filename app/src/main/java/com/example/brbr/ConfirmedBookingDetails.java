package com.example.brbr;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class ConfirmedBookingDetails extends Activity {
    DatabaseReference mDatabase, mRef,mDeleteReference;
    String customerFullName, customerPlace, date, time, userID,mobile;
    TextView tvFullName,tvPlace,tvDate,tvTime,tvMobile;
    String bookingID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmed_booking_details);
        tvFullName = (TextView) findViewById(R.id.customerName);
        tvPlace = (TextView) findViewById(R.id.customerPlace);
        tvDate = (TextView) findViewById(R.id.confirmedDate);
        tvTime = (TextView) findViewById(R.id.confirmedTime);
        tvMobile = (TextView) findViewById(R.id.customerMobile);
        Bundle bundle = getIntent().getExtras();
        bookingID = bundle.getString("bookingID");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRef = FirebaseDatabase.getInstance().getReference("users");


        mDatabase.child("bookings").child(bookingID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                date = dataSnapshot.child("date").getValue().toString();
                time = dataSnapshot.child("time").getValue().toString();
                userID = dataSnapshot.child("userID").getValue().toString();
                tvDate.setText(date);
                tvTime.setText(time);
                mRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        customerFullName = dataSnapshot.child("fullname").getValue().toString();
                        customerPlace = dataSnapshot.child("place").getValue().toString();
                        mobile = dataSnapshot.child("mobile").getValue().toString();
                        tvFullName.setText(customerFullName);
                        tvPlace.setText(customerPlace);
                        tvMobile.setText(mobile);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "Failed to read value.", databaseError.toException());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
}
