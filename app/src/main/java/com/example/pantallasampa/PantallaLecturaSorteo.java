package com.example.pantallasampa;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

        // Obtener el ID del sorteo de la actividad anterior
        String noticiaId = getIntent().getStringExtra("codigoNoticia");

        // Obtener referencia de la base de datos de Firebase
        DatabaseReference sorteosRef = FirebaseDatabase.getInstance().getReference().child("sorteos");

        // Escuchar cambios en el sorteo
        sorteosRef.child(noticiaId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores de lectura de la base de datos
            }
        });

        // Agregar OnClickListener al botón para seleccionar el ganador
        seleccionarGanadorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar si el sorteo está activo
                sorteosRef.child(noticiaId).child("finalizado").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean finalizado = dataSnapshot.getValue(Boolean.class);
                        if (!finalizado) {
                            // Leer los participantes directamente de la base de datos
                            DatabaseReference participantesRef = sorteosRef.child(noticiaId).child("participantes");
                            participantesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        // Obtener la lista de participantes
                                        ArrayList<String> participantes = new ArrayList<>();
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            String email = dataSnapshot.child("email").getValue(String.class);
                                            if (email != null && !participantes.contains(email)) {
                                                participantes.add(email);
                                            }
                                        }
                                        // Verificar si hay al menos un participante
                                        if (!participantes.isEmpty()) {
                                            // Seleccionar aleatoriamente un ganador
                                            Random random = new Random();
                                            int index = random.nextInt(participantes.size());
                                            String ganador = participantes.get(index);

                                            // Actualizar el ganador en la base de datos
                                            sorteosRef.child(noticiaId).child("ganador").setValue(ganador);
                                            sorteosRef.child(noticiaId).child("finalizado").setValue(true);

                                            // Actualizar la vista con el nuevo ganador y deshabilitar el botón
                                            ganadorSorteoTextView.setText(ganador);
                                            seleccionarGanadorButton.setEnabled(false);
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
                        } else {
                            Toast.makeText(getApplicationContext(), "El sorteo ya ha finalizado", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Manejar errores de lectura de la base de datos
                    }
                });
            }
        });
    }
}
