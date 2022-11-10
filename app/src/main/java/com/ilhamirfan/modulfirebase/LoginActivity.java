package com.ilhamirfan.modulfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private TextView explanationText;
    private Button verifyButton;
    private EditText verifyCodeInput;

    private FirebaseAuth mAuth;
    String verificationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String phoneNumber = getIntent().getStringExtra("PhoneNumber");

        explanationText = findViewById(R.id.ExplanationText);
        verifyCodeInput = findViewById(R.id.VerifyCodeInput);
        verifyButton = findViewById(R.id.SubmitBtn);


        explanationText.setText("Code verifikasi telah dikirimkan pada nomor " + phoneNumber + ". Periksa dan masukkan kode verifikasi!");


        mAuth = FirebaseAuth.getInstance();

        SendVerificationCode(phoneNumber);

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(verifyCodeInput.getText().toString()))
                {
                    Toast.makeText(LoginActivity.this, "Wrong OTP Entered", Toast.LENGTH_SHORT).show();
                }
                else
                    VerifyCode(verifyCodeInput.getText().toString());
                }
            }
        );
    }

    private void SendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)  // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            final String code = credential.getSmsCode();
            if (code != null) {
                VerifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(LoginActivity.this, "Verification Failed : " + e.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s, token);
            verificationID = s;
            Toast.makeText(LoginActivity.this, "Code sent", Toast.LENGTH_SHORT).show();
        }
    };

    private void VerifyCode(String Code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, Code);
        SignInByCredentials(credential);
    }

    private void SignInByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        }

                    }
                }
            );
    }
}