package com.example.esureapp;

import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Database {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Context context;

    public Database(Context context) {
        this.context = context;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public void register(String username, String email, String password, String pnumber) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            User newUser = new User(username, email, pnumber);
                            mDatabase.child("users").child(userId).setValue(newUser)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(context, "Register Success", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Database Error: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(context, "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void login(String email, String password, LoginCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure(task.getException().getMessage());
                    }
                });
    }

    public interface LoginCallback {
        void onSuccess();
        void onFailure(String message);
    }

    public static class User {
        public String username;
        public String email;
        public String phonenumber;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String username, String email, String phonenumber) {
            this.username = username;
            this.email = email;
            this.phonenumber = phonenumber;
        }
    }
}
