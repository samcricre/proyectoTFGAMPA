package com.example.pantallasampa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SorteoAdapter extends ArrayAdapter<Sorteos> {

    private Context mContext;
    private List<Sorteos> mSorteosList;

    public SorteoAdapter(Context context, List<Sorteos> sorteosList) {
        super(context, 0, sorteosList);
        mContext = context;
        mSorteosList = sorteosList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_listview_sorteo, parent, false);
        }

        Sorteos sorteo = mSorteosList.get(position);

        TextView nombreTextView = listItem.findViewById(R.id.textViewNombreSorteo);
        nombreTextView.setText("Nombre: " + sorteo.getNombre());

        TextView descripcionTextView = listItem.findViewById(R.id.textViewDescripcionSorteo);
        descripcionTextView.setText("Descripción: " + sorteo.getDescripcion());

        TextView premiosTextView = listItem.findViewById(R.id.textViewPremiosSorteo);
        premiosTextView.setText("Premios: " + sorteo.getPremios());

        TextView estadoTextView = listItem.findViewById(R.id.textViewEstadoSorteo);
        String estado = sorteo.isFinalizado() ? "Finalizado" : "Activo";
        estadoTextView.setText(estado);

        return listItem;
    }
}

