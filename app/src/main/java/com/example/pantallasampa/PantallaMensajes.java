package com.example.pantallasampa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
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

public class PantallaMensajes extends AppCompatActivity {

    private ListView listViewCorreos;
    private List<Correo> listaCorreos;
    private DatabaseReference correosRef;
    private CardView cardViewEnviados;
    private CardView cardViewRecibidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_mensajes);

        listViewCorreos = findViewById(R.id.listviewmensajes);
        listaCorreos = new ArrayList<>();
        correosRef = FirebaseDatabase.getInstance().getReference().child("correos");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String correoUsuarioActual = currentUser.getEmail();

        // Obtener y mostrar los correos recibidos por el usuario actual
        Query query = correosRef.orderByChild("destinatario").equalTo(correoUsuarioActual);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCorreos.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Correo correo = snapshot.getValue(Correo.class);
                    listaCorreos.add(correo);
                }
                if (listaCorreos.isEmpty()) {
                    Toast.makeText(PantallaMensajes.this, "No tiene correos disponibles", Toast.LENGTH_SHORT).show();
                } else {
                    CorreoAdapter adapter = new CorreoAdapter(PantallaMensajes.this, listaCorreos);
                    listViewCorreos.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PantallaMensajes.this, "Error al leer los correos", Toast.LENGTH_SHORT).show();
            }
        });

        cardViewEnviados = findViewById(R.id.cardViewEnviados);
        cardViewRecibidos = findViewById(R.id.cardviewRecibido);

        // Manejar el clic del botón "Correos Enviados"
        cardViewEnviados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarCorreosEnviados();
                // Cambia el color de fondo del CardView
                cardViewEnviados.setBackgroundColor(getResources().getColor(R.color.color_cardview_alterado));
                cardViewRecibidos.setBackgroundColor(getResources().getColor(R.color.color_cardview));
            }
        });

        // Manejar el clic del botón "Correos Recibidos"
        cardViewRecibidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarCorreosRecibidos();
                // Cambia el color de fondo del CardView
                cardViewRecibidos.setBackgroundColor(getResources().getColor(R.color.color_cardview_alterado));
                cardViewEnviados.setBackgroundColor(getResources().getColor(R.color.color_cardview));
            }
        });
    }

    // Método para cargar los correos enviados por el usuario
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
                if (listaCorreos.isEmpty()) {
                    Toast.makeText(PantallaMensajes.this, "No ha enviado correos", Toast.LENGTH_SHORT).show();
                } else {
                    CorreoAdapter adapter = new CorreoAdapter(PantallaMensajes.this, listaCorreos);
                    listViewCorreos.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PantallaMensajes.this, "Error al leer los correos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para cargar los correos recibidos por el usuario
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
                if (listaCorreos.isEmpty()) {
                    Toast.makeText(PantallaMensajes.this, "No tiene correos disponibles", Toast.LENGTH_SHORT).show();
                } else {
                    CorreoAdapter adapter = new CorreoAdapter(PantallaMensajes.this, listaCorreos);
                    listViewCorreos.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PantallaMensajes.this, "Error al leer los correos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void redactarCorreo(View view) {
        Intent intent = new Intent(this, PantallaCorreo.class);
        startActivity(intent);
    }
}

