package com.example.pantallasampa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CrearNoticiaAdapter extends ArrayAdapter <Noticia> {

    private Context mContext;
    private ArrayList<Noticia> mNoticias;

    int numeroClicksNoticia;

    DatabaseReference dr = FirebaseDatabase.getInstance().getReference();

    public CrearNoticiaAdapter(Context context, ArrayList <Noticia> noticias){

        super(context,0, noticias);
        mContext = context;
        mNoticias = noticias;

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

        //Creamos un listener para los items de la listview
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dr.child("noticias").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //Recorremos todas las noticias
                        for(DataSnapshot noticiaSnapshot : snapshot.getChildren()){

                            //Establecemos la condicion de que si los titulos son iguales, coja esa noticia
                            if(noticiaSnapshot.child("titular").getValue(String.class).equals(noticia.getTitular())){

                                //Aumentamos el numero de clicks de la noticia
                                numeroClicksNoticia = noticiaSnapshot.child("clicks").getValue(Integer.class);
                                numeroClicksNoticia ++;

                                //Volvemos a guardar el nuevo valor en la base de datos
                                noticiaSnapshot.child("clicks").getRef().setValue(numeroClicksNoticia);

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });



        return listItem;
    }
}
