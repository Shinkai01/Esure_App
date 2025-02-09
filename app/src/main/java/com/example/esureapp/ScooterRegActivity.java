package com.example.esureapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.esureapp.databinding.ActivityScooterRegBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ScooterRegActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    ActivityScooterRegBinding binding;
    String rfid, name, email, phone_number, password, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScooterRegBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("RFIDChecker").child("USER");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String rfidData = snapshot.getValue(String.class);
                    binding.editTextPhone.setText(rfidData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", error.getMessage());
            }
        });

        binding.buttonscooterreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rfid = binding.editTextPhone.getText().toString();
                name = binding.editTextText2.getText().toString();
                email = binding.editTextPhone2.getText().toString();
                phone_number = binding.editTextTextEmailAddress.getText().toString();
                password = binding.editTextTextPassword.getText().toString();
                username = binding.username1.getText().toString();

                if (name.isEmpty() || rfid.isEmpty() || phone_number.isEmpty() || email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                checkIfUsernameExists(username, new UsernameCheckCallback() {
                    @Override
                    public void onUsernameCheckComplete(boolean exists) {
                        if (exists) {
                            Toast.makeText(getApplicationContext(), "Username already exists. Please choose another one.", Toast.LENGTH_SHORT).show();
                        } else {
                            registerUser();
                        }
                    }
                });
            }
        });
    }

    private void checkIfUsernameExists(final String username, final UsernameCheckCallback callback) {
        DatabaseReference usersRef = firebaseDatabase.getReference("RFIDUsersFirebase");
        usersRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callback.onUsernameCheckComplete(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getMessage());
                callback.onUsernameCheckComplete(false);
            }
        });
    }

    private void registerUser() {
        RFIDUsersFirebase firebaseDatabase1 = new RFIDUsersFirebase(rfid, name, phone_number, email, password, username);
        reference = firebaseDatabase.getReference("RFIDUsersFirebase");
        reference.child(rfid).setValue(firebaseDatabase1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    binding.editTextPhone.setText("");
                    binding.editTextText2.setText("");
                    binding.editTextPhone2.setText("");
                    binding.editTextTextEmailAddress.setText("");
                    binding.editTextTextPassword.setText("");
                    binding.username1.setText("");
                    Toast.makeText(getApplicationContext(), "Register Success", Toast.LENGTH_SHORT).show();
                } else {
                    String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                    Log.e("RegistrationError", errorMessage);
                    Toast.makeText(getApplicationContext(), "Registration Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    interface UsernameCheckCallback {
        void onUsernameCheckComplete(boolean exists);
    }
}
