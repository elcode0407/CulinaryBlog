package com.elcode.culinaryblog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeUser extends AppCompatActivity {


    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;
    EditText fullNameEditText, ageEditText;
    Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        fullNameEditText = findViewById(R.id.fullName);
        ageEditText = findViewById(R.id.age);
        userId = user.getUid();

        change = findViewById(R.id.changeUser);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = fullNameEditText.getText().toString().trim();
                String age = ageEditText.getText().toString().trim();
                reference.child("users").child(userId).child("fullName").setValue(fullName);
                reference.child("users").child(userId).child("age").setValue(age);
                Toast.makeText(ChangeUser.this, "All change was accept", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ChangeUser.this, MainActivity.class));

            }
        });

        reference.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    String fullName = userProfile.fullName;
                    String age = userProfile.age;

                    fullNameEditText.setText(fullName);
                    ageEditText.setText(age);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChangeUser.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

    }
}