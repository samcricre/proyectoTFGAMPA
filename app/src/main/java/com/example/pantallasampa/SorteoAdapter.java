package com.example.pantallasampa;

import android.content.Context;
import android.content.Intent;
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

        final Sorteos sorteo = mSorteosList.get(position);

        TextView nombreTextView = listItem.findViewById(R.id.textViewNombreSorteo);
        nombreTextView.setText(sorteo.getNombre());

        TextView descripcionTextView = listItem.findViewById(R.id.textViewDescripcionSorteo);
        descripcionTextView.setText(sorteo.getDescripcion());

        TextView premiosTextView = listItem.findViewById(R.id.textViewPremiosSorteo);
        premiosTextView.setText(sorteo.getPremios());

        TextView estadoTextView = listItem.findViewById(R.id.textViewEstadoSorteo);
        String estado = sorteo.isFinalizado() ? "Finalizado" : "Activo";
        estadoTextView.setText(estado);

        // Agregar OnClickListener para navegar a la página de detalles del sorteo
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PantallaLecturaSorteo.class);
                // Pasa el ID de la noticia a la actividad de detalles del sorteo
                intent.putExtra("codigoNoticia", sorteo.getCodigoNoticia());
                mContext.startActivity(intent);
            }
        });

        return listItem;
    }
}
