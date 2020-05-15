package com.example.brbr;

import android.app.Activity;
import android.content.Intent;
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

public class BookingHistoryDetail extends Activity {

    Button btnCancel;
    DatabaseReference mDatabase, mRef,mDeleteReference;
    String barberFullName, barberPlace, date, time, barberID,status;
    TextView tvFullName,tvPlace,tvDate,tvTime,tvStatus;
    String bookingID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_history_details);
        tvFullName = (TextView) findViewById(R.id.barberName);
        tvPlace = (TextView) findViewById(R.id.barberPlace);
        tvDate = (TextView) findViewById(R.id.requestDate);
        tvTime = (TextView) findViewById(R.id.requestTime);
        tvStatus = (TextView) findViewById(R.id.requestStatus);
        btnCancel = (Button) findViewById(R.id.pending_request_cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteReference = FirebaseDatabase.getInstance().getReference("bookings")
                        .child(bookingID);
                mDeleteReference.removeValue();
                Intent intent = new Intent(BookingHistoryDetail.this, CustomerHome.class);
                startActivity(intent);
            }
        });

        Bundle bundle = getIntent().getExtras();
        bookingID = bundle.getString("bookingID");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRef = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child("bookings").child(bookingID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                status = dataSnapshot.child("status").getValue().toString();
                if(status.equals("confirmed") || status.equals("cancelled"))
                {
                    btnCancel.setVisibility(View.GONE);
                }
                date = dataSnapshot.child("date").getValue().toString();
                time = dataSnapshot.child("time").getValue().toString();

                barberID = dataSnapshot.child("barberID").getValue().toString();
                tvDate.setText(date);
                tvTime.setText(time);
                tvStatus.setText(status);
                mRef.child(barberID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        barberFullName = dataSnapshot.child("fullname").getValue().toString();
                        barberPlace = dataSnapshot.child("place").getValue().toString();
                        tvFullName.setText(barberFullName);
                        tvPlace.setText(barberPlace);
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
