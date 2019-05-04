package com.cs639.pacexchange;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputName, inputPhone, inputYear;
    private Button btnResetPassword;
    private TextView btnSignIn, btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //Get Firebase auth and firestore instances
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //Initialize UI elements
        btnSignIn = findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        btnResetPassword = findViewById(R.id.btn_reset_password);
        inputName = findViewById(R.id.name);
        inputPhone = findViewById(R.id.phone);
        inputYear = findViewById(R.id.grad);
        //Finish activity on sign up
        btnSignIn.setOnClickListener(v -> finish());
        //Write user information to server and check if successful
        registerUser();
    }

    public void registerUser() {
        btnSignUp.setOnClickListener(v -> {

            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            //Verify text fields are filled and password at least 6 characters
            if (TextUtils.isEmpty(email))
                Toast.makeText(this, "Enter email address!", Toast.LENGTH_SHORT).show();
            else if (TextUtils.isEmpty(password))
                Toast.makeText(this, "Enter password!", Toast.LENGTH_SHORT).show();
            else if (password.length() < 6)
                Toast.makeText(this, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            //Show progressbar while registering user with server
            progressBar.setVisibility(View.VISIBLE);
            //create user
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                Toast.makeText(this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful())
                    Toast.makeText(this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                else {
                    FirebaseUser user = auth.getCurrentUser();
                    //Set user display name and update profile
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(inputName.getText().toString()).build();
                    user.updateProfile(profileUpdates);
                    //Create firestore doc for user data
                    Map<String, Object> users = new HashMap<>();
                    //Save user fields to created doc
                    users.put("name", inputName.getText().toString());
                    users.put("email", inputEmail.getText().toString());
                    users.put("phone", inputPhone.getText().toString());
                    users.put("year", Integer.parseInt(inputYear.getText().toString()));
                    users.put("reputation", 0);
                    users.put("sales", 0);
                    db.collection("users").document(user.getUid()).set(users);

                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
            });

        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}