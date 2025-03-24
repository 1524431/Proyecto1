package com.example.proyect001.Cliente;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsCompat.Type;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.graphics.Insets;
import com.example.proyect001.R;

public class ListaCliente extends AppCompatActivity {

    FloatingActionButton btnAgregarClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cliente);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(Type.systemBars()); // Aquí estaba el problema
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        btnAgregarClient = findViewById(R.id.btnAgregarCliente);
        btnAgregarClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ListaCliente.this, "Hola Jóvenes, bienvenidos", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ListaCliente.this, AgregarCliente.class));
            }
        });
    }
}