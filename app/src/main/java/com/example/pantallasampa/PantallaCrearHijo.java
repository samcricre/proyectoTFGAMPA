package com.example.pantallasampa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_crear_hijo);

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


        //le damos valor a las variables
        nombreHijo = nombre.getText().toString();
        apellidosHijo = apellidos.getText().toString();
        edadHijo = edad.getText().toString();






    }


    //Metodo por el que guardamos el objeto en la base de datos
    public void crearHijo (View view){

        Hijo hijo = new Hijo(nombreHijo,apellidosHijo,edadHijo,cursoHijo);

        //Viajamos hasta el nodo hijos y generamos una key como valor de identidad del nuevo objeto que vamos a metter
        //EN caso de que el nodo hijos no exista relatime lo creará automaticamnete

        //Hay que extraer la key del usuario con el que estamos logeados para asi apuntar a su nodo propio y poder desarrollar el hijo

        String key = dr.child("usuarios")/*aqui deberia ir el valor del usuario al que queremos añadir hijos*/.child("hijos").push().getKey();

        //en la declaracion de dr ya estamos apuntando a usuarios
        dr/*falta igual identificar el usuario donde queremos apuntar*/.child("hijos").child(key).setValue(hijo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(PantallaCrearHijo.this, "Hijo añadido correctamente", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(PantallaCrearHijo.this, PantallaPerfil.class);

            }
        });


    }





}