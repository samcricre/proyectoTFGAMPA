package com.example.pantallasampa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class PantallaRegistro extends AppCompatActivity {

    //Variables necesarias
    private Button register;
    private EditText email;
    private EditText passw;
    private EditText passwConfirm;
    private FirebaseAuth mAuth;

    /*
    * Pantalla registro
    *
    *       [Por ahora esto es una versión no terminada de la pantalla registro]
    *
    * En esta pantalla insertaremos los distintos datos con los que nos registraremos con la ayuda
    * de FirebaseAuth. Con esta, podremos utilizara createUserWithEmailAndPassword(), función que
    * nos permite crear un usuario instroduciendo la contraseña y el correo electrónico.
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_registro);

        //Declaramos las variables
        register = findViewById(R.id.registerButton);
        email = findViewById(R.id.emailRegister);
        passw = findViewById(R.id.passwRegister);
        passwConfirm = findViewById(R.id.passwConfiRegister);
        mAuth = FirebaseAuth.getInstance();//Damos la isntancia a mAuth para inicializarla

        //Cuando le demos al botón de registro
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Variables auxiliares que almacenarán los campos requeridos
                //      [De momento solo estará email y passw]
                String emailAux = email.getText().toString();
                String passwAux = passw.getText().toString();

                //Confirmamos que no está vacíos los parametros obligatorios
                if(emailAux.isEmpty()||passwAux.isEmpty()){
                    //Si es el caso, se notifica y se para la función con un return
                    Toast.makeText(PantallaRegistro.this,
                            "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                //En el caso de que la validación sea correcta creanos el usuario
                mAuth.createUserWithEmailAndPassword(emailAux,passwAux)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    //En el caso de conseguirlo nos enviará a otra página
                                    // [En esta caso nos envía al inicio]
                                    Intent intent =
                                            new Intent(PantallaRegistro.this,
                                                    PantallaMensajes.class);
                                    startActivity(intent);
                                }
                                else{
                                    //En el caso de fracaso, notificaremos que ya existe la cuenta
                                    Toast.makeText(PantallaRegistro.this,
                                            "Error, la cuenta ya existe",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}