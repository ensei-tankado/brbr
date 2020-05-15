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
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class CustomerHome extends Activity {
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    ListView listView;
    Button bookingHistory, btnSignOut;
    ArrayList<String> uids;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_home);
        uids = new ArrayList<>();


        mAuth = FirebaseAuth.getInstance();

        btnSignOut = (Button) findViewById(R.id.customerSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent in = new Intent(CustomerHome.this,MainActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
            }
        });

        bookingHistory = (Button) findViewById(R.id.bookingHistory);

        bookingHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CustomerHome.this,BookingHistory.class);
                startActivity(i);
            }
        });


        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CustomerHome.this, BookRequest.class);
                intent.putExtra("BarberName",listView.getItemAtPosition(position).toString());
                intent.putExtra("UID",uids.get(position));
                startActivity(intent);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("users").orderByChild("role").equalTo("barber");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<String> array = new ArrayList<>();
                String uid;
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    User barber = ds.getValue(User.class);
                    String fullname = barber.getFullname();
                    if(barber.getStatus())
                    {
                        uid = ds.getKey();
                        uids.add(uid);
                        Log.d(TAG, "barber ID is " + uid);
                        array.add(fullname);
                        Log.d(TAG, "barber name is " + fullname);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CustomerHome.this,android.R.layout.simple_list_item_1,array);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

    }
}
