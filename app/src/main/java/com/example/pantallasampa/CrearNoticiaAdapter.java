package com.example.pantallasampa;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CrearNoticiaAdapter extends ArrayAdapter<Noticia> {

    private Context mContext;
    private ArrayList<Noticia> mNoticias;
    private DatabaseReference sorteosRef;
    private String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();


    public CrearNoticiaAdapter(Context context, ArrayList<Noticia> noticias) {
        super(context, 0, noticias);
        mContext = context;
        mNoticias = noticias;
        sorteosRef = FirebaseDatabase.getInstance().getReference().child("sorteos");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.cardviewnoticias, parent, false);
        }

        Noticia noticia = mNoticias.get(position);

        TextView textTitular = listItem.findViewById(R.id.tVTitular);
        textTitular.setText(noticia.getTitular());

        TextView textSubtitulo = listItem.findViewById(R.id.tVSubtitulo);
        textSubtitulo.setText(noticia.getSubtitulo());

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementarClicksEnNoticia(noticia); // Incrementar clicks en la noticia
                buscarNoticiaRelacionada(noticia);
            }
        });

        return listItem;
    }

    private void incrementarClicksEnNoticia(Noticia noticia) {
        int clicks = noticia.getClicks();
        noticia.setClicks(clicks + 1);
        // Guardar el nuevo número de clicks en la base de datos
        DatabaseReference noticiaRef = FirebaseDatabase.getInstance().getReference().child("noticias").child(noticia.getNoticiaId()).child("clicks");
        noticiaRef.setValue(clicks + 1);
    }

    private void buscarNoticiaRelacionada(Noticia noticia) {
        String noticiaKey = noticia.getNoticiaId();
        String titular = noticia.getTitular();
        String subtitulo = noticia.getSubtitulo();
        String cuerpo = noticia.getCuerpo();

        sorteosRef.orderByChild("codigoNoticia").equalTo(noticiaKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // La noticia relacionada existe en sorteos, muestra el diálogo para apuntarse al sorteo
                    mostrarDialogoApuntarseAlSorteo(noticiaKey, titular, subtitulo, cuerpo);
                } else {
                    // La noticia relacionada no existe en sorteos, mostrar un Toast indicando que no se trata de un sorteo
                    Toast.makeText(mContext, "No es un sorteo", Toast.LENGTH_SHORT).show();
                    // Abre la página de la noticia completa
                    abrirPaginaNoticiaCompleta(noticiaKey, titular, subtitulo, cuerpo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores si es necesario
            }
        });
    }

    private void mostrarDialogoApuntarseAlSorteo(String noticiaKey, String titular, String subtitulo, String cuerpo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Sorteo");
        builder.setMessage("¿Quieres apuntarte al sorteo relacionado con esta noticia?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                if (userEmail != null) {
                    Query sorteoRef = sorteosRef.orderByChild("codigoNoticia").equalTo(noticiaKey);
                    sorteoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (!dataSnapshot.child("participantes").hasChild(userEmail.replace(".", ","))) {
                                        DatabaseReference participantesRef = sorteosRef.child(dataSnapshot.getKey()).child("participantes").child(userEmail.replace(".", ","));
                                        participantesRef.setValue(userEmail);
                                    } else {
                                        Toast.makeText(mContext, "Ya estás apuntado al sorteo", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Manejar errores si es necesario
                        }
                    });

                    // Abre la página de la noticia completa
                    abrirPaginaNoticiaCompleta(noticiaKey, titular, subtitulo, cuerpo);
                } else {
                    Toast.makeText(mContext, "Debes iniciar sesión para participar en el sorteo", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                abrirPaginaNoticiaCompleta(noticiaKey, titular, subtitulo, cuerpo);
            }
        });
        builder.show();
    }

    private void abrirPaginaNoticiaCompleta(String noticiaKey, String titular, String subtitulo, String cuerpo) {
        // Abre la página de la noticia completa usando un Intent
        Intent intent = new Intent(mContext, LecturaNoticias.class);
        intent.putExtra("noticiaKey", noticiaKey);
        intent.putExtra("titular", titular);
        intent.putExtra("subtitulo", subtitulo);
        intent.putExtra("cuerpo", cuerpo);
        mContext.startActivity(intent);
    }
}
