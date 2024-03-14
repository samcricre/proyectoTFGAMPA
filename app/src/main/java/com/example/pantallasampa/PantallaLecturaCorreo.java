package com.example.pantallasampa;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PantallaLecturaCorreo extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_lectura_correo);

        // Obtener referencias a los elementos de la interfaz de usuario
        TextView asuntoTextView = findViewById(R.id.asuntoLectura);
        TextView emisorTextView = findViewById(R.id.emisor);
        TextView remitenteTextView = findViewById(R.id.remitente);
        TextView mensajeTextView = findViewById(R.id.mensaje);
        ImageView backButton = findViewById(R.id.backButtonCorreo);

        // Obtener los datos del intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String asunto = extras.getString("asunto");
            String emisor = extras.getString("destinatario");
            String remitente = extras.getString("remitente");
            String mensaje = extras.getString("contenido");

            // Establecer los datos en los elementos de la interfaz de usuario
            asuntoTextView.setText(asunto);
            emisorTextView.setText(emisor);
            remitenteTextView.setText(remitente);
            mensajeTextView.setText(mensaje);
        }

        // Agregar listener al botón de regresar
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Finaliza la actividad actual y vuelve a la anterior
            }
        });
    }
}

