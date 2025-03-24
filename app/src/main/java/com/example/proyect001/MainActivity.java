package com.example.proyect001;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.FirebaseApp;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.textfield.TextInputEditText;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button buttonLogin = findViewById(R.id.button);
        Button buttonRegister = findViewById(R.id.button3);

        buttonLogin.setOnClickListener(v -> {
            String email = ((TextInputEditText) findViewById(R.id.textInputEditText)).getText().toString().trim();
            String password = ((TextInputEditText) findViewById(R.id.textInputEditText2)).getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(MainActivity.this, "Ingrese su correo electrónico", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(email, password);
        });


        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Registro1Activity.class);
            startActivity(intent);
        });
    }


    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(MainActivity.this, "Bienvenido " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, Dashboard3.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    } else {
                        String errorMessage = task.getException().getMessage();

                        if (errorMessage != null) {
                            if (errorMessage.contains("no user record")) {
                                Toast.makeText(MainActivity.this, "Usuario no registrado. Cree una cuenta.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(MainActivity.this, Registro1Activity.class));
                            } else if (errorMessage.contains("password is invalid")) {
                                Toast.makeText(MainActivity.this, "Contraseña incorrecta.", Toast.LENGTH_LONG).show();
                            } else if (errorMessage.contains("email address is badly formatted")) {
                                Toast.makeText(MainActivity.this, "Formato de correo inválido.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error desconocido.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
