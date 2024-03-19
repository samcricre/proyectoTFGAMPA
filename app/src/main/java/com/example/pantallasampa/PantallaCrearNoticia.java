package com.example.pantallasampa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PantallaCrearNoticia extends AppCompatActivity {

    //Variables donde guardar los String
    String titular;
    String subtitulo;
    String cuerpo;
    int clicks = 0;


    //Variables para enlazar los elementos
    EditText eTitular;
    EditText eSubtitulo;
    EditText eCuerpo;


    //Apuntamos en la base de datos al nodo notica
    DatabaseReference dr = FirebaseDatabase.getInstance().getReference();

    //Apu


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_crear_noticia);

        //Enlazamos elementos
        eTitular = findViewById(R.id.titularNoticia);
        eSubtitulo = findViewById(R.id.subtituloNoticia);
        eCuerpo = findViewById(R.id.cuerpoNoticia);
    }


    //Metodo para publicar la noticia
    public void publicarNoticia(View view) {
        titular = eTitular.getText().toString();
        subtitulo = eSubtitulo.getText().toString();
        cuerpo = eCuerpo.getText().toString();

        // Generar una clave única para la noticia
        String key = dr.child("noticias").push().getKey();

        // Crear el objeto Noticia con el noticiaId
        Noticia noticia = new Noticia( titular, subtitulo, cuerpo, clicks,key );

        // Guardar la noticia en la base de datos
        dr.child("noticias").child(key).setValue(noticia).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent intent =  new Intent(PantallaCrearNoticia.this ,PantallaNoticias.class);
                intent.putExtra("emailUser", getIntent().getStringExtra("emailUser"));
                intent.putExtra("keyUsuario", getIntent().getStringExtra("keyUsuario"));
                intent.putExtra("rol", getIntent().getBooleanExtra("rol",false));
                startActivity(intent);
            }
        });
    }





    public void cancelarNoticia(View view){

        Intent intent =  new Intent(this,PantallaNoticias.class);
        intent.putExtra("emailUser", getIntent().getStringExtra("emailUser"));
        intent.putExtra("keyUsuario", getIntent().getStringExtra("keyUsuario"));
        intent.putExtra("rol", getIntent().getBooleanExtra("rol",false));
        startActivity(intent);

    }


}