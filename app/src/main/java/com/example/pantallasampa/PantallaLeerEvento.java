package com.example.pantallasampa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PantallaLeerEvento extends AppCompatActivity {

    private TextView title;
    private TextView descript;
    private TextView cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_leer_evento);

        title = findViewById(R.id.tituloLeer);
        descript = findViewById(R.id.decripcionLeer);
        cancel = findViewById(R.id.irSeDeEvento);

        title.setText(getIntent().getStringExtra("title"));
        descript.setText(getIntent().getStringExtra("descipt"));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}