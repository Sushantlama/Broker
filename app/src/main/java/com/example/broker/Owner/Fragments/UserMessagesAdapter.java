package com.example.broker.Owner.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.broker.Main.RecyclerViewInterface;
import com.example.broker.Main.User;
import com.example.broker.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserMessagesAdapter extends RecyclerView.Adapter<UserMessagesAdapter.UserMessagesViewHolder>{
    private final RecyclerViewInterface recyclerViewInterface;
    private Fragment fragment;
    private ArrayList<User> users;
    public UserMessagesAdapter(RecyclerViewInterface recyclerViewInterface, Fragment fragment, ArrayList<User> users){
        this.recyclerViewInterface = recyclerViewInterface;
        this.fragment = fragment;
        this.users = users;
    }

    @NonNull
    @Override
    public UserMessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_conversation,parent,false);
        return new UserMessagesViewHolder(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull UserMessagesViewHolder holder, int position) {
        User user = users.get(position);
        holder.username.setText(user.getName());
        if (user.getProfileImage().equals("No Image")) {
            Glide.with(fragment).load(R.drawable.avatar).into(holder.profilePic);
        } else {
            Glide.with(fragment).load(user.getProfileImage()).placeholder(R.drawable.avatar).into(holder.profilePic);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserMessagesViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profilePic;
        TextView username;
        TextView lastMessage;
        TextView time;
        public UserMessagesViewHolder(@NonNull View itemView,RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.mProfilePic);
            username = itemView.findViewById(R.id.mUserName);
            lastMessage = itemView.findViewById(R.id.mLastMessages);
            time = itemView.findViewById(R.id.mMessageTime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if(pos !=  RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
