package com.example.broker.Renter.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.broker.Main.LoginActivity;
import com.example.broker.Main.User;
import com.example.broker.Main.users;
import com.example.broker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RenterProfileFragment extends Fragment {

    private static final String TAG = "Renter Profile Fragment";

    ImageView profileImg;
    Button logoutBtn;
    TextView nameTV;
    TextView emailTV;
    TextView numberTV;
    TextView editBtn;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_renter_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logoutBtn = view.findViewById(R.id.rLogoutBtn);
        nameTV = view.findViewById(R.id.rProfileName);
        emailTV = view.findViewById(R.id.rProfileEmail);
        numberTV = view.findViewById(R.id.rProfileNumber);
        editBtn = view.findViewById(R.id.rProfileEditBtn);
        profileImg = view.findViewById(R.id.rProfileImageView);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference().child("users").child(mAuth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String age = snapshot.child("age").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                String imageUrl = snapshot.child("profileImage").getValue(String.class);
                String uid = snapshot.child("uid").getValue(String.class);
                users myUser = snapshot.child("user").getValue(users.class);
                User user = new User(uid, name, email, imageUrl, age, phoneNumber, myUser);
                setProfile(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, "onCancelled: " + error);
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent i = new Intent(getContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "edit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setProfile(User user) {
        if (user.getProfileImage().equals("No Image")) {
            Glide.with(this).load(R.drawable.avatar).into(profileImg);
        } else {
            Glide.with(this).load(user.getProfileImage()).placeholder(R.drawable.avatar).into(profileImg);
        }
        nameTV.setText(user.getName());
        emailTV.setText(user.getEmail());
        numberTV.setText(user.getPhoneNumber());
    }
}