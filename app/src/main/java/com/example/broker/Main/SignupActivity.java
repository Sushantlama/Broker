package com.example.broker.Main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.broker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "OwnerSignup";
    private static final String NoImg = "No Image";
    FirebaseDatabase database;
    FirebaseStorage storage;
    LottieAnimationView loadingAnim;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private EditText etEmail;
    private EditText etPassword;
    private TextView login;
    private String name;
    private String age;
    private String phone;
    private String image;
    private users myuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.theme_color));
        window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        age = intent.getStringExtra("age");
        phone = intent.getStringExtra("phone");
        image = intent.getStringExtra("image");
        if (intent.getIntExtra("users", 0) == 0) {
            myuser = users.Renter;
        } else {
            myuser = users.Owner;
        }


        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Button signupBtn = (Button) findViewById(R.id.oSignupBtn);
        login = findViewById(R.id.oSignupLoginBtn);
        etEmail = findViewById(R.id.oSignupMailBox);
        etPassword = findViewById(R.id.oSignupPassBox);
        loadingAnim = findViewById(R.id.loadingAnim2);


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    private void CreateAccount() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (email.isEmpty()) {
            etEmail.setError("Please type a Email");
            return;
        }
        if (password.isEmpty()) {
            etEmail.setError("Please type a Password");
            return;
        }


        loadingAnim.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        etEmail.setText("");
                                        etPassword.setText("");
                                        CreateProfile(mAuth.getCurrentUser());
                                    } else {
                                        loadingAnim.setVisibility(View.GONE);
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignupActivity.this, "Could Not send Verification Mail", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            loadingAnim.setVisibility(View.GONE);
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void CreateProfile(FirebaseUser user) {
        if (user == null) {
            loadingAnim.setVisibility(View.GONE);
            return;
        }
        StorageReference storageReference = storage.getReference().child("Profiles").child(user.getUid());
        databaseReference = database.getReference().child("users");
        if (image.equals(NoImg)) {
            String uid = user.getUid();
            String email = user.getEmail();
            User owner = new User(uid, name, email, image, age, phone, myuser);
            Map<String, Object> update = new HashMap<>();
            update.put(uid, owner);
            databaseReference.updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    mAuth.signOut();
                    loadingAnim.setVisibility(View.GONE);
                    Log.d(TAG, "createUserWithEmail:success");
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    Toast.makeText(SignupActivity.this, "User registered successfully,Please Verify your email", Toast.LENGTH_SHORT).show();
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loadingAnim.setVisibility(View.GONE);
                    Log.i(TAG, "onFailure: " + e);
                    mAuth.signOut();
                    Toast.makeText(SignupActivity.this, "Failed to upload account details", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Uri imageUri = Uri.parse(image);
            storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageDownloadUri = uri.toString();
                                String uid = user.getUid();
                                String email = user.getEmail();
                                User user = new User(uid, name, email, imageDownloadUri, age, phone, myuser);
                                Map<String, Object> update = new HashMap<>();
                                update.put(uid, user);
                                databaseReference
                                        .updateChildren(update)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d(TAG, "createUserWithEmail:success");
                                                mAuth.signOut();
                                                loadingAnim.setVisibility(View.GONE);
                                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                Toast.makeText(SignupActivity.this, "User registered successfully,Please Verify your email", Toast.LENGTH_SHORT).show();
                                                startActivity(i);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                loadingAnim.setVisibility(View.GONE);
                                                mAuth.signOut();
                                                Log.i(TAG, "onFailure: " + e);
                                                Toast.makeText(SignupActivity.this, "Failed to upload account details", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loadingAnim.setVisibility(View.GONE);
                    Log.w(TAG, "FileUpload : fail", e);
                }
            });
        }
    }
}