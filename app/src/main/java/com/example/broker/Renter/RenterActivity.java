package com.example.broker.Renter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.broker.R;
import com.example.broker.Renter.Fragments.RenterHomeFragment;
import com.example.broker.Renter.Fragments.RenterProfileFragment;
import com.example.broker.Renter.Fragments.RenterSearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class RenterActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renter);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.theme_color)));
        getSupportActionBar().setTitle("Broker");
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.theme_color));
        window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        replaceFragment(new RenterHomeFragment());
        bottomNavigationView = findViewById(R.id.renterBottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.rhome) {
                    getSupportActionBar().setTitle("Broker");
                    replaceFragment(new RenterHomeFragment());
                } else if (item.getItemId() == R.id.rsearch) {
                    getSupportActionBar().setTitle("Search");
                    replaceFragment(new RenterSearchFragment());
                } else if (item.getItemId() == R.id.rprofile) {
                    getSupportActionBar().setTitle("Renter");
                    replaceFragment(new RenterProfileFragment());
                }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.renterFrameLayout, fragment);
        fragmentTransaction.commit();
    }

}