package com.example.esureapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.esureapp.databinding.ActivityLoadingBinding;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loadingActivity extends AppCompatActivity implements View.OnClickListener{
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    ActivityLoadingBinding binding;
    MaterialButton button1, button2, button3, button4, button5, button6, button7, button8, button9, button0, buttonback;
    TextView inputloading;
    Button buttonLogin;
    EditText usernameinput;
    int input = 0;
    String name;

    @Override
    public void onClick(View v) {
        MaterialButton button = (MaterialButton) v;
        String buttonText = button.getText().toString();

        try {
            // Append the clicked number to the inputloading (integer)
            int buttonValue = Integer.parseInt(buttonText);
            input = input * 10 + buttonValue;  // Shift left and add new number
        } catch (NumberFormatException e) {
            // Handle "B" button (backspace/clear)
            if (buttonText.equals("B")) {
                input = 0; // Reset input to zero
            }
        }

        // Display the updated integer input as a string in the TextView
        inputloading.setText(String.valueOf(input));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loading);
        inputloading = findViewById(R.id.inputloading);
        buttonLogin  = findViewById(R.id.buttonLogin);
        usernameinput = findViewById(R.id.usernameinput);

        assignId(button0,R.id.button0);
        assignId(button1,R.id.button1);
        assignId(button2,R.id.button2);
        assignId(button3,R.id.button3);
        assignId(button4,R.id.button4);
        assignId(button5,R.id.button5);
        assignId(button6,R.id.button6);
        assignId(button7,R.id.button7);
        assignId(button8,R.id.button8);
        assignId(button9,R.id.button9);
        assignId(buttonback, R.id.buttonback);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("RFIDUsersFirebase");

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserAmount();
            }
        });
    }

    private void updateUserAmount() {
        final String username = usernameinput.getText().toString().trim();
        final int amountToAdd = input;  // Use the integer value now

        if (username.isEmpty() || amountToAdd == 0) {
            Toast.makeText(this, "Please enter username and amount", Toast.LENGTH_SHORT).show();
            return;
        }

        reference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User found, update amount
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // Fetch the current amount from Firebase
                        Double currentAmountValue = 0.0; // Use Double since Firebase stores numbers as doubles
                        Object currentAmountObj = userSnapshot.child("amount").getValue();

                        if (currentAmountObj != null) {
                            currentAmountValue = Double.parseDouble(currentAmountObj.toString());
                        }

                        // Add the new amount
                        double newAmountValue = currentAmountValue + amountToAdd;

                        // Store the updated amount as a number (double) in Firebase
                        userSnapshot.getRef().child("amount").setValue(newAmountValue);
                    }
                    Toast.makeText(loadingActivity.this, "Amount updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // User not found
                    Toast.makeText(loadingActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(loadingActivity.this, "Failed to update amount: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    void assignId(MaterialButton btn, int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }
}
