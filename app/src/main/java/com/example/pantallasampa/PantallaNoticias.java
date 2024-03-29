package com.example.pantallasampa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PantallaNoticias extends AppCompatActivity {

    private ImageView home, email, event, profile;
    private boolean rol;

    String emailUser;
    String keyUsuario;

    //Variable donde guardaremos todos los hijos del usuario
    ArrayList<Noticia> noticias = new ArrayList<>();

    Usuario usuario;

    DatabaseReference dr = FirebaseDatabase.getInstance().getReference();

    //variables de elementos
    Button btAñadirNoticia;
    ListView listaNoticias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_noticias);

        Button btnIrAPagina = findViewById(R.id.btAnadirNoticia);


        //Variables de la barra inferior de navegación
        home = findViewById(R.id.goHomeNews);
        email = findViewById(R.id.goEmailNews);
        event = findViewById(R.id.goEventNews);
        profile = findViewById(R.id.goProfileNews);
        emailUser = getIntent().getStringExtra("emailUser");
        rol = getIntent().getBooleanExtra("rol", false);
        if (rol)//si roll es true significa es admin
            configurarAccionesBarraInferiorAdmin();
        else//si es false es que es usuario
            configurarAccionesBarraInferiorUsuario();

        //Recibimos lo pasado por el intent
        emailUser = getIntent().getStringExtra("emailUser");
        keyUsuario = getIntent().getStringExtra("keyUsuario");


        btAñadirNoticia = findViewById(R.id.btAnadirNoticia);
        listaNoticias = findViewById(R.id.listViewNoticias);

        //comprobamos rol
        comprobarRol();

        //Mostramos listview de noticias
        listViewNoticias();

        // Agregar listener de clics a los elementos de la lista de noticias
        listaNoticias.setOnItemClickListener((parent, view, position, id) -> {
            // Obtener la noticia seleccionada
            Noticia noticiaSeleccionada = noticias.get(position);
            String noticiaKey = noticiaSeleccionada.getNoticiaId();
            Toast.makeText(PantallaNoticias.this, "Clic en la noticia: " + noticiaSeleccionada.getTitular(), Toast.LENGTH_SHORT).show();



        });


        // Configurar un listener para el botón
        btnIrAPagina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            navigateTo(PantallaCrearNoticia.class);
            }
        });

    }

    //Metodo comprobar rol para poder publicar noticia
    //Método para comprobar el rol del usuario que se introduce
    public void comprobarRol() {


        //Apuntamos el dr a usuarios
        dr.child("usuarios").child(keyUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String rol = snapshot.child("roll").getValue(String.class);

                if (rol.equals("usuario")) {
                    btAñadirNoticia.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    // Método para mostrar ListView de noticias
    public void listViewNoticias() {
        dr.child("noticias").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot noticiaSnapshot : snapshot.getChildren()) {
                    String noticiaId = noticiaSnapshot.getKey(); // Obtener el ID de la noticia
                    String titular = noticiaSnapshot.child("titular").getValue(String.class);
                    String subtitulo = noticiaSnapshot.child("subtitulo").getValue(String.class);
                    String cuerpo = noticiaSnapshot.child("cuerpo").getValue(String.class);
                    int nClicks = noticiaSnapshot.child("clicks").getValue(Integer.class);

                    Noticia noticia = new Noticia(titular, subtitulo, cuerpo, nClicks, noticiaId);
                    noticias.add(noticia);
                }

                // Crear el adaptador y asignarlo al ListView fuera del bucle for
                CrearNoticiaAdapter adapter = new CrearNoticiaAdapter(PantallaNoticias.this, noticias);
                listaNoticias.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores si es necesario
            }
        });
    }


    // Método para mostrar un mensaje de confirmación
    private void mostrarMensajeConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación");
        builder.setMessage("Te has apuntado al sorteo.");

        // Agrega botón para aceptar
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtener el correo electrónico del usuario
                String userEmail = emailUser;

                // Obtener la noticia seleccionada
                int posicionSeleccionada = listaNoticias.getCheckedItemPosition();
                Noticia noticiaSeleccionada = noticias.get(posicionSeleccionada);

                // Iniciar un nuevo Intent para abrir otra página
                Intent intent = new Intent(PantallaNoticias.this, LecturaNoticias.class);
                intent.putExtra("emailUser", emailUser);
                intent.putExtra("keyUsuario", keyUsuario);
                intent.putExtra("titular", noticiaSeleccionada.getTitular());
                intent.putExtra("subtitulo", noticiaSeleccionada.getSubtitulo());
                intent.putExtra("cuerpo", noticiaSeleccionada.getCuerpo());
                startActivity(intent);
            }
        });

        // Muestra el mensaje de confirmación
        builder.show();
    }



    private void configurarAccionesBarraInferiorAdmin() {
        home.setOnClickListener(v -> navigateTo(PantallaEventos.class));
        event.setOnClickListener(v -> navigateTo(PantallaCrearEventos.class));
        profile.setOnClickListener(v -> navigateTo(PantallaPerfil.class));
        email.setOnClickListener(v -> navigateTo(PantallaMensajesAdmin.class));
    }

    private void configurarAccionesBarraInferiorUsuario() {
        home.setOnClickListener(v -> navigateTo(PantallaEventos.class));
        event.setOnClickListener(v -> navigateTo(PantallaCatalogo.class));
        profile.setOnClickListener(v -> navigateTo(PantallaPerfil.class));
        email.setOnClickListener(v -> navigateTo(PantallaMensajes.class));
    }

    //Método complementario de configurarAccionesBarraInferior() el cual realizar el Intent y envía el emailUser [Sujeto a cambios]
    private void navigateTo(Class<?> destination) {
        Intent intent = new Intent(PantallaNoticias.this, destination);
        intent.putExtra("emailUser", emailUser);
        intent.putExtra("keyUsuario", keyUsuario);
        intent.putExtra("rol", rol);
        startActivity(intent);
    }

    // Método para iniciar el Intent para mostrar la noticia en LecturaNoticias
    private void iniciarIntentLecturaNoticias(Noticia noticia) {
        Intent intent = new Intent(PantallaNoticias.this, LecturaNoticias.class);
        intent.putExtra("emailUser", emailUser);
        intent.putExtra("keyUsuario", keyUsuario);
        intent.putExtra("titular", noticia.getTitular());
        intent.putExtra("subtitulo", noticia.getSubtitulo());
        intent.putExtra("cuerpo", noticia.getCuerpo());
        startActivity(intent);
    }
}
