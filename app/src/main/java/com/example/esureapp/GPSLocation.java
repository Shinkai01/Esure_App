package com.example.esureapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GPSLocation extends AppCompatActivity {

    private TextView scooterLocationlatTextView, scooterLocationlongTextView;
    private WebView mapWebView;

    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpslocation);

        scooterLocationlatTextView = findViewById(R.id.scooterlocationlat);
        scooterLocationlongTextView = findViewById(R.id.scooterlocationlong);
        mapWebView = findViewById(R.id.mapWebView);
        mapWebView.setWebViewClient(new WebViewClient());
        mapWebView.getSettings().setJavaScriptEnabled(true);
        reference = FirebaseDatabase.getInstance().getReference("SCOOTERSTATUS/LOCATION");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String locationlat = dataSnapshot.child("latitude").getValue(String.class);
                String locationlong = dataSnapshot.child("longitude").getValue(String.class);
                scooterLocationlatTextView.setText(locationlat);
                scooterLocationlongTextView.setText(locationlong);
                if (locationlat != null && locationlong != null) {
                    double lat = Double.parseDouble(locationlat);
                    double lng = Double.parseDouble(locationlong);
                    showLocationOnMap(lat, lng);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void showLocationOnMap(double lat, double lng) {
        String mapUrl = "https://www.openstreetmap.org/?mlat=" + lat + "&mlon=" + lng + "#map=15/" + lat + "/" + lng;
        mapWebView.loadUrl(mapUrl);
    }


}
