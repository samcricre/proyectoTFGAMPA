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
    public void publicarNoticia(View view){

        titular = eTitular.getText().toString();
        subtitulo = eSubtitulo.getText().toString();
        cuerpo = eCuerpo.getText().toString();

        //Creamos objeto noticia
        Noticia noticia = new Noticia(titular,subtitulo,cuerpo);

        //Sacamos la key que apuntara a la noticia
        String key = dr.child("noticias").push().getKey();
        dr.child("noticias").child(key).setValue(noticia).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Intent intent = new Intent(PantallaCrearNoticia.this, PantallaNoticias.class);
                startActivity(intent);
            }
        });



    }




    public void cancelarNoticia(View view){

        Intent intent =  new Intent(this,PantallaNoticias.class);
        startActivity(intent);

    }




}