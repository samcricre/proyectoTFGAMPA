package com.example.pantallasampa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PaginaSorteo extends AppCompatActivity {

    private ListView listViewSorteos;
    private List<Sorteos> listaSorteos;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_sorteo);

        listViewSorteos = findViewById(R.id.listviewmensajes);
        listaSorteos = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listViewSorteos.setAdapter(arrayAdapter);

        // Obtener referencia a la base de datos de Firebase
        DatabaseReference sorteosRef = FirebaseDatabase.getInstance().getReference().child("sorteos");

        // Escuchar cambios en los sorteos
        sorteosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaSorteos.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Sorteos sorteo = snapshot.getValue(Sorteos.class);
                    listaSorteos.add(sorteo);
                }
                actualizarListaSorteos();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de lectura de la base de datos
            }
        });

        // Obtener referencia de la CardView "volver"
        View volverCardView = findViewById(R.id.volver);

        // Establecer OnClickListener para la CardView "volver"
        volverCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Obtener referencia de la CardView para navegar a otra página
        View otraPaginaCardView = findViewById(R.id.crearSorteo);

        // Establecer OnClickListener para la CardView "otraPaginaCardView"
        otraPaginaCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí iniciamos una nueva actividad para navegar a otra página
                Intent intent = new Intent(PaginaSorteo.this, PantallaCrearSorteo.class);
                startActivity(intent);
            }
        });
    }

    private void actualizarListaSorteos() {
        if (listaSorteos.isEmpty()) {
            // Si no hay sorteos, mostrar un mensaje con un Toast
            Toast.makeText(this, "No hay sorteos disponibles en este momento", Toast.LENGTH_SHORT).show();
        } else {
            // Si hay sorteos, mostrar la lista
            arrayAdapter.clear();
            for (Sorteos sorteo : listaSorteos) {
                arrayAdapter.add(sorteo.getNombre());
            }
            arrayAdapter.notifyDataSetChanged();
        }
    }
}
