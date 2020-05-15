package com.example.brbr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import static android.content.ContentValues.TAG;

public class BookRequest extends Activity {
    TextView textView;
    EditText etBookingDate, etBookingTime;
    Button buttonBookingRequest;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    String barberID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_request);
        textView = (TextView) findViewById(R.id.barberName);
        etBookingDate = (EditText) findViewById(R.id.bookingDate);
        etBookingTime = (EditText) findViewById(R.id.bookingTime);
        buttonBookingRequest = (Button) findViewById(R.id.buttonBookRequest);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("bookings");
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            String barberName = bundle.getString("BarberName");
            barberID = bundle.getString("UID");
            textView.setText(barberName);
        }
        buttonBookingRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                String uid = user.getUid();
                final String date = etBookingDate.getText().toString().trim();
                final String time = etBookingTime.getText().toString().trim();
                Log.d(TAG,"Date is "+date);
                Log.d(TAG,"Time is "+time);
                if(isDate(date) && isTime(time))
                {
                    writeNewBooking(barberID,uid,date,time);
                }

            }
        });
    }

    private boolean isTime(String time) {
        boolean flag = true;
        if(time.length()!=5)
            flag = false;
        else
        {
            if(time.charAt(2)==':' && Character.isDigit(time.charAt(0)) && Character.isDigit(time.charAt(1)) && Character.isDigit(time.charAt(3)) && Character.isDigit(time.charAt(4)))
                flag = true;
            else
                flag = false;
        }


        if(!flag)
        {
            etBookingTime.setError("Wrong Time Format");
            etBookingTime.requestFocus();
        }
        return flag;
    }

    private boolean isDate(String date) {

        boolean flag = true;
        if(date.length()!=10)
            flag = false;
        else
        {
            if(date.charAt(2)=='/' && date.charAt(5)=='/' && Character.isDigit(date.charAt(0)) && Character.isDigit(date.charAt(1)) && Character.isDigit(date.charAt(3)) && Character.isDigit(date.charAt(4)) && Character.isDigit(date.charAt(6)) && Character.isDigit(date.charAt(7)) && Character.isDigit(date.charAt(8)) && Character.isDigit(date.charAt(9)))
                flag = true;
            else
                flag = false;
        }

        if(!flag)
        {
            etBookingDate.setError("Wrong Date Format");
            etBookingDate.requestFocus();
        }
        return flag;

    }

    private void writeNewBooking(String barberID, String userID, String date, String time)
    {
        long unique_id= new Date().getTime();
        Booking booking = new Booking(barberID,userID,date,time);
        mDatabase.child(Long.toString(unique_id)).setValue(booking);
        Log.d(TAG,Long.toString(unique_id));
        Log.d(TAG,"Date "+date);
        Log.d(TAG,"Time "+time);
        Intent intent = new Intent(BookRequest.this, CustomerHome.class);
        startActivity(intent);
    }
}
