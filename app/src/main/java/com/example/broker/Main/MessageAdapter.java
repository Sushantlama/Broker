package com.example.broker.Main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.broker.R;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter{

    private ArrayList<Message> messages;
    private Context context;
    final int ITEM_SEND = 1;
    final int ITEM_RECEIVE =2;

    public MessageAdapter( Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.send_message,parent,false);
            return new SendViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.receive_message,parent,false);
        return new ReceiverViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderId())){
            return ITEM_SEND;
        }
        return ITEM_RECEIVE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if(holder.getClass() == SendViewHolder.class){
            SendViewHolder viewHolder = (SendViewHolder) holder;
            viewHolder.sendTextView.setText(message.getMessage());
        }
        else{
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.receiveTextView.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class SendViewHolder extends RecyclerView.ViewHolder{
        private final TextView sendTextView;
        public SendViewHolder(@NonNull View itemView) {
            super(itemView);
            sendTextView = itemView.findViewById(R.id.sendMessage);
        }
    }
    public class ReceiverViewHolder extends RecyclerView.ViewHolder{
        private final TextView receiveTextView;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiveTextView = itemView.findViewById(R.id.receiveMessage);
        }
    }
}
