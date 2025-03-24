package com.example.proyect001;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PrecargadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_precargado);

        Button loginButton = findViewById(R.id.button2);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(PrecargadoActivity.this, Dashboard3.class));
                finish();
            }, 3000);
        } else {
            loginButton.setVisibility(View.VISIBLE);
            loginButton.setOnClickListener(v -> {
                startActivity(new Intent(PrecargadoActivity.this, MainActivity.class));
                finish();
            });
        }
    }
}
