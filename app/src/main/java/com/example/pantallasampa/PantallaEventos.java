package com.example.pantallasampa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PantallaEventos extends AppCompatActivity {
    private ImageView email, news, event, profile;
    private String emailUser, keyUsuario;
    private DatabaseReference dr;
    private Spinner selecHijo;
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_eventos);

        emailUser = getIntent().getStringExtra("emailUser");
        keyUsuario = getIntent().getStringExtra("keyUsuario");
        dr = FirebaseDatabase.getInstance().getReference();

        email = findViewById(R.id.goEmail);
        news = findViewById(R.id.goNews);
        event = findViewById(R.id.goEvent);
        profile = findViewById(R.id.goProfile);
        selecHijo = findViewById(R.id.spinnerSelectHijos);

        comprobarRoll();
    }

    public void comprobarRoll(){
        dr.child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot usuarioSnapshot : snapshot.getChildren()){

                    //Log.d("Usuario",user.getRoll());
                    String usuarioKey = usuarioSnapshot.getKey();
                    String emailUsuario = usuarioSnapshot.child("email").getValue(String.class);

                    //En el caso de que los correos sean iguales
                    if (emailUsuario.equals(emailUser) && !emailUsuario.isEmpty()) {
                        user = usuarioSnapshot.getValue(Usuario.class);
                        keyUsuario = usuarioKey;
                    }
                }
                //Dependiendo de su roll se tendrá una funciónalidad distinta
                if(user.getRoll().equals("usuario") && !user.getRoll().isEmpty()){
                    cargarHijos();
                    configurarAccionesBarraInferior();//Por si el navigate es distinto [si no lo cambio de vuelta]
                }
                else{
                    cargarCursos();
                    configurarAccionesBarraInferior();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    /*
    * CargarCursos() [Admin]
    *
    * En el caso del admin podrán selecciona un curso. El cual le enseñará los eventos creados para
    * el curso seleccionado
    * */
    private void cargarCursos(){
        String [] cursos = {"1ºA", "1ºB", "1ºC","2ºA", "2ºB", "2ºC","3ºA", "3ºB", "3ºC","4ºA", "4ºB", "4ºC","5ºA", "5ºB", "5ºC","6ºA", "6ºB", "6ºC",};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(PantallaEventos.this, android.R.layout.simple_spinner_item, cursos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selecHijo.setAdapter(adapter);
    }



    /*
    * CargarHijos() [Usuarios]
    *
    * En el caso de los usuarios/socios/familias
    * Es un método que nos permite realizar una recopilación de todos los hijos del usuario iniciado
    * Este nos permite crear una lista con estos hijos y crear una Sipinner con ellos
    * */
    private void cargarHijos() {
        //Realizamos una busqueda desde usuarios, para encontrar al usuario al cual le pertenece
        //el email (transeferido por el intent)
        dr.child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DatabaseReference hijosRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(keyUsuario).child("hijos");
                hijosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //Una vez dentro del nodo hijos en el que estamos interesados, los iremos guardando con la ayuda de un for each y un List de Strings
                        List<String> hijos = new ArrayList<>();
                        for (DataSnapshot hijoSnapshot : snapshot.getChildren()){
                            //Obtenemos los datos del hijo y los guardamos en la lista
                            String nombre = hijoSnapshot.child("nombre").getValue(String.class);
                            String apellidos = hijoSnapshot.child("apellidos").getValue(String.class);

                            hijos.add(nombre+" "+apellidos);
                        }

                        //Creamos el Spinner finalmente
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(PantallaEventos.this, android.R.layout.simple_spinner_item, hijos);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        selecHijo.setAdapter(adapter);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(PantallaEventos.this, "Error, contacte con el servicio técnico", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

    //Método que nos serrivrá para asignar a cada uno de los iconos un Intent con el método NagateTo y la clase a la que van [Sujeto a cambios]
    private void configurarAccionesBarraInferior() {
        news.setOnClickListener(v -> navigateTo(PantallaNoticias.class));
        event.setOnClickListener(v -> navigateTo(PantallaCrearEventos.class));
        profile.setOnClickListener(v -> navigateTo(PantallaPerfil.class));
        email.setOnClickListener(v -> navigateTo(PantallaMensajes.class));
    }

    //Método complementario de configurarAccionesBarraInferior() el cual realizar el Intent y envía el emailUser [Sujeto a cambios]
    private void navigateTo(Class<?> destination) {
        Intent intent = new Intent(PantallaEventos.this, destination);
        intent.putExtra("emailUser", emailUser);
        intent.putExtra("keyUsuario", keyUsuario);
        startActivity(intent);
    }


}
