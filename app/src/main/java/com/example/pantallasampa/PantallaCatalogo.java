package com.example.pantallasampa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PantallaCatalogo extends AppCompatActivity {

    private ImageView goBack;
    private CardView cuotas, extraescolares, tienda, ayuda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_catalogo);

        goBack = findViewById(R.id.goBackCatalogo);
        cuotas = findViewById(R.id.cuotas);
        extraescolares = findViewById(R.id.extraescolares);
        tienda = findViewById(R.id.tienda);
        ayuda = findViewById(R.id.ayuda);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaCatalogo.this,PantallaEventos.class);
                intent.putExtra("emailUser", getIntent().getStringExtra("emailUser"));
                startActivity(intent);

            }
        });

        confiCardViews();

    }

    public void confiCardViews() {
        cuotas.setOnClickListener(v -> mandarToast());
        extraescolares.setOnClickListener(v -> mandarToast());
        tienda.setOnClickListener(v -> mandarToast());
        ayuda.setOnClickListener(v -> mandarToast());
    }

    public void mandarToast(){
        Toast.makeText(this, "Se te ha redirgido a la página web", Toast.LENGTH_SHORT).show();
    }

}