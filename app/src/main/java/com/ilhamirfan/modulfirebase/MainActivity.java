package com.ilhamirfan.modulfirebase;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText phoneNumberInput;
    private Button phoneSubmitBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        phoneNumberInput = findViewById(R.id.PhoneInput);
        phoneSubmitBtn = findViewById(R.id.PhoneSubmitBtn);

        phoneSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = phoneNumberInput.getText().toString();

                Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
                loginActivity.putExtra("PhoneNumber", phoneNumber);
                startActivity(loginActivity);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent homeActivity = new Intent(MainActivity.this, HomeActivity.class);
            homeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeActivity);
        }
    }
}