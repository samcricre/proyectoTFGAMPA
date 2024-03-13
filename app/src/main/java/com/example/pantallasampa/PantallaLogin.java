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

import com.google.firebase.auth.FirebaseAuth;

public class PantallaLogin extends AppCompatActivity {

    //Variables necesarias
    private EditText email;
    private EditText passw;
    private Button accessButton;
    private TextView irRegister;
    private FirebaseAuth mAuth;

    /*
     * Pantalla login
     *
     *          [Terminado (falta testing y cambiar el intent)]
     *
     * En esta pantalla simplemente tendremos que introducir el correo con el que nos hemos
     * registrado. En el caso de que no tengamos correo, podremos darle al mensaje azul, el cual
     * nos redirige a una nueva pantalla donde podremos crearnos una cuenta. En el caso de que si
     * tengamos una cuenta, tendremos que introducir el email y la contraseñá, los cuales serán
     * verificados gracias a signInWithEmailAndPassword() de FirebaseAuth
     * */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_login);

        //Inivializaremos nuestras variables
        email = findViewById(R.id.emailBoxLogin);
        passw = findViewById(R.id.passwBoxLogin);
        accessButton = findViewById(R.id.btAccederLogin);
        irRegister = findViewById(R.id.irResgistro);
        mAuth = FirebaseAuth.getInstance();//Inicializamos la variable dando la isntancia

        //Si le damos al botón de iniciar sessión
        accessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //variables necesarias que almacenan los datos de email y passw como Strings
                String emailAux = email.getText().toString();
                String passwAux = passw.getText().toString();

                //Validamos que todos los campos estén rellenados
                if(emailAux.isEmpty()||passwAux.isEmpty()){
                    //Si no devuelve un mensaje y para la función con un return
                    Toast.makeText(PantallaLogin.this,
                            "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                //En el caso de que esté correcto, se realizará signInWithEmailAndPassword(), el
                //cual verifica si el usuario existe por el email y el passw
                mAuth.signInWithEmailAndPassword(emailAux,passwAux)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    //Si se consegue, se nos enviará a la siguiente pantalla
                                    //  [El intent debe cambiarse para la versión final]
                                    Intent intent =
                                            new Intent(PantallaLogin.this,
                                                    PantallaInicial.class);
                                    startActivity(intent);
                                }
                                else{
                                    //En el caso de fracaso, se nos envía una notifiación
                                    Toast.makeText(PantallaLogin.this,
                                            "Usuario o contraseña incorrectas",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        //Creamos un onClic para ir a la pantalla de registro con el uso de un Intent
        irRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaLogin.this, PantallaRegistro.class);
                startActivity(intent);
            }
        });

    }

}