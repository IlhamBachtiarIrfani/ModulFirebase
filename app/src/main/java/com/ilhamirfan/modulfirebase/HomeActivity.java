package com.ilhamirfan.modulfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextView userDataText;
    private Button logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        userDataText = findViewById(R.id.userDataText);
        logoutButton = findViewById(R.id.LogoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            userDataText.setText(currentUser.getUid() + "\n" + currentUser.getPhoneNumber());
        } else {
            finish();
        }
    }

    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        Intent mainActivity = new Intent(HomeActivity.this, MainActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainActivity);
    }
}