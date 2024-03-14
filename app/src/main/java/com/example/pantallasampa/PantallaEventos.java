package com.example.pantallasampa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PantallaEventos extends AppCompatActivity {
    private ImageView email;
    private ImageView news;
    private ImageView event;
    private ImageView profile;
    private String emailUser;
    private DatabaseReference dr;
    private List<String> hijos;
    private Spinner selecHijo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_eventos);


        emailUser = getIntent().getStringExtra("emailUser");
        dr = FirebaseDatabase.getInstance().getReference();

        news = findViewById(R.id.goNews);
        event = findViewById(R.id.goEvent);
        profile = findViewById(R.id.goProfile);
        email = findViewById(R.id.goEmail);

        hijos = new ArrayList<>();
        selecHijo = findViewById(R.id.spinnerSelectHijos);

        //Una vez que se entra en la página se utiliza el emailUser para encontrar el usuario y
        //hacer una lista de los nombres de su hijo
        dr.orderByChild("email").equalTo(emailUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot usuarioSnapshot : snapshot.getChildren()) {
                    // Verificar si el usuario tiene hijos
                    if (usuarioSnapshot.hasChild("hijos")) {
                        // Obtener la referencia a los hijos
                        DatabaseReference hijosRef = usuarioSnapshot.child("hijos").getRef();
                        // Escuchar los cambios en los hijos
                        hijosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // Iterar sobre los hijos y obtener sus datos
                                for (DataSnapshot hijoSnapshot : dataSnapshot.getChildren()) {
                                    String nombreHijo = hijoSnapshot.child("nombre").getValue(String.class);
                                    String apellidosHijo = hijoSnapshot.child("apellidos").getValue(String.class);

                                    String nombreCompleto = nombreHijo+" "+apellidosHijo;
                                    hijos.add(nombreCompleto);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Creamos el Spinner con los hijos (En el caso de que no haya no habrá hijos)
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hijos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selecHijo.setAdapter(adapter);


        //Con esta lista haremos un spinner el cual dependiendo del hijo seleccionado la lista de
        //eventos cambia


        /*
        *                           [Acciones de la barra inferior]
        * Asignamos onClick listeners en las distintas imagenas para que nos lleven a sus destinos
        * indicados
        * */
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaEventos.this, PantallaEventos.class);
                intent.putExtra("emailUser",emailUser);
                startActivity(intent);
            }
        });
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaEventos.this, PantallaEventos.class);
                intent.putExtra("emailUser",emailUser);
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaEventos.this, PantallaPerfil.class);
                intent.putExtra("emailUser",emailUser);
                startActivity(intent);
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaEventos.this, PantallaMensajes.class);
                intent.putExtra("emailUser",emailUser);
                startActivity(intent);
            }
        });
        //Fin de Acciones de la barra inferior




    }


}