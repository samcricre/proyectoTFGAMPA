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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_eventos);

        emailUser = getIntent().getStringExtra("emailUser");
        dr = FirebaseDatabase.getInstance().getReference();

        email = findViewById(R.id.goEmail);
        news = findViewById(R.id.goNews);
        event = findViewById(R.id.goEvent);
        profile = findViewById(R.id.goProfile);
        selecHijo = findViewById(R.id.spinnerSelectHijos);

        cargarHijos();
        configurarAccionesBarraInferior();
    }


    /*
    * CargarHijos() [Usuarios]
    *
    * Es un método que nos permite realizar una recopilación de todos los hijos del usuario iniciado
    * Este nos permite crear una lista con estos hijos y crear una Sipinner con ellos
    * */
    private void cargarHijos() {
        //Realizamos una busqueda desde usuarios, para encontrar al usuario al cual le pertenece
        //el email (transeferido por el intent)
        dr.child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Con un bucle for each, miraremos en cada uno de los snapshots
                for (DataSnapshot usuarioSnapshot : snapshot.getChildren()){
                    //Guardaremos su key e email
                    String usuarioKey = usuarioSnapshot.getKey();
                    String emailUsuario = usuarioSnapshot.child("email").getValue(String.class);

                    //En el caso de que los correos sean iguales
                    if (emailUsuario.equals(emailUser)&& !emailUsuario.isEmpty())  {

                        //Se guardará la llave para utilizarlo para llegar al usuario directamente y entrar en los nodos hijo
                        keyUsuario = usuarioKey;
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

    //Método que nos serrivrá para asignar a cada uno de los iconos un Intent con el método NagateTo y la clase a la que van [Sujeto a cambios]
    private void configurarAccionesBarraInferior() {
        news.setOnClickListener(v -> navigateTo(PantallaEventos.class));
        event.setOnClickListener(v -> navigateTo(PantallaEventos.class));
        profile.setOnClickListener(v -> navigateTo(PantallaPerfil.class));
        email.setOnClickListener(v -> navigateTo(PantallaMensajes.class));
    }

    //Método complementario de configurarAccionesBarraInferior() el cual realizar el Intent y envía el emailUser [Sujeto a cambios]
    private void navigateTo(Class<?> destination) {
        Intent intent = new Intent(PantallaEventos.this, destination);
        intent.putExtra("emailUser", emailUser);
        startActivity(intent);
    }
}
