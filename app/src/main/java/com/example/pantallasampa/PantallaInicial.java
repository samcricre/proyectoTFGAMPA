package com.example.pantallasampa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PantallaInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicial);
    }

    //Creamos los metodos que nos permiten navegar al inicio de sesion y registro
    public void navLogin(View view){

        Intent intent = new Intent(this, PantallaLogin.class);
        startActivity(intent);

    }

    public void navRegistro(View view){

        Intent intent =  new Intent(this, PantallaRegistro.class);
        startActivity(intent);
    }



}