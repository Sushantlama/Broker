package com.example.broker.Main.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.broker.Main.RecyclerViewInterface;
import com.example.broker.Main.classes.User;
import com.example.broker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        String senderId = FirebaseAuth.getInstance().getUid();
        String senderRoom = senderId+ user.getUid();
        FirebaseDatabase.getInstance().getReference()
                        .child("chats")
                                .child(senderRoom)
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){
                                                    String lastmsg = snapshot.child("lastMsg").getValue(String.class);
                                                    long time = snapshot.child("lastMsgTime").getValue(Long.class);
                                                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                                                    holder.lastMessage.setText(lastmsg);
                                                    holder.time.setText(dateFormat.format(new Date(time)));
                                                }
                                                else{
                                                    holder.lastMessage.setText("Tap to chat");
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
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
