package com.example.broker.Owner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.broker.R;

public class OwnerRoomActivity extends AppCompatActivity {
    private String roomName;
    private String roomAddress;
    private String roomBedrooms;
    private String roomBathrooms;
    private String roomKitchen;
    private String roomRent;
    private String roomAdvance;
    private String roomOwner;
    private String imageUrl;

    private ImageView roomImg;
    private TextView roomNameTv;
    private TextView roomAddressTv;
    private TextView roomKitchenTv;
    private TextView roomBedsTv;
    private TextView roomBathsTv;
    private TextView roomRentTv;
    private LottieAnimationView loadingAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_room);
        getSupportActionBar().hide();
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.theme_color));
        window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        Intent intent = getIntent();
        roomName = intent.getStringExtra("roomName");
        roomAddress = intent.getStringExtra("roomAddress");
        roomBedrooms = intent.getStringExtra("roomBedrooms");
        roomBathrooms = intent.getStringExtra("roomBathrooms");
        roomKitchen = intent.getStringExtra("roomKitchen");
        roomRent = intent.getStringExtra("roomRent");
        roomAdvance = intent.getStringExtra("roomAdvance");
        roomOwner = intent.getStringExtra("roomOwner");
        imageUrl = intent.getStringExtra("imageUrl");

        roomImg = findViewById(R.id.roomImg2);
        roomNameTv = findViewById(R.id.roomName2);
        roomAddressTv = findViewById(R.id.roomAddress2);
        roomBedsTv = findViewById(R.id.roomBeds2);
        roomBathsTv = findViewById(R.id.roomBathrooms2);
        roomKitchenTv = findViewById(R.id.roomKitchens2);
        roomRentTv = findViewById(R.id.roomRent2);
        loadingAnim = findViewById(R.id.loadingAnim5);

        loadingAnim.setVisibility(View.VISIBLE);


        if (roomName != null && roomAddress != null &&
                roomBedrooms != null && roomBathrooms != null &&
                roomKitchen != null && roomRent != null &&
                roomAdvance != null && roomOwner != null && imageUrl != null) {
            Glide.with(this).load(imageUrl).placeholder(R.drawable.res1)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            loadingAnim.setVisibility(View.GONE);
                            Toast.makeText(OwnerRoomActivity.this, "failed to load image", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            loadingAnim.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(roomImg);
            roomNameTv.setText(roomName);
            roomAddressTv.setText(roomAddress);
            roomBedsTv.setText(roomBedrooms);
            roomBathsTv.setText(roomBathrooms);
            roomKitchenTv.setText(roomKitchen);
            roomRentTv.setText(roomRent);
        }
    }
}