package com.example.broker.Owner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.broker.R;

public class OwnerActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
        getSupportActionBar().hide();

        ActivityResultLauncher<Intent> imagesResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            if (result.getData().getClipData() != null) {
                                int count = result.getData().getClipData().getItemCount();

                                for (int i = 0; i < count; i++) {
                                    Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                                    Toast.makeText(this, "1" + imageUri.toString(), Toast.LENGTH_SHORT).show();
                                }
                                //TODO: do something; here is your selected images
                            } else if (result.getData() != null) {
                                String imagePath = result.getData().toString();
                                Toast.makeText(this, "2" + imagePath, Toast.LENGTH_SHORT).show();
                                //TODO: do something
                            }
                        }
                });
        Button photos_btn = (Button) findViewById(R.id.photosBtn);
        photos_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                imagesResultLauncher.launch(intent);
            }
        });



    }
}