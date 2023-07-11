package com.example.broker.Owner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.broker.R;

public class PostActivity extends AppCompatActivity {

    private static final String TAG = "Post";

    ImageView close;
    ImageView addImage;
    EditText etRoomName;
    EditText etRoomAddress;
    EditText etRoomBedrooms;
    EditText etRoomBathrooms;
    EditText etRoomKitchen;
    EditText etRoomRent;
    EditText etRoomAdvance;
    TextView post;

    Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_post);
        close = findViewById(R.id.close);
        addImage = findViewById(R.id.addImage);

        etRoomName = findViewById(R.id.EtRoomName);
        etRoomAddress = findViewById(R.id.EtRoomAddress);
        etRoomBedrooms = findViewById(R.id.EtRoomBedrooms);
        etRoomBathrooms = findViewById(R.id.EtRoomBathrooms);
        etRoomKitchen = findViewById(R.id.EtRoomsKitchen);
        etRoomRent = findViewById(R.id.EtRoomRent);
        etRoomAdvance = findViewById(R.id.EtRoomAdvance);

        post = findViewById(R.id.post);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), OwnerActivity.class);
                startActivity(i);
                finish();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });

        ActivityResultLauncher<Intent> imagesResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        if(data!=null){
                            addImage.setImageURI(data.getData());
                            selectedImage = data.getData();
                        }
                    }
                });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                imagesResultLauncher.launch(intent);
            }
        });
    }
    public void post(){

        String roomName = etRoomName.getText().toString();
        String roomAddress = etRoomAddress.getText().toString();
        String roomBedrooms = etRoomBedrooms.getText().toString();
        String  roomBathrooms = etRoomBathrooms.getText().toString();
        String  roomKitchen = etRoomKitchen.getText().toString();
        String  roomRent = etRoomRent.getText().toString();
        String  roomAdvance = etRoomAdvance.getText().toString();
        if(roomName.isEmpty()){
            etRoomName.setError("Please type a name for room");
            return;
        }
        if(roomAddress.isEmpty()){
            etRoomAddress.setError("Please type the address");
            return;
        }
        if(roomBedrooms.isEmpty()){
            etRoomBedrooms.setError("Enter No of BedRooms");
            return;
        }

        if(roomBathrooms.isEmpty()){
            etRoomBathrooms.setError("Enter No of Bathrooms");
            return;
        }
        if(roomKitchen.isEmpty()){
            etRoomKitchen.setError("Enter No of Kitchen");
            return;
        }

        if(roomRent.isEmpty()){
            etRoomRent.setError("Enter Room Rent ");
            return;
        }
        if(roomAdvance.isEmpty()){
            etRoomAdvance.setError("Enter Room Advance");
            return;
        }
        Log.i(TAG, "post: " + roomName + " "+ roomAddress+" "+roomBedrooms+" "+roomBathrooms+" "+roomKitchen+" "+roomRent+" "+roomAdvance);
    }
}