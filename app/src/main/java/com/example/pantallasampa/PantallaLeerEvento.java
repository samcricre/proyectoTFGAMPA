package com.example.pantallasampa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PantallaLeerEvento extends AppCompatActivity {

    private TextView title;
    private TextView borrar;
    private TextView descript;
    private TextView cancel;
    private DatabaseReference db;
    private String key, emailUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_leer_evento);

        title = findViewById(R.id.tituloLeer);
        descript = findViewById(R.id.decripcionLeer);
        cancel = findViewById(R.id.irSeDeEvento);
        borrar = findViewById(R.id.borrarEvento);
        db = FirebaseDatabase.getInstance().getReference();
        key = getIntent().getStringExtra("key");
        emailUser = getIntent().getStringExtra("emailUser");

        if (getIntent().getBooleanExtra("rol", false)) {
            borrar.setVisibility(View.VISIBLE); // Si isVisible es true, hace visible el TextView
        } else {
            borrar.setVisibility(View.GONE); // Si isVisible es false, oculta el TextView
        }

        db.child("eventos").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Evento event = snapshot.getValue(Evento.class);

                title.setText(event.getTitulo());
                descript.setText(event.getDescripcion());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PantallaLeerEvento.this, "Hubo un error contacte con sercivio técnico", Toast.LENGTH_SHORT).show();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.child("eventos").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(PantallaLeerEvento.this, "Se ha eliminado el evento", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PantallaLeerEvento.this, PantallaEventos.class);
                        intent.putExtra("emailUser", emailUser);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}