package com.example.pantallasampa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_correo);

        listViewCorreos = findViewById(R.id.listviewmensajes);
        listaCorreos = new ArrayList<>();
        correosRef = FirebaseDatabase.getInstance().getReference().child("correos");

        // Obtener el correo electrónico del usuario actual
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String correoUsuarioActual = currentUser.getEmail();

        // Obtener y mostrar los correos asociados al correo del usuario actual
        Query query = correosRef.orderByChild("destinatario").equalTo(correoUsuarioActual);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            // Dentro del ValueEventListener
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
                    // Adaptador del listView
                    CorreoAdapter adapter = new CorreoAdapter(PantallaMensajes.this, listaCorreos);
                    listViewCorreos.setAdapter(adapter);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores de lectura de la base de datos
                Toast.makeText(PantallaMensajes.this, "Error al leer los correos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Metodo para navegar a la pagina para redactar correo
    public void redactarCorreo(View view){

        Intent intent =  new Intent(this, PantallaCorreo.class);
        startActivity(intent);

    }

}