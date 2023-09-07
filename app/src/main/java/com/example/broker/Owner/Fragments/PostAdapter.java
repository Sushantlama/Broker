package com.example.broker.Owner.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.broker.Main.RecyclerViewInterface;
import com.example.broker.R;
import com.example.broker.Room.room;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder> {
    private RecyclerViewInterface recyclerViewInterface;
    private ArrayList<room> rooms;
    private Fragment fragment;

    public PostAdapter(ArrayList<room> rooms, Fragment fragment,RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.rooms = rooms;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rooms_list_item, parent, false);
        return new viewHolder(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        room room = rooms.get(position);
        String imageUrl = room.getImageUrl();
        if (imageUrl.equals("No Image")) {
            Glide.with(fragment).load(R.drawable.res1).into(holder.getListImg());
        } else {
            Glide.with(fragment).load(imageUrl).into(holder.getListImg());
        }
        holder.getListName().setText(room.getRoomName());
        holder.getListKitchen().setText(String.valueOf(room.getRoomKitchen()));
        holder.getListAddress().setText(room.getRoomAddress());
        holder.getListBeds().setText(String.valueOf(room.getRoomBedrooms()));
        holder.getListRent().setText(room.getRoomRent() + " inr/month");
        holder.getListBathrooms().setText(String.valueOf(room.getRoomBathrooms()));

    }


    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        private ImageView listImg;
        private TextView listName;
        private TextView listAddress;
        private TextView listKitchen;
        private TextView listBeds;
        private TextView listRent;
        private TextView listBathrooms;

        public viewHolder(@NonNull View itemView,RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            listImg = itemView.findViewById(R.id.listRoomImg);
            listName = itemView.findViewById(R.id.listName);
            listAddress = itemView.findViewById(R.id.listAddress);
            listKitchen = itemView.findViewById(R.id.listKitchen);
            listBeds = itemView.findViewById(R.id.listBeds);
            listRent = itemView.findViewById(R.id.listRent);
            listBathrooms = itemView.findViewById(R.id.listBathrooms);
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

        public ImageView getListImg() {
            return listImg;
        }

        public TextView getListAddress() {
            return listAddress;
        }

        public TextView getListName() {
            return listName;
        }

        public TextView getListBeds() {
            return listBeds;
        }

        public TextView getListKitchen() {
            return listKitchen;
        }

        public TextView getListRent() {
            return listRent;
        }

        public TextView getListBathrooms() {
            return listBathrooms;
        }
    }
}
