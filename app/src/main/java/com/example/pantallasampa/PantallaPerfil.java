package com.example.pantallasampa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PantallaPerfil extends AppCompatActivity {

    //Declaramos la variables sobre las que vamos a trabajar
    TextView nombreUsuario;

    TextView apellidoUsuario;

    TextView correoUsuario;

    TextView telefonoUsuario;

    //Creamos conexion con la base de datos
    private DatabaseReference dR;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_perfil);


        //Vicnulamos las variables  los elementos del xml
        nombreUsuario = findViewById(R.id.tVNombre);
        apellidoUsuario = findViewById(R.id.tVApellidos);
        correoUsuario = findViewById(R.id.tVCorreo);
        telefonoUsuario = findViewById(R.id.tVTelefono);


        //Usamos la clave recibida a través deñ intent para acceder a los datos de esa key
        DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference().child("usuarios");//Aqui estamos diciendo que apunte a los usuarios

        //Recibimos los datos del intent
        String userEmail = getIntent().getStringExtra("emailUser");

        Log.d("perfil", userEmail);

        //ordenamos por email de usuarios y comparamos si hay igualdad para extraer sus datos
        usuarioRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Iteramos sobre los resultados
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    //Variables de lo extraido del database
                    String nombre = childSnapshot.child("nombre").getValue(String.class);
                    String apellidos = childSnapshot.child("apellidos").getValue(String.class);
                    String email = childSnapshot.child("email").getValue(String.class);
                    String telefono = childSnapshot.child("telf").getValue(String.class);

                    nombreUsuario.setText(nombre);
                    apellidoUsuario.setText(apellidos);
                    correoUsuario.setText(email);
                    telefonoUsuario.setText(telefono);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    //Metodo para navegar a crear hijo
    public void navCrearHijo(View view){

        Intent intent = new Intent(this, PantallaCrearHijo.class);
        startActivity(intent);

    }


    //Metodo para navegar a pagina correos
    public void navCorreos (View view){

        Intent intent = new Intent(this, PantallaMensajes.class);
        startActivity(intent);

    }

}