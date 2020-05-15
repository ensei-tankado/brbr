package com.example.brbr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.shapes.OvalShape;
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

public class BookingHistory extends Activity {

    ListView listView;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    ArrayList<String> array;
    ArrayList<String> bookingIDs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_history);
        listView = (ListView) findViewById(R.id.listViewHistory);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        Log.d(TAG, "Logged in customer is "+userID);
        mDatabase = FirebaseDatabase.getInstance().getReference("bookings");
        array = new ArrayList<>();
        bookingIDs = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BookingHistory.this, BookingHistoryDetail.class);
                intent.putExtra("bookingID",bookingIDs.get(position));
                startActivity(intent);
            }
        });

        mDatabase.orderByChild("userID").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String date = ds.child("date").getValue().toString();
                    String time = ds.child("time").getValue().toString();
                    String status = ds.child("status").getValue().toString();
                    String bookingID = ds.getKey();
                    bookingIDs.add(bookingID);
                    String listItem = date+" "+time+" "+status;
                    array.add(listItem);
                    Log.d(TAG, "Booking history "+date+" "+time+" "+status+" "+bookingID);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(BookingHistory.this,android.R.layout.simple_list_item_1,array);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
}
