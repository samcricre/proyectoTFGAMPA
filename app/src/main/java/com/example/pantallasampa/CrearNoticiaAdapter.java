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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CrearNoticiaAdapter extends ArrayAdapter<Noticia> {

    private Context mContext;
    private ArrayList<Noticia> mNoticias;
    private DatabaseReference dr;

    public CrearNoticiaAdapter(Context context, ArrayList<Noticia> noticias) {
        super(context, 0, noticias);
        mContext = context;
        mNoticias = noticias;
        dr = FirebaseDatabase.getInstance().getReference().child("noticias");
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
                incrementarNumeroClicks(noticia);
                buscarNoticiaRelacionada(noticia);
            }
        });

        return listItem;
    }

    private void incrementarNumeroClicks(Noticia noticia) {
        String titularNoticia = noticia.getTitular();
        for (Noticia n : mNoticias) {
            if (n.getTitular().equals(titularNoticia)) {
                int clicks = n.getClicks();
                n.setClicks(clicks + 1);
                notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
                actualizarNumeroClicksFirebase(n.getNoticiaId(), clicks + 1);
                break;
            }
        }
    }

    private void actualizarNumeroClicksFirebase(String noticiaId, int nuevoNumeroClicks) {
        dr.child(noticiaId).child("clicks").setValue(nuevoNumeroClicks);
    }

    private void buscarNoticiaRelacionada(Noticia noticia) {
        String noticiaKey = noticia.getNoticiaId();
        String titular = noticia.getTitular();
        String subtitulo = noticia.getSubtitulo();
        String cuerpo = noticia.getCuerpo();

        DatabaseReference otraTablaRef = FirebaseDatabase.getInstance().getReference().child("otraTabla");
        otraTablaRef.child(noticiaKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Verificar si la noticia relacionada existe en la otra tabla
                if (snapshot.exists()) {
                    // La noticia relacionada existe, muestra el diálogo para apuntarse al sorteo
                    mostrarDialogoApuntarseAlSorteo(noticiaKey);
                } else {
                    // La noticia relacionada no existe, mostrar un Toast indicando que no se trata de un sorteo
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

    private void mostrarDialogoApuntarseAlSorteo(String noticiaKey) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Sorteo");
        builder.setMessage("¿Quieres apuntarte al sorteo relacionado con esta noticia?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Implementa la lógica para apuntarse al sorteo
                apuntarseAlSorteo(noticiaKey);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Implementa la lógica para cancelar
            }
        });
        builder.show();
    }

    private void apuntarseAlSorteo(String noticiaKey) {
        // Aquí puedes implementar la lógica para apuntarse al sorteo
    }

    private void abrirPaginaNoticiaCompleta(String noticiaKey, String titular, String subtitulo, String cuerpo) {
        // Aquí abres la página de la noticia completa usando un Intent
        Intent intent = new Intent(mContext, LecturaNoticias.class);
        intent.putExtra("noticiaKey", noticiaKey);
        intent.putExtra("titular", titular);
        intent.putExtra("subtitulo", subtitulo);
        intent.putExtra("cuerpo", cuerpo);
        mContext.startActivity(intent);
    }
}
