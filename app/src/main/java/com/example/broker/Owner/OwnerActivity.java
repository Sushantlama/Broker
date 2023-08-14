package com.example.broker.Owner;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.broker.Owner.Fragments.OwnerHomeFragment;
import com.example.broker.Owner.Fragments.OwnerMessagesFragment;
import com.example.broker.Owner.Fragments.OwnerProfileFragment;
import com.example.broker.Owner.Fragments.PostActivity;
import com.example.broker.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class OwnerActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.theme_color)));
        getSupportActionBar().setTitle("Your rooms");
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.theme_color));
        window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        replaceFragment(new OwnerHomeFragment());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.oHome) {
                    getSupportActionBar().setTitle("Your rooms");
                    replaceFragment(new OwnerHomeFragment());
                } else if (item.getItemId() == R.id.oMessage) {
                    getSupportActionBar().setTitle("Messages");
                    replaceFragment(new OwnerMessagesFragment());
                } else if (item.getItemId() == R.id.oAdd) {
                    Intent i = new Intent(getApplicationContext(), PostActivity.class);
                    startActivity(i);
                } else if (item.getItemId() == R.id.oSettings) {
                    getSupportActionBar().setTitle("Owner");
                    replaceFragment(new OwnerProfileFragment());
                }

                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}