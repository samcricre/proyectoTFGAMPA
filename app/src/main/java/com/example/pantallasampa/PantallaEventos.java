package com.example.pantallasampa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PantallaEventos extends AppCompatActivity {
    private ImageView email, news, event, profile;
    private String emailUser, keyUsuario, hijoSelected;
    private DatabaseReference dr;
    private Spinner selecHijo;
    private Usuario user;
    private boolean esAdmin;
    private ArrayList<Evento> list = new ArrayList<>();
    private ListView listaEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_eventos);

        emailUser = getIntent().getStringExtra("emailUser");
        keyUsuario = getIntent().getStringExtra("keyUsuario");
        dr = FirebaseDatabase.getInstance().getReference();

        //Variables de la barra inferior de navegación
        email = findViewById(R.id.goEmail);
        news = findViewById(R.id.goNews);
        event = findViewById(R.id.goEvent);
        profile = findViewById(R.id.goProfile);

        selecHijo = findViewById(R.id.spinnerSelectHijos);
        listaEventos = findViewById(R.id.eventList);

        comprobarRoll();

        selecHijo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hijoSelected = parent.getItemAtPosition(position).toString();//También es curso seleccionado
                //Limpiamos las listas
                list.clear();
                listaEventos.setAdapter(null);

                //Dependiendo del roll
                if(esAdmin){
                    //Esto es debido a que si eres admin, el spinner tiene cursos y no nombres, así que puedo utilizarlo inmediatamente
                    cargarEventPorClase(hijoSelected);
                }
                else {
                    //Cogemos referencia y entramos en el usuario con la key usuario
                    DatabaseReference hijosRef = FirebaseDatabase.getInstance().getReference();
                    hijosRef.child("usuarios").child(keyUsuario).child("hijos").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //Vamos ciendo todos los hijos que hay
                            for (DataSnapshot hijoSnapshot : snapshot.getChildren()){
                                //Si el hijo, tiene el mismo nombre que el marcado en el spinner
                                if((hijoSnapshot.child("nombre").getValue(String.class)+" "+hijoSnapshot.child("apellidos").getValue(String.class)).equals(hijoSelected)){
                                    //Guardamos su clase
                                    String clase = hijoSnapshot.child("curso").getValue(String.class);
                                    cargarEventPorClase(clase);
                                    break;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(PantallaEventos.this, "Error, contacte con el servicio técnico", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void cargarEventPorClase(String clase){
        DatabaseReference edr = FirebaseDatabase.getInstance().getReference();
        edr.child("eventos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                for(DataSnapshot eventoSnapshot : snapshot.getChildren()){
                    count++;
                    if(eventoSnapshot.hasChild("curso")){
                        if(eventoSnapshot.child("curso").getValue(String.class).equals(clase) && !yaHaPasado(eventoSnapshot.child("fechaFin").getValue(String.class),eventoSnapshot.child("horaFin").getValue(String.class))){
                            Evento recop = eventoSnapshot.getValue(Evento.class);
                            list.add(recop);
                            EventAdapter adapter = new EventAdapter(PantallaEventos.this,list, esAdmin, eventoSnapshot.getKey(), emailUser);
                            listaEventos.setAdapter(adapter);
                        }
                    }

                }
                if(count == 0){
                    Toast.makeText(PantallaEventos.this, "No se han encontrado eventos asignados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                    configurarAccionesBarraInferiorUsuario();
                    esAdmin = false;
                }
                else{
                    cargarCursos();
                    configurarAccionesBarraInferiorAdmin();
                    esAdmin = true;
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
        DatabaseReference hijosRef = FirebaseDatabase.getInstance().getReference();
        hijosRef.child("usuarios").child(keyUsuario).child("hijos").addListenerForSingleValueEvent(new ValueEventListener() {
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

    //Método que nos serrivrá para asignar a cada uno de los iconos un Intent con el método NagateTo y la clase a la que van [Sujeto a cambios]
    private void configurarAccionesBarraInferiorAdmin() {
        news.setOnClickListener(v -> navigateTo(PantallaNoticias.class));
        event.setOnClickListener(v -> navigateTo(PantallaCrearEventos.class));
        profile.setOnClickListener(v -> navigateTo(PantallaPerfil.class));
        email.setOnClickListener(v -> navigateTo(PantallaMensajesAdmin.class));
    }
    private void configurarAccionesBarraInferiorUsuario() {
        news.setOnClickListener(v -> navigateTo(PantallaNoticias.class));
        event.setOnClickListener(v -> navigateTo(PantallaCatalogo.class));
        profile.setOnClickListener(v -> navigateTo(PantallaPerfil.class));
        email.setOnClickListener(v -> navigateTo(PantallaMensajes.class));
    }

    //Método complementario de configurarAccionesBarraInferior() el cual realizar el Intent y envía el emailUser [Sujeto a cambios]
    private void navigateTo(Class<?> destination) {
        Intent intent = new Intent(PantallaEventos.this, destination);
        intent.putExtra("emailUser", emailUser);
        intent.putExtra("keyUsuario", keyUsuario);
        intent.putExtra("rol", esAdmin);
        startActivity(intent);
    }

    public static boolean yaHaPasado(String fecha, String hora) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");

            Date fechaActual = new Date();
            Date fechaEvento= formatoFecha.parse(fecha);
            Date horaEvento = formatoHora.parse(hora);

            Date fechaHoraEvento = new Date(fechaEvento.getTime() + horaEvento.getTime());
            Date fechaHoraActual = new Date(fechaActual.getTime());

            return fechaHoraEvento.before(fechaHoraActual);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


}
