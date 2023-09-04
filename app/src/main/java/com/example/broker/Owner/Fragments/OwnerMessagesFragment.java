package com.example.broker.Owner.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.broker.Main.RecyclerViewInterface;
import com.example.broker.Main.User;
import com.example.broker.Owner.OwnerChatActivity;
import com.example.broker.R;
import com.example.broker.Renter.RoomActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class OwnerMessagesFragment extends Fragment implements RecyclerViewInterface {

    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    ArrayList<User> users;
    UserMessagesAdapter userMessagesAdapter;
    RecyclerView messagesRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_owner_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        users = new ArrayList<>();
        userMessagesAdapter = new UserMessagesAdapter(this, this,users);
        messagesRecyclerView = view.findViewById(R.id.messagesRecyclerView);
        messagesRecyclerView.setAdapter(userMessagesAdapter);
        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot mysnapshot : snapshot.getChildren()){
                    User user = mysnapshot.getValue(User.class);
                    if(Objects.equals(user.getUid(), firebaseAuth.getUid())){
                        continue;
                    }
                    users.add(user);
                }
                userMessagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onItemClick(int position) {
        User user = users.get(position);
        Intent intent = new Intent(getContext(), OwnerChatActivity.class);
        intent.putExtra("name",user.getName());
        intent.putExtra("uid",user.getUid());
        startActivity(intent);
    }
}