package com.example.pantallasampa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PantallaPerfil extends AppCompatActivity {

    //Declaramos la variables sobre las que vamos a trabajar
    TextView nombreUsuario;

    TextView apellidoUsuario;

    TextView correoUsuario;

    TextView telefonoUsuario;

    ListView listaHijos;

    //Creamos conexion con la base de datos
    private DatabaseReference dR;

    String userEmail;//Variable donde guardamos lo enviado a través del intent

    //Variable donde guardaremos todos los hijos del usuario
    ArrayList <Hijo> hijos = new ArrayList<>();

    //Key del usuario logeado
    String keyUsuario;

    //Usamos la clave recibida a través deñ intent para acceder a los datos de esa key
    DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference().child("usuarios");//Aqui estamos diciendo que apunte a los usuarios


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_perfil);


        //Vicnulamos las variables  los elementos del xml
        nombreUsuario = findViewById(R.id.tVNombre);
        apellidoUsuario = findViewById(R.id.tVApellidos);
        correoUsuario = findViewById(R.id.tVCorreo);
        telefonoUsuario = findViewById(R.id.tVTelefono);
        listaHijos = findViewById(R.id.listViewHijos);


        //Obtenemos la key del usuario logeado
        keyUser();

        //Recibimos los datos del intent
        userEmail = getIntent().getStringExtra("emailUser");
        keyUsuario = getIntent().getStringExtra("keyUsuario");


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



    //Metodo por el que sacamos la key del usuario que esta logeado
    public void keyUser(){
        Log.d("pantallaPerfil", "entra en keyuser");
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.d("pantallaPerfil", "antes del for");
                //Recorremos todos los usuarios que teiene la base de datos
                for (DataSnapshot usuarioSnapshot : snapshot.getChildren()){
                    Log.d("pantallaPerfil", "entra en el for");

                    //Extraemos las key de los usuarios
                    String usuarioKey = usuarioSnapshot.getKey();
                    Log.d("pantallaPerfil", usuarioKey);

                    //Ahora debemos verificar que coincide el email logeado con el del usuario, asi podremos coger la key del usuarios que tenemos logueado
                    String emailUsuario = usuarioSnapshot.child("email").getValue(String.class);

                    if (emailUsuario.equals(userEmail))  {

                        keyUsuario = usuarioKey;

                        //Apuntamos referencia a los hijos del usuario logeado a través de la key
                        DatabaseReference hijosRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(keyUsuario).child("hijos");


                        hijosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                //Iteramos sobre los hijos
                                for (DataSnapshot hijoSnapshot : snapshot.getChildren()){

                                    //Obtenemos los datos del hijo
                                    String nombre = hijoSnapshot.child("nombre").getValue(String.class);
                                    String apellidos = hijoSnapshot.child("apellidos").getValue(String.class);
                                    String curso = hijoSnapshot.child("curso").getValue(String.class);
                                    String edad = hijoSnapshot.child("edad").getValue(String.class);

                                    //Creamos el objeto del hijo con los datos obtenidos y lo añadimos al arraylist
                                    Hijo hijo = new Hijo(nombre, apellidos, edad, curso);
                                    hijos.add(hijo);

                                    //Le pasamos los datos al adpter
                                    CrearHijoAdapter adapater = new CrearHijoAdapter(PantallaPerfil.this,hijos);

                                    listaHijos.setAdapter(adapater);

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                    }



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

    /*
    //Metodo para guardar hijos en arraylist
    public void guardarHijos(){

        Log.d("pantallaPerfil", "Entra en metodo guardar hijo");


        //Apuntamos referencia a los hijos del usuario logeado a través de la key
        DatabaseReference hijosRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(keyUsuario).child("hijos");

        Log.d("pantallaPerfil", "apunta a la referencia");

        hijosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Iteramos sobre los hijos
                for (DataSnapshot hijoSnapshot : snapshot.getChildren()){

                    //Obtenemos los datos del hijo
                    String nombre = hijoSnapshot.child("nombre").getValue(String.class);
                    String apellidos = hijoSnapshot.child("apellidos").getValue(String.class);
                    String curso = hijoSnapshot.child("curso").getValue(String.class);
                    String edad = hijoSnapshot.child("edad").getValue(String.class);

                    //Creamos el objeto del hijo con los datos obtenidos y lo añadimos al arraylist
                    Hijo hijo = new Hijo(nombre, apellidos, edad, curso);
                    hijos.add(hijo);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    */


    //Metodo para navegar a crear hijo
    public void navCrearHijo(View view){

        Intent intent = new Intent(this, PantallaCrearHijo.class);
        intent.putExtra("emailUser",userEmail);//Enviamos el email con el que estamos trabajando a la pantalla de crear Hijo
        startActivity(intent);

    }


    public void navCarnetSocio(View view){

        Intent intent = new Intent(this,PantallaCarnetSocio.class);
        intent.putExtra("keyUsuario",keyUsuario);
        startActivity(intent);

    }


    //Metodo para navegar a pagina correos
    public void navCorreos (View view){

        Intent intent = new Intent(this, PantallaMensajes.class);
        startActivity(intent);

    }

}