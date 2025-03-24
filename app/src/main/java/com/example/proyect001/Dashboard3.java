package com.example.proyect001;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.proyect001.Cliente.ListaCliente;
import com.example.proyect001.Datos.DatosActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class Dashboard3 extends AppCompatActivity {

    //Botones dashboard---------------------
    CardView cvEmpresa, cvGastos,cvTareas,cvListatareas,cvFavoritos,cvMisdatos,cvClientes;

    Button btncerrarsesion, btnDesarrollador;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    //Botones dashboard---------------------

    //private FirebaseAuth firebaseAuth;   se declaro arribaa
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private TextView usuarioNombreTextView;
    private TextView usuarioCodigoTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard3);

        usuarioNombreTextView = findViewById(R.id.usuarioNombreTextView);
        usuarioCodigoTextView = findViewById(R.id.usuarioCodigoTextView);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        //Botones dashboard ---------------------------
        cvEmpresa =findViewById(R.id.cvEmpresa);
        cvGastos =findViewById(R.id.cvGastos);
        cvTareas =findViewById(R.id.cvTareas);
        cvListatareas =findViewById(R.id.cvListatareas);
        cvFavoritos =findViewById(R.id.cvFavoritos);
        cvMisdatos =findViewById(R.id.cvMisdatos);
        cvClientes = findViewById(R.id.cvClientes); // Asegúrate de llamarlo en el código


        btncerrarsesion=findViewById(R.id.btnCerrarSesion);
        btnDesarrollador=findViewById(R.id.btndev);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUSer = firebaseAuth.getCurrentUser();
        //Botones dashboard --------------------


        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid(); // Obtener el UID del usuario
            obtenerNombreDesdeDatabase(userId);
            obtenerUsuario(); // Llamada para obtener el código del usuario
        } else {
            usuarioNombreTextView.setText("Usuario no autenticado");
            usuarioCodigoTextView.setText("Usuario no autenticado");
            Toast.makeText(this, "Por favor, inicie sesión", Toast.LENGTH_SHORT).show();
        }

        Button bCerrarSesion = findViewById(R.id.btnCerrarSesion);
        if (bCerrarSesion != null) {
            bCerrarSesion.setOnClickListener(v -> cierreSesion());
        } else {
            Toast.makeText(this, "Error: btnCerrarSesion no encontrado en el layout", Toast.LENGTH_SHORT).show();
        }

        cvMisdatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir la actividad DatosActivity
                Intent intent = new Intent(Dashboard3.this, DatosActivity.class);
                startActivity(intent);
            }
        });

        cvClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard3.this, ListaCliente.class);
                startActivity(intent);
            }
        });
    }

    private void obtenerNombreDesdeDatabase(String userId) {
        userRef = database.getReference("Usuarios").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FirebaseData", "Datos obtenidos de Firebase: " + dataSnapshot.getValue());

                if (dataSnapshot.exists()) {
                    String nombreUsuario = dataSnapshot.child("nombres").getValue(String.class);

                    if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
                        usuarioNombreTextView.setText("Usuario Nombre: " + nombreUsuario);
                    } else {
                        usuarioNombreTextView.setText("Usuario Nombre: No disponible");
                    }
                } else {
                    usuarioNombreTextView.setText("Usuario Nombre: No encontrado");
                    Log.d("FirebaseData", "No se encontró el nodo del usuario en la base de datos.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                usuarioNombreTextView.setText("Error al obtener nombre");
                Toast.makeText(Dashboard3.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                Log.e("FirebaseError", "Error al obtener datos: " + databaseError.getMessage());
            }
        });
    }

    private void obtenerUsuario() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            // Verifica que el UID tiene al menos 3 caracteres
            if (userId != null && userId.length() >= 3) {
                String primerosTresCaracteres = userId.substring(0, 3);  // Extrae los primeros 3 caracteres
                usuarioCodigoTextView.setText("Código Usuario: " + primerosTresCaracteres);
            } else {
                usuarioCodigoTextView.setText("Código Usuario: No disponible");
            }
        } else {
            usuarioCodigoTextView.setText("Usuario no autenticado");
        }
    }








    private void cierreSesion() {
        firebaseAuth.signOut();
        Toast.makeText(this, "Cerraste Sesión Exitosamente", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Dashboard3.this, MainActivity.class));
        finish();
    }
}