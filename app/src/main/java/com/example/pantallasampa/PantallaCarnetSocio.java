package com.example.pantallasampa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PantallaCarnetSocio extends AppCompatActivity {

    //Variable donde guardamos lo recibido por el intetn
    String keyUsuario;

    //Variables donde guardar los valores
    String nombre;
    String apellidos;

    //Refeerencia de l base de datos
    DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("usuarios");

    //Variables de los elementos de la pantalla
    TextView nombreUser;
    TextView apellidosUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_carnet_socio);

        //Recibimos intent
        keyUsuario = getIntent().getStringExtra("keyUsuario");

        nombreUser = findViewById(R.id.tVNombreUser);
        apellidosUser = findViewById(R.id.tVApellidosUser);

        dr.child(keyUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                nombre = snapshot.child("nombre").getValue(String.class);
                apellidos = snapshot.child("apellidos").getValue(String.class);

                //Asignamos a los elementos de la pantalla lo extraido de la base de datos
                nombreUser.setText(nombre);
                apellidosUser.setText(apellidos);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //Metodo navegar pantalla de perfil de vuelta
    public void navPantallaPerfil(View view){

        Intent intent = new Intent(this,PantallaPerfil.class);
        startActivity(intent);

    }
}