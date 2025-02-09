package com.example.esureapp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.view.View;
import android.widget.Button;

public class statusActivity extends AppCompatActivity {

    private TextView textViewStatus, textViewLocationlat, textViewLocationlong, textViewTimer, textViewUser;
    private DatabaseReference mDatabase;
    private final String CHANNEL_ID = "AccidentAlertChannel";
    private final int NOTIFICATION_ID = 001;
    private final int REQUEST_NOTIFICATION_PERMISSION = 100;  // Request code for permission
    private Button buttonSafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_status);

        textViewStatus = findViewById(R.id.status);
        textViewLocationlat = findViewById(R.id.locationlat);
        textViewLocationlong = findViewById(R.id.locationlong);
        textViewTimer = findViewById(R.id.timer);
        textViewUser = findViewById(R.id.tvuser);

        // Initialize the button
        buttonSafe = findViewById(R.id.buttonSafe);

        // Set an OnClickListener for the button
        buttonSafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatusToSafe();
            }
        });

        // Initialize Firebase database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("SCOOTERSTATUS");

        // Create notification channel for Android O and above
        createNotificationChannel();

        // Listening for status changes in Firebase
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = dataSnapshot.child("STATUS").getValue(String.class);
                String latitude = dataSnapshot.child("LOCATION").child("latitude").getValue(String.class);
                String longitude = dataSnapshot.child("LOCATION").child("longitude").getValue(String.class);
                String timer = dataSnapshot.child("DATE").getValue(String.class);
                String user = dataSnapshot.child("USER").getValue(String.class);

                // Log values for debugging purposes
                Log.d("FirebaseData", "Status: " + status);
                Log.d("FirebaseData", "Latitude: " + latitude);
                Log.d("FirebaseData", "Longitude: " + longitude);
                Log.d("FirebaseData", "Timer: " + timer);
                Log.d("FirebaseData", "User: " + user);

                // Check for null before setting text
                textViewStatus.setText(status != null ? status : "N/A");
                textViewLocationlat.setText(latitude != null ? latitude : "N/A");
                textViewLocationlong.setText(longitude != null ? longitude : "N/A");
                textViewTimer.setText(timer != null ? timer : "N/A");
                textViewUser.setText(user != null ? user : "N/A");

                // Check if the status is "Accident Detected"
                if ("Accident Detected".equals(status)) {
                    showAccidentAlert();
                    checkAndRequestNotificationPermission();  // Check and request notification permission
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
                Log.e("FirebaseError", databaseError.getMessage());
            }
        });
    }

    // Method to set status to "Safe"
    private void setStatusToSafe() {
        mDatabase.child("STATUS").setValue("Safe")
                .addOnSuccessListener(aVoid -> {
                    Log.d("FirebaseUpdate", "Status updated to Safe successfully.");
                    textViewStatus.setText("Safe"); // Update the UI to reflect the change
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseUpdate", "Failed to update status: " + e.getMessage());
                });
    }
    private void showAccidentAlert() {
        if (!isFinishing()) { // Check if the activity is not finishing
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alert!!!")
                    .setMessage("Accident Detected! Someone could've been in an accident! Please Check your E-Scooter")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    // Check and request notification permission
    private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {  // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request notification permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION_PERMISSION);
            } else {
                // Permission already granted, send the notification
                sendNotification();
            }
        } else {
            // Below Android 13, permission is granted by default
            sendNotification();
        }
    }

    // Send the notification
    private void sendNotification() {
        // Create an explicit intent for an activity in your app
        Intent intent = new Intent(this, statusActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Use FLAG_IMMUTABLE for Android 12+
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            // Use FLAG_UPDATE_CURRENT for older versions
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)  // Set your notification icon here
                .setContentTitle("Accident Alert")
                .setContentText("Accident Detected! Please check the status.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)  // Opens the app when notification is clicked
                .setAutoCancel(true);  // Dismiss the notification after it's clicked

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Request notification permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
            return;
        }

        // Show the notification
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, send the notification
                sendNotification();
            } else {
                // Permission denied, handle accordingly (optional)
                // You might want to show a message that notification permission is required
            }
        }
    }

    // Create notification channel for Android O and above
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Accident Alert Channel";
            String description = "Channel for accident notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
