package com.example.pantallasampa;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PantallaLecturaCorreo extends AppCompatActivity {

    private String correoId;
    private DatabaseReference correosRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_lectura_correo);

        // Obtener referencias a los elementos de la interfaz de usuario
        TextView asuntoTextView = findViewById(R.id.asuntoLectura);
        TextView emisorTextView = findViewById(R.id.emisor);
        TextView remitenteTextView = findViewById(R.id.remitente);
        TextView mensajeTextView = findViewById(R.id.mensaje);
        ImageView backButton = findViewById(R.id.backButtonCorreo);
        ImageView deleteButton = findViewById(R.id.deleteButtonCorreo);

        // Obtener los datos del intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String asunto = extras.getString("asunto");
            String emisor = extras.getString("destinatario");
            String remitente = extras.getString("remitente");
            String mensaje = extras.getString("contenido");
            correoId = extras.getString("correoId");

            // Establecer los datos en los elementos de la interfaz de usuario
            asuntoTextView.setText(asunto);
            emisorTextView.setText(emisor);
            remitenteTextView.setText(remitente);
            mensajeTextView.setText(mensaje);
        }

        // Obtener la referencia a la base de datos de Firebase
        correosRef = FirebaseDatabase.getInstance().getReference().child("correos");

        // Agregar listener al botón de regresar
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Finaliza la actividad actual y vuelve a la anterior
            }
        });

        // Agregar listener al botón de eliminar
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarCorreo();
            }
        });
    }

    private void eliminarCorreo() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // El usuario no está autenticado
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        final String usuarioActual = currentUser.getEmail();

        correosRef.child(correoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Correo correo = dataSnapshot.getValue(Correo.class);
                    if (correo != null) {
                        // Verificar si el usuario actual es el remitente o el destinatario del correo
                        if (correo.getRemitente().equals(usuarioActual)) {
                            // El usuario actual es el remitente del correo
                            correo.setEliminadoRemitente(true);
                        } else if (correo.getDestinatario().equals(usuarioActual)) {
                            // El usuario actual es el destinatario del correo
                            correo.setEliminado(true);
                        }
                        // Actualizar el correo en la base de datos Firebase
                        dataSnapshot.getRef().setValue(correo);
                        Toast.makeText(PantallaLecturaCorreo.this, "Correo eliminado", Toast.LENGTH_SHORT).show();
                        finish(); // Finaliza la actividad actual
                    } else {
                        Toast.makeText(PantallaLecturaCorreo.this, "No se encontró el correo", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PantallaLecturaCorreo.this, "Error al eliminar el correo", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
