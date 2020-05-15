package com.example.brbr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class CustomerRoute extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_route);

        Button customerRegister = (Button) findViewById(R.id.customer_register);
        Button customerLogin = (Button) findViewById(R.id.customer_login);

        customerRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerRoute.this, CustomerRegister.class);
                startActivity(intent);
            }
        });

        customerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerRoute.this, CustomerLogin.class);
                startActivity(intent);
            }
        });
    }
}
