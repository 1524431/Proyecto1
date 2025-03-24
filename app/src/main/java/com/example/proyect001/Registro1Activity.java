package com.example.proyect001;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registro1Activity extends AppCompatActivity {
    EditText etnombre, etapellido, etcorreo, etpassword, etconfirmar_password;
    Button btnregister;
    TextView lblvolverlogin;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    // Variables para almacenar los datos del usuario
    String nombre, apellidos, correo, password, confirmarpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro1);

        // Referencias a las vistas
        etnombre = findViewById(R.id.etnombre);
        etapellido = findViewById(R.id.etapellido);
        etcorreo = findViewById(R.id.etcorreo);
        etpassword = findViewById(R.id.etpassword);
        etconfirmar_password = findViewById(R.id.etconfirmar_password);
        btnregister = findViewById(R.id.btnregistrar);
        lblvolverlogin = findViewById(R.id.btnvolverlogin);

        // Inicialización de FirebaseAuth y ProgressDialog
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Registro1Activity.this);
        progressDialog.setTitle("Espere porfavor...");
        progressDialog.setCanceledOnTouchOutside(false);

        // Redirigir a MainActivity cuando el usuario haga clic en el botón de volver
        Button VolverButton = findViewById(R.id.btnvolverlogin);
        VolverButton.setOnClickListener(v -> {
            startActivity(new Intent(Registro1Activity.this, MainActivity.class));
            finish();
        });

        // Acción del botón de registrar
        btnregister.setOnClickListener(v -> validarDatos());
    }

    // Método para validar los datos de entrada
    private void validarDatos() {
        nombre = etnombre.getText().toString().trim();
        apellidos = etapellido.getText().toString().trim();
        correo = etcorreo.getText().toString().trim();
        password = etpassword.getText().toString().trim();
        confirmarpassword = etconfirmar_password.getText().toString().trim();

        // Validación de campos
        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(this, "El campo nombre está vacío", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(apellidos)) {
            Toast.makeText(this, "El campo apellido está vacío", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(correo) || !Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Por favor ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "El campo contraseña está vacío", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirmarpassword)) {
            Toast.makeText(this, "El campo confirmar contraseña está vacío", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmarpassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        } else {
            // Ahora llama a guardarUsuario() pasando el uid obtenido
            firebaseAuth.createUserWithEmailAndPassword(correo, password)
                    .addOnSuccessListener(authResult -> {
                        String uid = firebaseAuth.getCurrentUser().getUid();
                        guardarUsuario(uid); // Aquí pasamos el uid como parámetro
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(Registro1Activity.this, "Error al registrar usuario: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        }
    }



    private void guardarUsuario(String uid) {
        Log.d("FirebaseDatabase", "Intentando guardar los datos del usuario en Firebase con UID: " + uid);

        progressDialog.setMessage("Guardando información");
        progressDialog.show();

        // Datos del usuario a guardar
        HashMap<String, String> datosusuario = new HashMap<>();
        datosusuario.put("uid", uid);
        datosusuario.put("nombres", nombre);
        datosusuario.put("apellido", apellidos);
        datosusuario.put("correo", correo);
        datosusuario.put("password", password);
        datosusuario.put("fecha_nacimiento", "");
        datosusuario.put("edad", "");
        datosusuario.put("telefono", "");
        datosusuario.put("domicilio", "");
        datosusuario.put("tiktok", "");

        // Guardar los datos del usuario en Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.child(uid).setValue(datosusuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("FirebaseDatabase", "Datos del usuario guardados exitosamente en la base de datos.");
                        progressDialog.dismiss();
                        Toast.makeText(Registro1Activity.this, "Usuario Creado Con Éxito", Toast.LENGTH_SHORT).show();

                        // Redirigir a la pantalla de login
                        Intent intent = new Intent(Registro1Activity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpia las actividades previas
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("FirebaseDatabaseError", "Error al guardar los datos del usuario: " + e.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(Registro1Activity.this, "Error al guardar los datos del usuario", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}