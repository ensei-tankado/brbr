package com.example.brbr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class BarberRoute extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barber_route);
        Button barberRegister = (Button) findViewById(R.id.barber_register);
        Button barberLogin = (Button) findViewById(R.id.barber_login);

        barberRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarberRoute.this,BarberRegister.class);
                startActivity(intent);
            }
        });

        barberLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarberRoute.this,BarberLogin.class);
                startActivity(intent);
            }
        });

    }
}
