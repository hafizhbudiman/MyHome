package com.kecepret.myhome;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String email = extras.getString("email");
            String fullname = extras.getString("fullname");

            TextInputLayout tilFullname = findViewById(R.id.til_fullname);
            tilFullname.setHint(fullname);
            tilFullname.setFocusable(false);

            TextInputLayout tilEmail = findViewById(R.id.til_email);
            tilEmail.setHint(email);
            tilEmail.setFocusable(false);
        }
    }
}
