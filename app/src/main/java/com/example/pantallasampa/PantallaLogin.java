package com.example.pantallasampa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class PantallaLogin extends AppCompatActivity {

    //Variables necesarias
    private EditText email;
    private EditText passw;
    private Button accessButton;
    private TextView irRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_login);

        //Inivializaremos nuestras variables
        email = findViewById(R.id.emailBoxLogin);
        passw = findViewById(R.id.passwBoxLogin);
        accessButton = findViewById(R.id.btAccederLogin);
        irRegister = findViewById(R.id.irResgistro);

        mAuth = FirebaseAuth.getInstance();


        accessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAux = email.getText().toString();
                String passwAux = passw.getText().toString();

                //Validamos que todos los campos estén rellenados
                if(emailAux.isEmpty()||passwAux.isEmpty()){
                    Toast.makeText(PantallaLogin.this,
                            "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(emailAux,passwAux)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent intent =
                                            new Intent(PantallaLogin.this,
                                                    PantallaInicial.class);
                                    intent.putExtra("email",emailAux);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(PantallaLogin.this,
                                            "Error al iniciar sesión",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        //Creamos un onClic para ir a la pantalla de registro
        irRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaLogin.this, PantallaRegistro.class);
                startActivity(intent);
            }
        });



    }

}