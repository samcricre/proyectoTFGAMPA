package com.example.pantallasampa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PantallaNoticias extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_noticias);
    }


    public void navCrearNoticia(View view){

        Intent intent = new Intent(this, PantallaCrearNoticias.class);

        startActivity(intent);

    }

}