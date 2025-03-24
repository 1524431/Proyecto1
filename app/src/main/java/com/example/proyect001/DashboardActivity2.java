package com.example.proyect001;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity2 extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard2);

        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            String userName = user.getDisplayName();
            if (userName != null && !userName.isEmpty()) {
                welcomeTextView.setText("Bienvenido, " + userName + "!");
            } else {
                welcomeTextView.setText("Bienvenido!");
            }
        } else {
            welcomeTextView.setText("Bienvenido, Usuario no autenticado");
            Toast.makeText(this, "Por favor, inicie sesión", Toast.LENGTH_SHORT).show();
        }

        View rootView = findViewById(R.id.main);
        if (rootView == null) {
            throw new NullPointerException("El ID 'main' no existe en activity_dashboard2.xml. Verifica tu layout.");
        }

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button6 = findViewById(R.id.button6);

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cierreSesion();
            }
        });
    }

    private void cierreSesion() {
        firebaseAuth.signOut();
        startActivity(new Intent(DashboardActivity2.this, MainActivity.class));
        Toast.makeText(this, "Cerraste Sesión Exitosamente", Toast.LENGTH_SHORT).show();
        finish();
    }
}


