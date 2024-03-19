package com.example.pantallasampa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LecturaNoticias extends AppCompatActivity {

    private TextView asuntoLectura;
    private TextView subtituloLectura;
    private TextView mensaje;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura_noticias);

        // Inicializar vistas
        asuntoLectura = findViewById(R.id.asuntoLectura);
        subtituloLectura = findViewById(R.id.subtituloLectura);
        mensaje = findViewById(R.id.mensaje);
        backButton = findViewById(R.id.backButtonCorreo);

        // Configurar clic en el botón de volver atrás
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Volver atrás al presionar el botón
            }
        });

        // Obtener datos de la noticia pasados desde la actividad anterior
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String asunto = extras.getString("titular");
            String subtitulo = extras.getString("subtitulo");
            String cuerpo = extras.getString("cuerpo");

            // Establecer datos en las vistas
            asuntoLectura.setText(asunto);
            subtituloLectura.setText(subtitulo);
            mensaje.setText(cuerpo);
        }
    }
}
