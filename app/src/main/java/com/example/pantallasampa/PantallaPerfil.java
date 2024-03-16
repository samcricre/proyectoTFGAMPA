package com.example.pantallasampa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PantallaPerfil extends AppCompatActivity {

    //Declaramos la variables sobre las que vamos a trabajar
    TextView nombreUsuario;

    TextView apellidoUsuario;

    TextView correoUsuario;

    TextView telefonoUsuario;

    TextView tVNumeroUsuarios;

    ListView listaHijos;

    ScrollView scrollUsuario;

    //Variable donde guardamos lo enviado a través del intent
    String userEmail;

    //Variable donde guardaremos todos los hijos del usuario
    ArrayList <Hijo> hijos = new ArrayList<>();

    //Key del usuario logeado
    String keyUsuario;

    //LineaChart - Grafico de linea
    LineChart graficoLinea;
    int numeroUsuarios;
    ArrayList<Integer> progresionUsuarios = new ArrayList<>();

    //PieChart - Gráfico de circulo
    PieChart graficoCirculo;

    int contadorH;
    int contadorF;
    int contadorTotal;


    //Usamos la clave recibida a través deñ intent para acceder a los datos de esa key
    DatabaseReference dr = FirebaseDatabase.getInstance().getReference();//Aqui estamos diciendo que apunte a los usuarios


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
        scrollUsuario = findViewById(R.id.scrollUsuario);
        graficoLinea = findViewById(R.id.graficoLineal);
        tVNumeroUsuarios = findViewById(R.id.tVNumeroUsuarios);
        graficoCirculo = findViewById(R.id.graficoCirculo);

        //Llamada a los metodos de los graficos
        graficoLineal();

        //Obtenemos la key del usuario logeado
        keyUser();

        //Recibimos los datos del intent
        userEmail = getIntent().getStringExtra("emailUser");

        //Log.d("perfil", userEmail);

        //ordenamos por email de usuarios y comparamos si hay igualdad para extraer sus datos
        dr.child("usuarios").orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
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
        dr.child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {

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
                                    String sexo = hijoSnapshot.child("sexo").getValue(String.class);

                                    //Creamos el objeto del hijo con los datos obtenidos y lo añadimos al arraylist
                                    Hijo hijo = new Hijo(nombre, apellidos, edad, curso,sexo);
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

    //Metodo LineChart
    public void graficoLineal(){


        //Creamos un Map donde guardaremos los claves vlaor que queremos guardar
        Map<String,Object> dataMap = new HashMap<>();



        //Obtenemos el numero total de usuarios
        dr.child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                numeroUsuarios = (int) snapshot.getChildrenCount();
                Log.d("numUser", String.valueOf(numeroUsuarios));
                tVNumeroUsuarios.setText(String.valueOf(numeroUsuarios));

                //Sacamos el arraylist de la base de datos lo actualizamos con el nuevo numero de usuarios y lo volvemos a guardar con el map
                dr.child("stats").child("line").child("progresionUsuarios").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot listaUser : snapshot.getChildren()){
                            Integer valor = listaUser.getValue(Integer.class);
                            progresionUsuarios.add(valor);
                        }

                        progresionUsuarios.add(numeroUsuarios);

                        //Añadimos al map los datos actualizados
                        dataMap.put("numeroUsuarios",numeroUsuarios);
                        dataMap.put("progresionUsuarios",progresionUsuarios);

                        //Guardamos el map en la ruta referenciada
                        dr.child("stats").child("line").setValue(dataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Log.d("stats", "Insertado exitosamente");

                            }
                        });

                        // Configurar el eje X
                        XAxis xAxis = graficoLinea.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                        // Configurar el eje Y
                        YAxis yAxisRight = graficoLinea.getAxisRight();
                        yAxisRight.setEnabled(false);

                        // Agregar datos al LineChart
                        ArrayList<Entry> entries = new ArrayList<>();
                        // Obtener datos de progresionUsuarios y agregarlos a entries
                        for (int i = 0; i < progresionUsuarios.size(); i++) {
                            entries.add(new Entry(i, progresionUsuarios.get(i))); // Suponiendo que i representa el índice del usuario y el valor en la lista es su progreso
                        }


                        LineDataSet dataSet = new LineDataSet(entries, "Nº de usuarios"); // Agregar etiqueta opcional
                        dataSet.setColor(Color.parseColor("#003E77"));//Color de la linea
                        dataSet.setLineWidth(2f);
                        LineData lineData = new LineData(dataSet);
                        graficoLinea.setData(lineData);
                        graficoLinea.invalidate(); // Actualizar el gráfico

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    //Método piechart
    public void graficoCirculo(){





    }


    //Metodo para navegar a crear hijo
    public void navCrearHijo(View view){

        Intent intent = new Intent(this, PantallaCrearHijo.class);
        intent.putExtra("emailUser",userEmail);//Enviamos el email con el que estamos trabajando a la pantalla de crear Hijo
        intent.putExtra("keyUsuario", keyUsuario);
        startActivity(intent);

    }


    //Metodo para navegar a pagina correos
    public void navCorreos (View view){

        Intent intent = new Intent(this, PantallaMensajes.class);
        intent.putExtra("keyUsuario", keyUsuario);
        intent.putExtra("emailUser",userEmail);
        startActivity(intent);

    }

    //Metodo navegar navCarnetSocio
    public void navCarnetSocio (View view){

        Intent intent = new Intent(this, PantallaCarnetSocio.class);
        intent.putExtra("keyUsuario", keyUsuario);
        intent.putExtra("emailUser",userEmail);
        startActivity(intent);
    }

}