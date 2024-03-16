package com.example.pantallasampa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PantallaMensajesAdmin extends AppCompatActivity {

    private ListView listViewCorreos;
    private List<Correo> listaCorreos;
    private DatabaseReference correosRef;
    private CardView cardViewEnviados;
    private CardView cardViewRecibidos;
    private TextView modo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_mensajes_admin);

        listViewCorreos = findViewById(R.id.listviewmensajes);
        listaCorreos = new ArrayList<>();
        correosRef = FirebaseDatabase.getInstance().getReference().child("correos");
        modo = findViewById(R.id.estadoMensaje);
        modo.setText("Correos Recibidos");

        // Mostrar los correos recibidos al iniciar la actividad
        cargarCorreosRecibidos();

        cardViewEnviados = findViewById(R.id.cardViewEnviados);
        cardViewRecibidos = findViewById(R.id.cardviewRecibido);

        // Configurar los listeners para los botones de acción
        cardViewEnviados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarCorreosEnviados();
                modo.setText("Correos Enviados");
            }
        });

        cardViewRecibidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarCorreosRecibidos();
                modo.setText("Correos Recibidos");
            }
        });
    }

    // Método para mostrar los correos en el ListView
    private void mostrarCorreos(List<Correo> correos) {
        if (correos.isEmpty()) {
            Toast.makeText(PantallaMensajesAdmin.this, "No hay correos disponibles", Toast.LENGTH_SHORT).show();
        } else {
            CorreoAdapter adapter = new CorreoAdapter(PantallaMensajesAdmin.this, correos);
            listViewCorreos.setAdapter(adapter);
        }
    }

    // Método para cargar los correos enviados
    private void cargarCorreosEnviados() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String correoUsuarioActual = currentUser.getEmail();

        Query query = correosRef.orderByChild("remitente").equalTo(correoUsuarioActual);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCorreos.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Correo correo = snapshot.getValue(Correo.class);
                    listaCorreos.add(correo);
                }
                mostrarCorreos(listaCorreos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PantallaMensajesAdmin.this, "Error al leer los correos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para cargar los correos recibidos
    private void cargarCorreosRecibidos() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String correoUsuarioActual = currentUser.getEmail();

        Query query = correosRef.orderByChild("destinatario").equalTo(correoUsuarioActual);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCorreos.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Correo correo = snapshot.getValue(Correo.class);
                    listaCorreos.add(correo);
                }
                mostrarCorreos(listaCorreos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PantallaMensajesAdmin.this, "Error al leer los correos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para redactar un nuevo correo
    public void redactarCorreo(View view) {
        Intent intent = new Intent(this, PantallaCrearCorreoAdmin.class);
        startActivity(intent);
    }

    // Método para navegar a otra pantalla (opcional)
    public void nav(View view) {
        Intent intent = new Intent(this, PantallaCorreo.class);
        startActivity(intent);
    }
}
