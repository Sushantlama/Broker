package com.example.broker.Owner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.broker.Main.Message;
import com.example.broker.Main.MessageAdapter;
import com.example.broker.Owner.Fragments.OwnerHomeFragment;
import com.example.broker.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class OwnerChatActivity extends AppCompatActivity {

    MessageAdapter adapter;
    ArrayList<Message> messages;
    String senderRoom,receiverRoom;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_chat);
        Intent intent = getIntent();

        String username = intent.getStringExtra("name");
        String receiverUid = intent.getStringExtra("uid");
        String senderUid = FirebaseAuth.getInstance().getUid();

        senderRoom = senderUid+receiverUid;
        receiverRoom = receiverUid+senderUid;

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.theme_color)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(username!=null){
            getSupportActionBar().setTitle(username);
        }
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.theme_color));
        window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

        database = FirebaseDatabase.getInstance();

        EditText messageBox = findViewById(R.id.messageBox);
        ImageView sendBtn = (ImageView)findViewById(R.id.messageSendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String MessageText = messageBox.getText().toString();
                Date date = new Date();
                Message message = new Message(MessageText,senderUid,date.getTime());

                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference().child("chats")
                                        .child(receiverRoom)
                                        .child("messages")
                                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(OwnerChatActivity.this, "Message Sent SuccessFully", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
            }
        });

        messages = new ArrayList<>();
        adapter = new MessageAdapter(this,messages);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}