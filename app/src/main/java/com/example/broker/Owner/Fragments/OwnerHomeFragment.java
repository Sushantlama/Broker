package com.example.broker.Owner.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.broker.Main.RecyclerViewInterface;
import com.example.broker.R;
import com.example.broker.Renter.RoomActivity;
import com.example.broker.Room.room;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OwnerHomeFragment extends Fragment implements RecyclerViewInterface {

    private static final String TAG = "Home";

    RecyclerView recyclerView;
    ArrayList<room> rooms;
    PostAdapter postAdapter;
    TextView emptyList;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    private DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    LottieAnimationView loadingAnim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_owner_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview);
        emptyList = view.findViewById(R.id.empty_list);
        loadingAnim = view.findViewById(R.id.loadingAnim);


        rooms = new ArrayList<>();
        postAdapter = new PostAdapter(rooms, this,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(postAdapter);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference().child("public").child(mAuth.getUid()).child("posts");

        loadingAnim.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    room room = dataSnapshot.getValue(room.class);
                    rooms.add(room);
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
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(int position) {
        room room = rooms.get(position);
        Intent intent = new Intent(getContext(), RoomActivity.class);
        intent.putExtra("roomName",room.getRoomName());
        intent.putExtra("roomAddress",room.getRoomAddress());
        intent.putExtra("roomBedrooms",room.getRoomBedrooms());
        intent.putExtra("roomBathrooms",room.getRoomBathrooms());
        intent.putExtra("roomKitchen",room.getRoomKitchen());
        intent.putExtra("roomRent",room.getRoomRent());
        intent.putExtra("roomAdvance",room.getRoomAdvance());
        intent.putExtra("roomOwner",room.getRoomOwner());
        intent.putExtra("imageUrl",room.getImageUrl());
        startActivity(intent);
    }
}