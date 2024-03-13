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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class PantallaCorreo extends AppCompatActivity {

    private EditText destinatarioEditText;
    private EditText asuntoEditText;
    private EditText contenidoEditText;
    private Button enviarButton;

    private DatabaseReference correosRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_correo);

        destinatarioEditText = findViewById(R.id.editTextText2);
        asuntoEditText = findViewById(R.id.editTextText3);
        contenidoEditText = findViewById(R.id.editTextTextMultiLine);
        enviarButton = findViewById(R.id.button);

        // Obtener una referencia a la base de datos de Firebase
        correosRef = FirebaseDatabase.getInstance().getReference().child("correos");

        enviarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarCorreo();
            }
        });
    }


    private void enviarCorreo() {
        String destinatario = destinatarioEditText.getText().toString();
        String asunto = asuntoEditText.getText().toString();
        String contenido = contenidoEditText.getText().toString();

        // Obtener el correo electrónico del remitente
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String remitente = currentUser.getEmail();

        // Validar que los campos no estén vacíos
        if (TextUtils.isEmpty(destinatario) || TextUtils.isEmpty(asunto) || TextUtils.isEmpty(contenido)) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generar una clave única para el correo electrónico
        String correoID = correosRef.push().getKey();

        // Crear un mapa con los datos del correo electrónico CLASE PENDIENTE ---------
        Map<String, Object> correoValues = new HashMap<>();
        correoValues.put("remitente", remitente);
        correoValues.put("destinatario", destinatario);
        correoValues.put("asunto", asunto);
        correoValues.put("contenido", contenido);
        // ---------------

        // Guardar el correo electrónico en la base de datos
        correosRef.child("conversacionID1").child(correoID).setValue(correoValues)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PantallaCorreo.this, "Correo enviado correctamente", Toast.LENGTH_SHORT).show();
                        // Limpiar los campos de texto después de enviar el correo
                        destinatarioEditText.setText("");
                        asuntoEditText.setText("");
                        contenidoEditText.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PantallaCorreo.this, "Error al enviar el correo", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Metodo que cancela el correo y vuelve de vuelta a la pantalla de mensajes
    public void cancelarCorreo(View view){
        Intent intent =  new Intent(this, PantallaMensajes.class);
        startActivity(intent);
    }

}