package com.example.broker.Owner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.broker.R;
import com.example.broker.Room.room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

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
    String ownername ;
    String roomName;
    String roomAddress;
    String roomBedrooms;
    String  roomBathrooms;
    String  roomKitchen;
    String  roomRent;
    String  roomAdvance;
    String imageUrl;


    public void post(){
        roomName = etRoomName.getText().toString();
        roomAddress = etRoomAddress.getText().toString();
        roomBedrooms = etRoomBedrooms.getText().toString();
        roomBathrooms = etRoomBathrooms.getText().toString();
        roomKitchen = etRoomKitchen.getText().toString();
        roomRent = etRoomRent.getText().toString();
        roomAdvance = etRoomAdvance.getText().toString();

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
        databaseReference1 = firebaseDatabase.getReference("users/owner/"+firebaseAuth.getUid());
        databaseReference1
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Owner owner = snapshot.getValue(Owner.class);
                        ownername = owner.getName();
                        createPost();
                        Toast.makeText(PostActivity.this, ""+ownername, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i(TAG, "onCancelled: "+error.getCode());
                    }
                });
    }

    public void createPost(){
        Toast.makeText(this, "ceating post", Toast.LENGTH_SHORT).show();
        addImage();
    }

    public void addImage(){
        Toast.makeText(this, "addimage", Toast.LENGTH_SHORT).show();
        storageReference = FirebaseStorage.getInstance().getReference().child("post").child(Objects.requireNonNull(firebaseAuth.getUid())).child(String.valueOf(System.currentTimeMillis()));
        if(selectedImage == null){
            Toast.makeText(this, "no image", Toast.LENGTH_SHORT).show();
            imageUrl = "No image";
            room myroom = new room(roomName,roomAddress,roomBedrooms,roomBathrooms,roomKitchen,roomRent,roomAdvance,ownername,imageUrl);
            addToDb(myroom);
        }
        else{
            Toast.makeText(this, "yes image", Toast.LENGTH_SHORT).show();
            storageReference.putFile(selectedImage)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageUrl = uri.toString();
                                        room myroom = new room(roomName,roomAddress,roomBedrooms,roomBathrooms,roomKitchen,roomRent,roomAdvance,ownername,imageUrl);
                                        addToDb(myroom);
                                        Toast.makeText(PostActivity.this, ""+imageUrl, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else{
                                Log.i(TAG, "onComplete: "+task.getException());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG, "onFailure: "+e);
                        }
                    });
        }
    }
    public void addToDb(room troom){
        databaseReference2 = firebaseDatabase.getReference();
        Map<String, Object> update = new HashMap<>();
        update.put("public/"+firebaseAuth.getUid()+"/posts/"+System.currentTimeMillis(), troom);
        databaseReference2
                .updateChildren(update)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(PostActivity.this, "posted", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), OwnerActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure: "+e);
                    }
                });
    }
}