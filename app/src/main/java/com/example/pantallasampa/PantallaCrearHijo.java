package com.example.pantallasampa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PantallaCrearHijo extends AppCompatActivity {


    //Variables para crear el objeto de hijo
    String nombreHijo;
    String apellidosHijo;
    String edadHijo;
    String cursoHijo;

    DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("usuarios");


    //Variables vincular elemetos
    EditText nombre;
    EditText apellidos;
    EditText edad;
    Button btAñadirHijo;

    Spinner spinnerCursos;

    //Variable donde recogemos lo pasado a través del intent
    String userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_crear_hijo);

        //recogemos lo pasado a través del intent
        userEmail = getIntent().getStringExtra("emailUser");

        //Lista donde guardamos los cursos disponibles para su eleccion
        String [] cursos = {"1ºA", "1ºB", "1ºC","2ºA", "2ºB", "2ºC","3ºA", "3ºB", "3ºC","4ºA", "4ºB", "4ºC","5ºA", "5ºB", "5ºC","6ºA", "6ºB", "6ºC",};


        ///Vinculamos los elementos
        nombre = findViewById(R.id.nombreHijo);
        apellidos = findViewById(R.id.apellidosHijo);
        edad = findViewById(R.id.edadHijo);
        spinnerCursos = findViewById(R.id.spCursos);
        btAñadirHijo = findViewById(R.id.btAnadirHijo);

        //Vinculamos la lista al spinner a través de un adpater
        //Vinculamos el arrayList al spinner a traves de un adaptador
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cursos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCursos.setAdapter(adapter);


        //Escuchamos la seleccion del spinner para poder guardarlo en una variable
        spinnerCursos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Guradamos el elemento que seleccionamos en el spinner en una variable para poder crear el objeto hijo
                cursoHijo = spinnerCursos.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });









    }


    //Metodo por el que guardamos el objeto en la base de datos
    public void crearHijo (View view){

        //le damos valor a las variables
        nombreHijo = nombre.getText().toString();
        apellidosHijo = apellidos.getText().toString();
        edadHijo = edad.getText().toString();

        Log.d("objetohijo", nombreHijo);
        Log.d("objetohijo", apellidosHijo);
        Log.d("objetohijo", edadHijo);

        Hijo hijo = new Hijo(nombreHijo,apellidosHijo,edadHijo,cursoHijo);



        //Viajamos hasta el nodo hijos y generamos una key como valor de identidad del nuevo objeto que vamos a metter
        //EN caso de que el nodo hijos no exista relatime lo creará automaticamnete

        //Hay que extraer la key del usuario con el que estamos logeados para asi apuntar a su nodo propio y poder desarrollar el hijo

        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Recorremos todos los usuarios que teiene la base de datos
                for (DataSnapshot usuarioSnapshot : snapshot.getChildren()){

                    //Extraemos las key de los usuarios
                    String usuarioKey = usuarioSnapshot.getKey();

                    //Ahora debemos verificar que coincide el email logeado con el del usuario, asi podremos coger la key del usuarios que tenemos logueado
                    String emailUsuario = usuarioSnapshot.child("email").getValue(String.class);

                    if (emailUsuario.equals(userEmail))  {

                        //Si encontramos la coincidencia de email, crearemos el nodo de hijos bajo su key. Para ello deberemos apuntar donde queremos guardar los datos
                        String key = dr/*.child("usuarios")*/.child(usuarioKey).child("hijos").push().getKey();

                        //Le añadimos un valor a esa key pasandole el objeto hijo que hemos creado con los datos recogidos.
                        //Comenzamos la ruta directamente desde la key, ya que al declarar el databse reference le hemos marcado la posicion de usuarios
                        dr.child(usuarioKey).child("hijos").child(key).setValue(hijo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                //Si es exitoso que nos devuelva de nuevo a la pantalla de peril
                                Toast.makeText(PantallaCrearHijo.this, "Hijo añadido correctamente", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PantallaCrearHijo.this, PantallaPerfil.class);
                                intent.putExtra("emailUser",userEmail);
                                startActivity(intent);
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





}