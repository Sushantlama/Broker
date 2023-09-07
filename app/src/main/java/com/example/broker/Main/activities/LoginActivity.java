package com.example.broker.Main.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.broker.Main.users;
import com.example.broker.Owner.OwnerActivity;
import com.example.broker.R;
import com.example.broker.Renter.RenterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "OwnerLogin";
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText Email;
    private EditText Password;
    private LottieAnimationView loadingAnim;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            String myUser = sh.getString("user","Owner");
            if(myUser.equals("Owner")){
                Intent i = new Intent(getApplicationContext(), OwnerActivity.class);
                startActivity(i);
                finish();
            }
            else{
                Intent i = new Intent(getApplicationContext(), RenterActivity.class);
                startActivity(i);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.theme_color)));
        getSupportActionBar().setTitle("Broker");
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.theme_color));
        window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        Email = findViewById(R.id.LoginMailBox);
        Password = findViewById(R.id.LoginPassBox);
        Button login = findViewById(R.id.LoginBtn);
        loadingAnim = findViewById(R.id.loadingAnim1);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingAnim.setVisibility(View.VISIBLE);
                SignIn();
            }
        });

        TextView signup = findViewById(R.id.LoginSignupBtn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SetupProfileActivity.class);
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
                                        databaseReference = firebaseDatabase.getReference().child("users").child(mAuth.getCurrentUser().getUid());
                                        databaseReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                users myUser = snapshot.child("user").getValue(users.class);
                                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                                loadingAnim.setVisibility(View.GONE);
                                                if(myUser == users.Owner){
                                                    myEdit.putString("user","Owner");
                                                    myEdit.apply();
                                                    Intent i = new Intent(getApplicationContext(), OwnerActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                }
                                                else {
                                                    myEdit.putString("user","Renter");
                                                    myEdit.apply();
                                                    Intent i = new Intent(getApplicationContext(),RenterActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                loadingAnim.setVisibility(View.GONE);
                                                mAuth.signOut();
                                                Log.i(TAG, "onCancelled: "+error);
                                            }
                                        });

                                    }
                                    else{
                                        loadingAnim.setVisibility(View.GONE);
                                        mAuth.signOut();
                                        Toast.makeText(LoginActivity.this, "Please Verify email and try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                // Sign in success, update UI with the signed-in user's information
//                              updateUI(user);
                            } else {
                                loadingAnim.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                              updateUI(null);
                            }
                        }
                    }
                    );
        }
    }

}