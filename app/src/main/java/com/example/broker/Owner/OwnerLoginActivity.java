package com.example.broker.Owner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.broker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OwnerLoginActivity extends AppCompatActivity {

    private static final String TAG = "Login";
    private FirebaseAuth mAuth;
    private EditText Email;
    private EditText Password;
    private TextView signup;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i = new Intent(getApplicationContext(), OwnerActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_login);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        Email = findViewById(R.id.emailBox);
        Password = findViewById(R.id.editTextLoginPassword);
        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });
        signup = findViewById(R.id.Signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), OwnerSetUpProfile.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void SignIn(){
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "email cannot be empty", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "password cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if(user!= null){
                                    if(user.isEmailVerified()){
                                        Intent i = new Intent(getApplicationContext(), OwnerActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                    else{
                                        mAuth.signOut();
                                        Toast.makeText(OwnerLoginActivity.this, "Please Verify email and try again", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                // Sign in success, update UI with the signed-in user's information
//                              updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(OwnerLoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                              updateUI(null);
                            }
                        }
                    }
                    );
        }
    }

}