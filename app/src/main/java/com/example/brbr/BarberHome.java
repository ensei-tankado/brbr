package com.example.brbr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class BarberHome extends Activity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    ListView listView;
    String buid;
//    ArrayList<String> uids;
    ArrayList<String> bookingIDs;
    String date;
    String time;
    String userID;
    String bookingStatus;
    String bookingID;
    Intent intent;
    Button btnConfirmed, btnSignOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barber_home);
        listView = (ListView) findViewById(R.id.listViewRequests);

        mAuth = FirebaseAuth.getInstance();

        btnSignOut = (Button) findViewById(R.id.barberSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent in = new Intent(BarberHome.this,MainActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
            }
        });

        btnConfirmed = (Button) findViewById(R.id.bookingConfirmed);
        btnConfirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BarberHome.this,ConfirmedBooking.class);
                startActivity(i);
            }
        });
        bookingIDs = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        buid = user.getUid();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(BarberHome.this,RequestDetails.class);
//                intent.putExtra("userID",uids.get(position));
//                intent.putExtra("date",date);
//                intent.putExtra("time",time);
                bookingID = bookingIDs.get(position);
                mDatabase.child("bookings").child(bookingID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userID = dataSnapshot.child("userID").getValue().toString();
                        time = dataSnapshot.child("time").getValue().toString();
                        date = dataSnapshot.child("date").getValue().toString();
                        bookingStatus = dataSnapshot.child("status").getValue().toString();
                        intent.putExtra("userID", userID);
                        intent.putExtra("date",date);
                        intent.putExtra("time",time);
                        intent.putExtra("bookingID",bookingID);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "Failed to read value.", databaseError.toException());
                    }
                });
//                intent.putExtra("bookingID",bookingID);
//                intent.putExtra("userID",);

            }
        });

        Query query = mDatabase.child("bookings").orderByChild("barberID").equalTo(buid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> array = new ArrayList<>();

                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Booking booking = ds.getValue(Booking.class);
//                    String userID = booking.getUserID();
//                    uids.add(userID);
                    if(booking.getStatus().equals("pending")) {
                        bookingIDs.add(ds.getKey());
                        String singleDate = booking.getDate();
                        String singleTime = booking.getTime();
                        String dateTime = singleDate + " " + singleTime;
                        array.add(dateTime);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(BarberHome.this,android.R.layout.simple_list_item_1,array);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
}
