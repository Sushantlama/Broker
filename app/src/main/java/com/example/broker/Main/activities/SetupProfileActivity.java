package com.example.broker.Main.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.broker.Main.users;
import com.example.broker.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupProfileActivity extends AppCompatActivity {

    Uri selectedImage;

    CircleImageView imageView;
    Button continueBtn;
    EditText nameBox;
    EditText ageBox;
    EditText phoneBox;
    private Spinner spinner;

    private ArrayAdapter<users> arrayAdapter;
    private  int myuser = 0; // 0 for renter,1 for owner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_profile);
        getSupportActionBar().hide();
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.theme_color));
        window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

        imageView = findViewById(R.id.oAvatarView);
        continueBtn = findViewById(R.id.oContinueButton);
        nameBox = findViewById(R.id.oNameBox);
        ageBox = findViewById(R.id.oAgeBox);
        phoneBox = findViewById(R.id.oPhoneBox);


        spinner = findViewById(R.id.spinner);
        arrayAdapter = new ArrayAdapter<users>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,users.values());
        spinner.setSelection(myuser);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                myuser = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




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
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("age",age);
                intent.putExtra("phone",phone);
                intent.putExtra("users",myuser);
                if(selectedImage != null) {
                    String image = selectedImage.toString();
                    intent.putExtra("image",image);
                    startActivity(intent);
                } else {
                    intent.putExtra("image","No Image");
                    startActivity(intent);
                }

            }
        });
    }


}