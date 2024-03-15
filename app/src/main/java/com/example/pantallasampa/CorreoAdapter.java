package com.example.pantallasampa;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.pantallasampa.Correo;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CorreoAdapter extends ArrayAdapter<Correo> {

    private Context mContext;
    private List<Correo> mCorreos;

    public CorreoAdapter(Context context, List<Correo> correos) {
        super(context, 0, correos);
        mContext = context;
        mCorreos = correos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_listview_correo, parent, false);
        }

        final Correo currentCorreo = mCorreos.get(position);

        TextView remitenteTextView = listItem.findViewById(R.id.remitenteTextView);
        remitenteTextView.setText(currentCorreo.getRemitente());

        TextView asuntoTextView = listItem.findViewById(R.id.asuntoTextView);
        asuntoTextView.setText(currentCorreo.getAsunto());

        TextView contenidoTextView = listItem.findViewById(R.id.contenidoTextView);
        contenidoTextView.setText(currentCorreo.getContenido());

        TextView fechaTextView = listItem.findViewById(R.id.fechaTextView);
        fechaTextView.setText(formatFecha(currentCorreo.getTimestamp()));

        // Agregar un OnClickListener al listItem
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad de detalle del correo y pasar los datos del correo seleccionado
                Intent intent = new Intent(mContext, PantallaLecturaCorreo.class);
                intent.putExtra("remitente", currentCorreo.getRemitente());
                intent.putExtra("destinatario", currentCorreo.getDestinatario());
                intent.putExtra("asunto", currentCorreo.getAsunto());
                intent.putExtra("contenido", currentCorreo.getContenido());
                intent.putExtra("correoId", currentCorreo.getCorreoId()); // Pasar la clave del correo
                mContext.startActivity(intent);
            }
        });

        return listItem;
    }

    // Método para formatear la fecha en formato legible
    private String formatFecha(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(timestamp);
    }
}
