package com.example.pantallasampa;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class PantallaLecturaSorteo extends AppCompatActivity {

    private TextView nombreSorteoTextView;
    private TextView premiosSorteoTextView;
    private TextView descripcionSorteoTextView;
    private TextView ganadorSorteoTextView;
    private TextView creadorSorteoTextView;
    private ImageView seleccionarGanadorButton;
    private ListView listaParticipantes; // Movido para estar disponible en toda la clase

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_lectura_sorteo);

        // Inicializar vistas
        nombreSorteoTextView = findViewById(R.id.tituloSorteo);
        premiosSorteoTextView = findViewById(R.id.premiosSorteo);
        descripcionSorteoTextView = findViewById(R.id.descripcionSorteo);
        ganadorSorteoTextView = findViewById(R.id.ganadorSorteo);
        creadorSorteoTextView = findViewById(R.id.creadorSorteo);
        seleccionarGanadorButton = findViewById(R.id.createButtonCorreo);
        listaParticipantes = findViewById(R.id.listViewParticipantes); // Inicializado aquí

        // Obtener el ID del sorteo de la actividad anterior
        String noticiaId = getIntent().getStringExtra("codigoNoticia");

        // Obtener referencia de la base de datos de Firebase
        DatabaseReference sorteosRef = FirebaseDatabase.getInstance().getReference().child("sorteos");

        // Consultar el sorteo con el código de noticia específico
        Query query = sorteosRef.orderByChild("codigoNoticia").equalTo(noticiaId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Obtener el objeto Sorteo
                        Sorteos sorteo = snapshot.getValue(Sorteos.class);
                        if (sorteo != null) {
                            // Establecer los datos en las vistas
                            nombreSorteoTextView.setText(sorteo.getNombre());
                            premiosSorteoTextView.setText(sorteo.getPremios());
                            descripcionSorteoTextView.setText(sorteo.getDescripcion());
                            ganadorSorteoTextView.setText(sorteo.getGanador());
                            creadorSorteoTextView.setText(sorteo.getCreador());

                            // Habilitar o deshabilitar el botón según el estado del sorteo
                            boolean finalizado = sorteo.isFinalizado();
                            seleccionarGanadorButton.setEnabled(!finalizado);
                        }
                    }
                } else {
                    // No se encontró ningún sorteo con el código de noticia específico
                    Toast.makeText(getApplicationContext(), "No se encontró ningún sorteo con ese código de noticia", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores de lectura de la base de datos
            }
        });

        // Agregar OnClickListener al botón para seleccionar un ganador aleatorio
        seleccionarGanadorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatabaseReference participantesRef = sorteosRef.child(noticiaId).child("participantes");
                participantesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            ArrayList<String> participantes = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String participante = dataSnapshot.getValue(String.class);
                                if (participante != null && !participantes.contains(participante)) {
                                    participantes.add(participante);
                                }
                            }
                            if (!participantes.isEmpty()) {
                                Random random = new Random();
                                int index = random.nextInt(participantes.size());
                                String ganador = participantes.get(index);

                                // Actualizar el ganador en la base de datos
                                sorteosRef.child(noticiaId).child("ganador").setValue(ganador);
                                sorteosRef.child(noticiaId).child("finalizado").setValue(true);

                                // Actualizar la vista con el nuevo ganador y deshabilitar el botón
                                ganadorSorteoTextView.setText(ganador);
                                seleccionarGanadorButton.setEnabled(false); // Deshabilitar el botón después de elegir un ganador
                            } else {
                                Toast.makeText(getApplicationContext(), "No hay participantes para elegir un ganador", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "No hay participantes para elegir un ganador", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Manejar errores de lectura de la base de datos
                    }
                });
            }
        });

        // Obtener referencia de la lista de participantes en la base de datos
        DatabaseReference participantesRef = sorteosRef.child(noticiaId).child("participantes");

        // Leer los participantes de la base de datos y llenar la lista
        participantesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> participantes = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String email = dataSnapshot.getValue(String.class);
                        if (email != null) {
                            participantes.add(email);
                        }
                    }
                } else {
                    // Si no hay participantes, mostrar un mensaje o tomar alguna acción
                    Toast.makeText(getApplicationContext(), "No hay participantes registrados", Toast.LENGTH_SHORT).show();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, participantes);
                listaParticipantes.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores de lectura de la base de datos
            }
        });
    }
}
