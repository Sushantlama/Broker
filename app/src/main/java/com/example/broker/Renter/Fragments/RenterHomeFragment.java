package com.example.broker.Renter.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.broker.Owner.Fragments.PostAdapter;
import com.example.broker.R;
import com.example.broker.Renter.RoomAdapter;
import com.example.broker.Room.room;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RenterHomeFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<room> rooms;
    RoomAdapter roomAdapter;
    TextView emptyList;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    private DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    LottieAnimationView loadingAnim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_renter_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rRecyclerview);
        emptyList = view.findViewById(R.id.rEmpty_list);
        loadingAnim = view.findViewById(R.id.rLoadingAnim);


        rooms = new ArrayList<>();
        roomAdapter = new RoomAdapter(rooms, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(roomAdapter);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference().child("public");

        loadingAnim.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DataSnapshot postDataSnapshot = dataSnapshot.child("posts");
                    for (DataSnapshot myDs : postDataSnapshot.getChildren()) {
                        room room = myDs.getValue(room.class);
                        rooms.add(room);
                    }
                }

                loadingAnim.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                if (rooms.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyList.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyList.setVisibility(View.VISIBLE);
                }
                roomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}