package com.example.broker.Owner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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

public class OwnerSignupActivity extends AppCompatActivity {

    private static final String TAG = "OwnerSignup";
    private static final String NoImg = "No Image";

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    private DatabaseReference databaseReference;

    private EditText etEmail;
    private EditText etPassword;
    private TextView login;

    private  String name;
    private  String age;
    private  String phone;
    private String image;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Toast.makeText(this, "user exist", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), OwnerActivity.class);
            startActivity(i);
            finish();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_signup);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        age = intent.getStringExtra("age");
        phone = intent.getStringExtra("phone");
        image = intent.getStringExtra("image");



        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Button signupBtn = (Button) findViewById(R.id.SignupBtn);
        login = findViewById(R.id.Login);
        init();
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), OwnerLoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private  void init(){
        etEmail = findViewById(R.id.OwnerSignUpEmail);
        etPassword = findViewById(R.id.OwnerSignUpPassword);
    }

    private void CreateAccount(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if(email.isEmpty()) {
            etEmail.setError("Please type a Email");
            return;
        }
        if (password.isEmpty()) {
            etEmail.setError("Please type a Password");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        etEmail.setText("");
                                        etPassword.setText("");
                                        CreateProfile(mAuth.getCurrentUser());
                                    }
                                    else{
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(OwnerSignupActivity.this, "Could Not send Verification Mail", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else{
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(OwnerSignupActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private  void CreateProfile(FirebaseUser user){
        if(user == null){
            return;
        }
        StorageReference storageReference = storage.getReference().child("Profiles").child(user.getUid());
        databaseReference = database.getReference().child("users").child("owner");
        if(image.equals(NoImg)){
            String uid = user.getUid();
            String email = user.getEmail();
            Owner owner = new Owner(uid,name,email,image,age,phone);
            Map<String, Object> update = new HashMap<>();
            update.put(uid, owner);
            databaseReference.updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    mAuth.signOut();
                    Log.d(TAG, "createUserWithEmail:success");
                    Intent i = new Intent(getApplicationContext(), OwnerLoginActivity.class);
                    Toast.makeText(OwnerSignupActivity.this, "User registered successfully,Please Verify your email", Toast.LENGTH_SHORT).show();
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "onFailure: "+e);
                    mAuth.signOut();
                    Toast.makeText(OwnerSignupActivity.this, "Failed to upload account details", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Uri imageUri = Uri.parse(image);
            storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageDownloadUri = uri.toString();
                                String uid = user.getUid();
                                String email = user.getEmail();
                                Owner owner = new Owner(uid,name,email,imageDownloadUri,age,phone);
                                Map<String, Object> update = new HashMap<>();
                                update.put(uid, owner);
                                databaseReference
                                        .updateChildren(update)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d(TAG, "createUserWithEmail:success");
                                                mAuth.signOut();
                                                Intent i = new Intent(getApplicationContext(), OwnerLoginActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                Toast.makeText(OwnerSignupActivity.this, "User registered successfully,Please Verify your email", Toast.LENGTH_SHORT).show();
                                                startActivity(i);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                mAuth.signOut();
                                                Log.i(TAG, "onFailure: "+e);
                                                Toast.makeText(OwnerSignupActivity.this, "Failed to upload account details", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "FileUpload : fail",e);
                }
            });
        }
    }
}