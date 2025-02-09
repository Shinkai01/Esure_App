package com.example.esureapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences sharedPreferences = getSharedPreferences("share_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        Toast.makeText(getApplicationContext(), "Welcome"+username,Toast.LENGTH_SHORT).show();
        CardView GPSLocation = findViewById(R.id.GPS);
        GPSLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,GPSLocation.class));
            }

        });
        CardView LoadingStation = findViewById(R.id.LoadingStation);
        LoadingStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,loadingActivity.class));
            }

        });
        CardView Status = findViewById(R.id.card);
        Status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,statusActivity.class));
            }

        });

        CardView registration = findViewById(R.id.ScooterRegistration);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,ScooterRegActivity.class));
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.bot_profile) {
                    startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                    return true;

                } else if (itemId == R.id.bot_load) {
                    startActivity(new Intent(HomeActivity.this, PayLoadActivity.class));
                    return true;
                }
                else if (itemId == R.id.bot_exit) {
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    };
}

