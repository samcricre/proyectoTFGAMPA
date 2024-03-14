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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class PantallaRegistro extends AppCompatActivity {

    //Variables necesarias
    private Button register;
    private EditText nombre;
    private EditText apellido;
    private EditText email;
    private EditText telef;
    private EditText passw;
    private EditText passwConfirm;
    private FirebaseAuth mAuth;
    private DatabaseReference dr;

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
        nombre = findViewById(R.id.nombreRegister);
        apellido = findViewById(R.id.apellidosRegister);
        email = findViewById(R.id.emailRegister);
        telef = findViewById(R.id.telefRegister);
        passw = findViewById(R.id.passwRegister);
        passwConfirm = findViewById(R.id.passwConfiRegister);
        mAuth = FirebaseAuth.getInstance();//Damos la isntancia a mAuth para inicializarla
        dr = FirebaseDatabase.getInstance().getReference();//Conseguimos la referencia

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Datos necesarios
                String nombreAux = nombre.getText().toString();
                String apellidosAux = apellido.getText().toString();
                String emailAux = email.getText().toString();
                String telefAux = telef.getText().toString();
                String passwAux = passw.getText().toString();
                String passwConfiAux = passwConfirm.getText().toString();

                //Guardaremos los datos también en el tipo usuario
                Usuario usuario = new Usuario(nombreAux,apellidosAux,emailAux,telefAux);

                //Validación
                if(!validacion(nombreAux,apellidosAux,emailAux,telefAux,passwAux,passwConfiAux)){
                    //Si la validación es incorrecta, con un return para la función
                    return;
                }

                //En el caso de que la validación sea correcta creanos el usuario
                mAuth.createUserWithEmailAndPassword(emailAux,passwAux)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    //En el caso de conseguirlo como eso significa que el correo no
                                    //está registrado y es único podremos crear el usuario en la
                                    //base de datos. Por otro lado se nos manda a la siguiente página
                                    createDataInDB(usuario);

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

    /*
    * Una vez creada el usuario en auth, lo que haremos es crear también el usuario en la base de
    * datos de nuestra app. En este caso en la RealTimeDatabase. Esto nos permitirá conocer sus
    * posteriormente.
    * */
    public void createDataInDB(Usuario user){

        String key = dr.child("usuarios").push().getKey();
        dr.child("usuarios").child(key).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(PantallaRegistro.this, "Cuenta regitrada correctamente", Toast.LENGTH_SHORT).show();

                //Obtenemos el usuario actualmente autenticado
                FirebaseUser user = mAuth.getCurrentUser();

                String userEmail = user.getEmail();

                Intent intent = new Intent(PantallaRegistro.this,PantallaPerfil.class);
                intent.putExtra("emailUser", userEmail);//Pasamos el email del usuario dentro de la app para localizarle y extraer los datos
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PantallaRegistro.this, "Error de la base de datos (no el auth)", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /*
    * Vamos a crear un método que nos servirá para realizar una validación de los datos introducidos
    *
    *   -Que ningún campo esté vacío
    *   -Telefono tenga 9 numeros
    *   -Correo con @ y luego un .
    *   -Contraseña y contraseña de verificación iguales
    *
    * [Código optimizable]
    * */
    public boolean validacion(String nombre, String apellido, String email, String telf, String passw, String passwConf){
        boolean valid = true;

        //Que ningún campo esté vacío
        if(nombre.isEmpty()||apellido.isEmpty()||email.isEmpty()||telf.isEmpty()||passw.isEmpty()||passwConf.isEmpty()){
            valid = false;
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return valid;
        }

        if(!validarTelefono(telf)){
            valid = false;
            Toast.makeText(this, "Formato del telefono incorrecto", Toast.LENGTH_SHORT).show();
        }

        if(!validarCorreo(email)){
            valid = false;
            Toast.makeText(this, "Formato del correo es incorrecto", Toast.LENGTH_SHORT).show();
        }

        if(!passw.equals(passwConf)){
            valid = false;
            Toast.makeText(this, "Contraseñas no coincidentes", Toast.LENGTH_SHORT).show();
        }

        return valid;
    }


    //Función para validar correo
    public static boolean validarCorreo(String correo) {
        // Expresión regular para verificar el formato de un correo electrónico
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        // Compilar la expresión regular en un patrón
        Pattern pattern = Pattern.compile(regex);
        // Verificar si el correo coincide con el patrón
        return pattern.matcher(correo).matches();
    }

    //Función para validar telefono
    public static boolean validarTelefono(String telefono) {
        // Expresión regular para verificar el formato de un número de teléfono (9 dígitos)
        String regex = "^\\d{9}$";
        // Compilar la expresión regular en un patrón
        Pattern pattern = Pattern.compile(regex);
        // Verificar si el número de teléfono coincide con el patrón
        return pattern.matcher(telefono).matches();
    }
}