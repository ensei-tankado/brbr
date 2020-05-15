package com.example.brbr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ConfirmedBooking extends Activity {
    ArrayList<String> array;
    ArrayList<String> bookingIDs;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    String barberID;
    ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmed_booking);
        array = new ArrayList<>();
        bookingIDs = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listViewConfirmedRequests);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ConfirmedBooking.this, ConfirmedBookingDetails.class);
                intent.putExtra("bookingID",bookingIDs.get(position));
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        barberID = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("bookings");
        mDatabase.orderByChild("barberID").equalTo(barberID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    if(ds.child("status").getValue().toString().equals("confirmed"))
                    {
                        String listItem = ds.child("date").getValue().toString() + " " + ds.child("time").getValue().toString();
                        String bookingID = ds.getKey();
                        bookingIDs.add(bookingID);
                        array.add(listItem);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ConfirmedBooking.this,android.R.layout.simple_list_item_1,array);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
}
