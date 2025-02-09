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

public class LoginActivity extends AppCompatActivity {
    EditText edemail, edpassword;
    Button btn;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        edemail = findViewById(R.id.editTextLoginUsername);
        edpassword = findViewById(R.id.editTextLoginPassword);
        btn = findViewById(R.id.buttonLogin);
        tv = findViewById(R.id.textViewUser);
        tv.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
        btn.setOnClickListener(v -> {
            String email = edemail.getText().toString();
            String password = edpassword.getText().toString();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Fill in the Details", Toast.LENGTH_SHORT).show();
            } else {
                Database db = new Database(getApplicationContext());
                db.login(email, password, new Database.LoginCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getApplicationContext(), "Login Failed: " + message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
