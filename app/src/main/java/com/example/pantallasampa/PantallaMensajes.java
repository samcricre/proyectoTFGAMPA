package com.example.pantallasampa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PantallaMensajes extends AppCompatActivity {

    private ImageView home, news, event, profile;
    private ListView listViewCorreos;
    private List<Correo> listaCorreos, listaCorreosEliminadosRemitente, listaCorreosEliminadosDestinatario;
    private DatabaseReference correosRef;
    private CardView cardViewEnviados, cardViewRecibidos, cardViewBorrados;
    private TextView modo;
    private String emailUser;
    private boolean rol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_mensajes);

        //Variables de la barra inferior de navegación
        home = findViewById(R.id.goHomeEmail);
        news = findViewById(R.id.goNewsEmail);
        event = findViewById(R.id.goEventEmail);
        profile = findViewById(R.id.goProfileEmail);
        emailUser = getIntent().getStringExtra("emailUser");
        rol = getIntent().getBooleanExtra("rol", false);
        if(rol)//si roll es true significa es admin
            configurarAccionesBarraInferiorAdmin();
        else//si es false es que es usuario
            configurarAccionesBarraInferiorUsuario();


        //Variables inicializadas
        listViewCorreos = findViewById(R.id.listviewmensajes);
        listaCorreos = new ArrayList<>();
        listaCorreosEliminadosRemitente = new ArrayList<>();
        listaCorreosEliminadosDestinatario = new ArrayList<>();
        correosRef = FirebaseDatabase.getInstance().getReference().child("correos");
        modo = findViewById(R.id.estadoMensaje);
        modo.setText("Correos Recibidos");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String correoUsuarioActual = currentUser.getEmail();

        // Obtener y mostrar los correos recibidos por el usuario actual
        Query query = correosRef.orderByChild("destinatario").equalTo(correoUsuarioActual);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCorreos.clear();
                listaCorreosEliminadosDestinatario.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Correo correo = snapshot.getValue(Correo.class);
                    if (!correo.isEliminado() && !correo.isEliminadoRemitente()) {
                        listaCorreos.add(correo);
                    } else if (correo.isEliminado() && correo.getDestinatario().equals(correoUsuarioActual)) {
                        listaCorreosEliminadosDestinatario.add(correo);
                    }
                }
                mostrarCorreos(listaCorreos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PantallaMensajes.this, "Error al leer los correos", Toast.LENGTH_SHORT).show();
            }
        });

        cardViewEnviados = findViewById(R.id.cardViewEnviados);
        cardViewRecibidos = findViewById(R.id.crearSorteo);
        cardViewBorrados = findViewById(R.id.cardViewBorrados);

        cardViewEnviados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarCorreosEnviados();
                modo.setText("Correos Enviados");
            }
        });

        cardViewRecibidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarCorreosRecibidos();
                modo.setText("Correos Recibidos");
            }
        });

        cardViewBorrados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarCorreosEliminados(); // Cargar los correos eliminados al hacer clic en "Borrados"
                modo.setText("Correos Eliminados");
            }
        });
    }

    private void mostrarCorreos(List<Correo> correos) {
        if (correos.isEmpty()) {
            Toast.makeText(PantallaMensajes.this, "No tiene correos disponibles", Toast.LENGTH_SHORT).show();
        } else {
            CorreoAdapter adapter = new CorreoAdapter(PantallaMensajes.this, correos);
            listViewCorreos.setAdapter(adapter);
        }
    }

    private void cargarCorreosEnviados() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String correoUsuarioActual = currentUser.getEmail();

        Query query = correosRef.orderByChild("remitente").equalTo(correoUsuarioActual);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCorreos.clear();
                listaCorreosEliminadosRemitente.clear(); // Limpiar la lista de correos eliminados por el remitente
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Correo correo = snapshot.getValue(Correo.class);
                    if (!correo.isEliminado() || correo.isEliminadoRemitente()) { // Incluir correos no eliminados y correos eliminados por el remitente
                        listaCorreos.add(correo);
                    } else {
                        listaCorreosEliminadosRemitente.add(correo); // Agregar a la lista de correos eliminados por el remitente
                    }
                }
                mostrarCorreos(listaCorreos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PantallaMensajes.this, "Error al leer los correos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarCorreosRecibidos() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String correoUsuarioActual = currentUser.getEmail();

        Query query = correosRef.orderByChild("destinatario").equalTo(correoUsuarioActual);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCorreos.clear();
                listaCorreosEliminadosDestinatario.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Correo correo = snapshot.getValue(Correo.class);
                    if (!correo.isEliminado() && !correo.isEliminadoRemitente()) {
                        listaCorreos.add(correo);
                    } else if (correo.isEliminado() && correo.getDestinatario().equals(correoUsuarioActual)) {
                        listaCorreosEliminadosDestinatario.add(correo);
                    }
                }
                mostrarCorreos(listaCorreos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PantallaMensajes.this, "Error al leer los correos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarCorreosEliminados() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String correoUsuarioActual = currentUser.getEmail();

        // Dependiendo del contexto, cargar los correos eliminados del remitente o del destinatario
        if (modo.getText().toString().equals("Correos Eliminados")) {
            Query query = correosRef.orderByChild("destinatario").equalTo(correoUsuarioActual);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listaCorreos.clear();
                    listaCorreosEliminadosDestinatario.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Correo correo = snapshot.getValue(Correo.class);
                        if (correo.isEliminadoRemitente() && correo.getDestinatario().equals(correoUsuarioActual)) {
                            listaCorreosEliminadosDestinatario.add(correo);
                        }
                    }
                    mostrarCorreos(listaCorreosEliminadosDestinatario);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(PantallaMensajes.this, "Error al leer los correos", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Query query = correosRef.orderByChild("remitente").equalTo(correoUsuarioActual);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listaCorreos.clear();
                    listaCorreosEliminadosRemitente.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Correo correo = snapshot.getValue(Correo.class);
                        if (correo.isEliminado() && correo.getRemitente().equals(correoUsuarioActual)) {
                            listaCorreosEliminadosRemitente.add(correo);
                        }
                    }
                    mostrarCorreos(listaCorreosEliminadosRemitente);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(PantallaMensajes.this, "Error al leer los correos", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void redactarCorreo(View view) {
        Intent intent = new Intent(this, PantallaCorreo.class);
        startActivity(intent);
    }



    private void configurarAccionesBarraInferiorAdmin() {
        news.setOnClickListener(v -> navigateTo(PantallaNoticias.class));
        event.setOnClickListener(v -> navigateTo(PantallaCrearEventos.class));
        profile.setOnClickListener(v -> navigateTo(PantallaPerfil.class));
        home.setOnClickListener(v -> navigateTo(PantallaEventos.class));
    }
    private void configurarAccionesBarraInferiorUsuario() {
        news.setOnClickListener(v -> navigateTo(PantallaNoticias.class));
        event.setOnClickListener(v -> navigateTo(PantallaCatalogo.class));
        profile.setOnClickListener(v -> navigateTo(PantallaPerfil.class));
        home.setOnClickListener(v -> navigateTo(PantallaEventos.class));
    }
    private void navigateTo(Class<?> destination) {
        Intent intent = new Intent(PantallaMensajes.this, destination);
        intent.putExtra("emailUser", emailUser);
        intent.putExtra("keyUsuario", getIntent().getStringExtra("keyUsuario"));
        intent.putExtra("rol", rol);
        startActivity(intent);
    }
    
}
