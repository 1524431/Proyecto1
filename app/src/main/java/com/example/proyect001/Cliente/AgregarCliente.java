package com.example.proyect001.Cliente;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.proyect001.R;

public class AgregarCliente extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference BD_Clientes;

    private TextView uid_usuario_I;
    private EditText nombresCli, apellidosCli, correoCli, dniCli, telefonoCli, direccionCli;
    private Button btnguardarcliente;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cliente);

        // Inicializar FirebaseAuth y obtener usuario autenticado
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        // Si el usuario no está autenticado, salir
        if (firebaseUser == null) {
            Toast.makeText(this, "Debes iniciar sesión para agregar clientes", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar variables
        inicializarVariables();
    }

    private void inicializarVariables() {
        uid_usuario_I = findViewById(R.id.tvidCliente);
        nombresCli = findViewById(R.id.tvnombrel);
        apellidosCli = findViewById(R.id.tvapellidol);
        correoCli = findViewById(R.id.tvcorreoclientel);
        dniCli = findViewById(R.id.tvdniclientel);
        telefonoCli = findViewById(R.id.tvtelefonoclientel);
        direccionCli = findViewById(R.id.tvdireccionclientel);
        btnguardarcliente = findViewById(R.id.btnguardarcliente);

        // Mostrar UID del usuario autenticado
        uid_usuario_I.setText("UID Usuario: " + firebaseUser.getUid());

        // Base de datos
        BD_Clientes = FirebaseDatabase.getInstance().getReference();

        // Listener del botón
        btnguardarcliente.setOnClickListener(v -> agregarCliente());
    }

    private void agregarCliente() {
        String uid = firebaseUser.getUid();
        DatabaseReference clientesRef = BD_Clientes.child("Usuarios").child(uid).child("Clientes");
        String id_cliente = clientesRef.push().getKey();

        if (id_cliente == null) {
            Toast.makeText(this, "Error al generar ID del cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener datos del cliente
        String nombres = nombresCli.getText().toString().trim();
        String apellidos = apellidosCli.getText().toString().trim();
        String correo = correoCli.getText().toString().trim();
        String dni = dniCli.getText().toString().trim();
        String telefono = telefonoCli.getText().toString().trim();
        String direccion = direccionCli.getText().toString().trim();

        // Validaciones
        if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || dni.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto Cliente
        Cliente cliente = new Cliente(id_cliente, uid, nombres, apellidos, correo, dni, telefono, direccion);

        // Guardar en Firebase
        clientesRef.child(id_cliente).setValue(cliente)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Cliente agregado correctamente", Toast.LENGTH_SHORT).show();
                    finish();  // Cierra la actividad después de guardar
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    public static class Cliente {
        private String id_cliente;
        private String uid;
        private String nombres;
        private String apellidos;
        private String correo;
        private String dni;
        private String telefono;
        private String direccion;

        public Cliente() {}

        public Cliente(String id_cliente, String uid, String nombres, String apellidos,
                       String correo, String dni, String telefono, String direccion) {
            this.id_cliente = id_cliente;
            this.uid = uid;
            this.nombres = nombres;
            this.apellidos = apellidos;
            this.correo = correo;
            this.dni = dni;
            this.telefono = telefono;
            this.direccion = direccion;
        }

        // Getters y Setters
        public String getId_cliente() { return id_cliente; }
        public void setId_cliente(String id_cliente) { this.id_cliente = id_cliente; }
        public String getUid() { return uid; }
        public void setUid(String uid) { this.uid = uid; }
        public String getNombres() { return nombres; }
        public void setNombres(String nombres) { this.nombres = nombres; }
        public String getApellidos() { return apellidos; }
        public void setApellidos(String apellidos) { this.apellidos = apellidos; }
        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }
        public String getDni() { return dni; }
        public void setDni(String dni) { this.dni = dni; }
        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }
        public String getDireccion() { return direccion; }
        public void setDireccion(String direccion) { this.direccion = direccion; }
    }
}
