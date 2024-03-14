package com.example.pantallasampa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.pantallasampa.Correo;
import java.util.List;

public class CorreoAdapter extends ArrayAdapter<Correo> {

    private Context mContext;
    private List<Correo> mCorreos;

    public CorreoAdapter(Context context, List<Correo> correos) {
        super(context, 0, correos);
        mContext = context;
        mCorreos = correos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_listview_correo, parent, false);
        }

        Correo currentCorreo = mCorreos.get(position);

        TextView remitenteTextView = listItem.findViewById(R.id.remitenteTextView);
        remitenteTextView.setText(currentCorreo.getRemitente());

        TextView asuntoTextView = listItem.findViewById(R.id.asuntoTextView);
        asuntoTextView.setText(currentCorreo.getAsunto());

        TextView contenidoTextView = listItem.findViewById(R.id.contenidoTextView);
        contenidoTextView.setText(currentCorreo.getContenido());

        return listItem;
    }
}

