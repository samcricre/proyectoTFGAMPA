package com.example.pantallasampa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PantallaMensajes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_mensajes);
    }

    //Metodo para navegar a la pagina para redactar correo
    public void redactarCorreo(View view){

        Intent intent =  new Intent(this, PantallaCorreo.class);
        startActivity(intent);

    }

}