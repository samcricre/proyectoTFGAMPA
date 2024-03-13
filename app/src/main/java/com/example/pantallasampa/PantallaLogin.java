package com.example.pantallasampa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PantallaLogin extends AppCompatActivity {

    //Variables necesarias
    private EditText email;
    private EditText passw;
    private Button accessButton;
    private TextView irRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_login);

        //Inivializaremos nuestras variables
        email = findViewById(R.id.emailBoxLogin);
        passw = findViewById(R.id.passwBoxLogin);
        accessButton = findViewById(R.id.btAccederLogin);
        irRegister = findViewById(R.id.irResgistro);






    }
}