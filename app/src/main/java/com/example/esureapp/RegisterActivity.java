package com.example.esureapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText edemail, edusername, edpassword, edconfirmpassword, edpnumber;
    Button btn;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        edemail = findViewById(R.id.editTextEmail);
        edusername = findViewById(R.id.editTextLoginUsername);
        edpnumber = findViewById(R.id.editTextPhoneNumber);
        edpassword = findViewById(R.id.editTextLoginPassword);
        edconfirmpassword = findViewById(R.id.editTextConfirmPassword);
        btn = findViewById(R.id.buttonRegister);
        tv = findViewById(R.id.textViewExistingUser);
        tv.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
        btn.setOnClickListener(v -> {
            String username = edusername.getText().toString();
            String email = edemail.getText().toString();
            String pnumber = edpnumber.getText().toString();
            String password = edpassword.getText().toString();
            String confirm = edconfirmpassword.getText().toString();
            Database db = new Database(getApplicationContext());
            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || confirm.isEmpty() || pnumber.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Fill in the Details", Toast.LENGTH_SHORT).show();
            } else {
                if (password.compareTo(confirm) == 0) {
                    if (isValid(password)) {
                        db.register(username, email, password, pnumber);
                    } else {
                        Toast.makeText(getApplicationContext(), "Password must contain at least 8 characters and numbers", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Password didn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static boolean isValid(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasLetter = false;
        boolean hasNumber = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            }
            if (hasLetter && hasNumber) {
                break;
            }
        }
        return hasLetter && hasNumber;
    }
}
