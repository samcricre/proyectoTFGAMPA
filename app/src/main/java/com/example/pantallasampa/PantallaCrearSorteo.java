package com.example.pantallasampa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PantallaCrearSorteo extends AppCompatActivity {

    private EditText nombreSorteoEditText, descripcionSorteoEditText, premiosSorteoEditText;
    private Button btCrearSorteo;

    private DatabaseReference sorteosRef, noticiasRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_crear_sorteo);

        // Inicializar Firebase Database
        sorteosRef = FirebaseDatabase.getInstance().getReference().child("sorteos");
        noticiasRef = FirebaseDatabase.getInstance().getReference().child("noticias");

        // Inicializar vistas
        nombreSorteoEditText = findViewById(R.id.nombreSorteo);
        descripcionSorteoEditText = findViewById(R.id.descripcionSorteo);
        premiosSorteoEditText = findViewById(R.id.premiosSorteo);
        btCrearSorteo = findViewById(R.id.btCrearSorteo);

        // Configurar el botón para crear un sorteo
        btCrearSorteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearSorteoYNoticia();
            }
        });
    }

    private void crearSorteoYNoticia() {
        // Obtener los valores de los campos
        String nombreSorteo = nombreSorteoEditText.getText().toString().trim();
        String descripcionSorteo = descripcionSorteoEditText.getText().toString().trim();
        String premiosSorteo = premiosSorteoEditText.getText().toString().trim();

        // Verificar si los campos están vacíos
        if (nombreSorteo.isEmpty() || descripcionSorteo.isEmpty() || premiosSorteo.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el usuario actualmente autenticado
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String creador = user.getEmail();

            // Crear un objeto Sorteo con los datos ingresados
            Sorteos sorteo = new Sorteos(nombreSorteo, descripcionSorteo, premiosSorteo, creador, false, "");

            // Guardar el sorteo en la base de datos de Firebase
            sorteosRef.push().setValue(sorteo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Crear una noticia con los datos del sorteo y el id del sorteo
                            String mensajeNoticia = "Nuevo sorteo disponible: " + nombreSorteo;
                            Noticia noticia = new Noticia(nombreSorteo, descripcionSorteo, mensajeNoticia, 0);

                            // Guardar la noticia en la base de datos de Firebase
                            noticiasRef.push().setValue(noticia)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Obtener la clave de la noticia recién creada
                                            String noticiaKey = noticiasRef.getKey();
                                            // Asignar la clave de la noticia al atributo correspondiente del sorteo
                                            sorteosRef.child(noticiaKey).child("codigoNoticia").setValue(noticiaKey)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(PantallaCrearSorteo.this, "Sorteo y noticia creados correctamente", Toast.LENGTH_SHORT).show();
                                                            // Limpiar los campos después de crear el sorteo
                                                            nombreSorteoEditText.setText("");
                                                            descripcionSorteoEditText.setText("");
                                                            premiosSorteoEditText.setText("");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(PantallaCrearSorteo.this, "Error al asignar la clave de la noticia al sorteo", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(PantallaCrearSorteo.this, "Error al crear la noticia", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PantallaCrearSorteo.this, "Error al crear el sorteo", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }
}
