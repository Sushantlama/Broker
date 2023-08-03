package com.example.broker.Owner;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.broker.R;

public class OwnerSetUpProfile extends AppCompatActivity {

    Uri selectedImage;

    ImageView imageView;
    Button continueBtn;
    EditText nameBox;
    EditText ageBox;
    EditText phoneBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_set_up_profile);

        imageView = findViewById(R.id.imageView);
        continueBtn = findViewById(R.id.continueBtn);
        nameBox = findViewById(R.id.nameBox);
        ageBox = findViewById(R.id.ageBox);
        phoneBox = findViewById(R.id.PhoneBox);



        getSupportActionBar().hide();
        ActivityResultLauncher<Intent> imagesResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        if(data!=null){
                            imageView.setImageURI(data.getData());
                            selectedImage = data.getData();

                        }
                    }
                });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                imagesResultLauncher.launch(intent);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameBox.getText().toString();
                String age = ageBox.getText().toString();
                String phone = phoneBox.getText().toString();
                if(name.isEmpty()) {
                    nameBox.setError("Please type a name");
                    return;
                }
                if(age.isEmpty()) {
                    ageBox.setError("Please type a age");
                    return;
                }
                if(phone.isEmpty()) {
                    phoneBox.setError("Please type a phone number");
                    return;
                }

                if(selectedImage != null) {
                    Intent intent = new Intent(getApplicationContext(), OwnerSignupActivity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("age",age);
                    intent.putExtra("phone",phone);
                    String image = selectedImage.toString();
                    intent.putExtra("image",image);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), OwnerSignupActivity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("age",age);
                    intent.putExtra("phone",phone);
                    intent.putExtra("image","No Image");
                    startActivity(intent);
                }

            }
        });
    }


}