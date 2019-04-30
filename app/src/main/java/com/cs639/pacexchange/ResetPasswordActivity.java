package com.cs639.pacexchange;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText inputEmail;
    private Button btnReset;
    private TextView btnBack;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        //Initialize UI elements
        inputEmail = findViewById(R.id.email);
        btnReset = findViewById(R.id.btn_reset_password);
        btnBack = findViewById(R.id.btn_back);
        progressBar = findViewById(R.id.progressBar);
        //Get firebase auth instance
        auth = FirebaseAuth.getInstance();
        //Navigate back to login activity
        btnBack.setOnClickListener(v -> finish());
        //Attempt to send password reset email
        btnReset.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            //Verify email field is not empty
            if (TextUtils.isEmpty(email))
                Toast.makeText(this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
            //Show progressbar while communicating with server to send reset email
            progressBar.setVisibility(View.VISIBLE);
            //Show toast message varying on password reset success
            auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Toast.makeText(this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            });
        });
    }
}

