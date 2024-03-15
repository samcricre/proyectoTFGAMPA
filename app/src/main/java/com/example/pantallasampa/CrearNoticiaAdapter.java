package com.example.pantallasampa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CrearNoticiaAdapter extends ArrayAdapter <Noticia> {

    private Context mContext;
    private ArrayList<Noticia> mNoticias;

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





        return listItem;
    }
}
