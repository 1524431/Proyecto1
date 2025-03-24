package com.example.proyect001.Datos;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.Pattern;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyect001.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatosActivity extends AppCompatActivity {

    EditText etNombre, etApellido, etFechaNacimiento, etEdad, etTelefono, etDomicilio;
    Button btnGuardarDatos;
    TextView tvNombre, tvUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);

        // Inicializar los campos
        etNombre = findViewById(R.id.etnombreMD);
        etApellido = findViewById(R.id.etapellidoMD);
        etFechaNacimiento = findViewById(R.id.etfechanacimientoMD);
        etEdad = findViewById(R.id.etedadMD);
        etTelefono = findViewById(R.id.etntelefonoMD);
        etDomicilio = findViewById(R.id.etdomicilioMD);
        btnGuardarDatos = findViewById(R.id.btnguardardatos);

        tvNombre = findViewById(R.id.tvNombre);
        tvUsuario = findViewById(R.id.tvUsuario);

        // Cargar los datos del usuario desde Firebase
        cargarDatos();




        // Guardar datos en Firebase
        btnGuardarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarDatos()) {
                    String nombre = etNombre.getText().toString().trim();
                    String apellido = etApellido.getText().toString().trim();
                    String fechaNacimiento = etFechaNacimiento.getText().toString().trim();
                    String edad = etEdad.getText().toString().trim();
                    String telefono = etTelefono.getText().toString().trim();
                    String domicilio = etDomicilio.getText().toString().trim();

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DatosUsuario usuario = new DatosUsuario(nombre, apellido, fechaNacimiento, edad, telefono, domicilio, uid);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Usuarios");

                    myRef.child(uid).setValue(usuario)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(DatosActivity.this, "Datos guardados exitosamente", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(DatosActivity.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    // Función para cargar los datos desde Firebase
    private void cargarDatos() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();  // Obtener el UID del usuario autenticado
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Usuarios").child(uid);  // Cambié "Desarrollador" por "Usuarios"

        myRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DatosUsuario usuario = task.getResult().getValue(DatosUsuario.class);
                if (usuario != null) {
                    etNombre.setText(usuario.getNombres());
                    etApellido.setText(usuario.getApellido());
                    etFechaNacimiento.setText(usuario.getFecha_nacimiento());
                    etEdad.setText(usuario.getEdad());
                    etTelefono.setText(usuario.getTelefono());
                    etDomicilio.setText(usuario.getDomicilio());
                    tvNombre.setText("Nombre: " + usuario.getNombres());
                    tvUsuario.setText("ID: " + usuario.getUid());
                } else {
                    Toast.makeText(DatosActivity.this, "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Si ocurre un error
                Toast.makeText(DatosActivity.this, "Error al cargar los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Función para validar los datos ingresados
    private boolean validarDatos() {
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String fechaNacimiento = etFechaNacimiento.getText().toString().trim();
        String edadStr = etEdad.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String domicilio = etDomicilio.getText().toString().trim();

        if (nombre.isEmpty()) {
            etNombre.setError("El nombre es obligatorio");
            return false;
        }

        if (apellido.isEmpty()) {
            etApellido.setError("El apellido es obligatorio");
            return false;
        }

        if (!Pattern.matches("^\\d{2}/\\d{2}/\\d{4}$", fechaNacimiento)) {
            etFechaNacimiento.setError("Formato incorrecto (DD/MM/AAAA)");
            return false;
        }

        if (edadStr.isEmpty() || !edadStr.matches("\\d+")) {
            etEdad.setError("Ingrese una edad válida");
            return false;
        }

        int edad = Integer.parseInt(edadStr);
        if (edad < 0 || edad > 120) {
            etEdad.setError("Edad fuera de rango (0-120)");
            return false;
        }

        if (!telefono.matches("\\d{9,}")) {
            etTelefono.setError("Teléfono inválido (mínimo 9 dígitos)");
            return false;
        }

        if (domicilio.isEmpty()) {
            etDomicilio.setError("El domicilio es obligatorio");
            return false;
        }

        return true;
    }

    // Clase que representa los datos del usuario
    public static class DatosUsuario {
        private String nombres;
        private String apellido;
        private String fecha_nacimiento;
        private String edad;
        private String telefono;
        private String domicilio;
        private String uid;

        public DatosUsuario() {
            // Constructor vacío necesario para Firebase
        }

        public DatosUsuario(String nombres, String apellido, String fecha_nacimiento, String edad,
                            String telefono, String domicilio, String uid) {
            this.nombres = nombres;
            this.apellido = apellido;
            this.fecha_nacimiento = fecha_nacimiento;
            this.edad = edad;
            this.telefono = telefono;
            this.domicilio = domicilio;
            this.uid = uid;
        }

        // Getters y Setters
        public String getNombres() { return nombres; }
        public void setNombres(String nombres) { this.nombres = nombres; }

        public String getApellido() { return apellido; }
        public void setApellido(String apellido) { this.apellido = apellido; }

        public String getFecha_nacimiento() { return fecha_nacimiento; }
        public void setFecha_nacimiento(String fecha_nacimiento) { this.fecha_nacimiento = fecha_nacimiento; }

        public String getEdad() { return edad; }
        public void setEdad(String edad) { this.edad = edad; }

        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }

        public String getDomicilio() { return domicilio; }
        public void setDomicilio(String domicilio) { this.domicilio = domicilio; }

        public String getUid() { return uid; }
        public void setUid(String uid) { this.uid = uid; }
    }
}
