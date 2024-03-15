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

public class EventAdapter extends ArrayAdapter<Evento> {

    private Context mContext;
    private ArrayList<Evento> evento;

    public EventAdapter(Context context, ArrayList <Evento> evento){

        super(context,0, evento);
        mContext = context;
        this.evento = evento;

    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.cardvieweventos, parent, false);
        }

        Evento event = evento.get(position);

        TextView eventTitle = listItem.findViewById(R.id.nombreEvento);
        eventTitle.setText(cortarText(event.getTitulo(),15));

        TextView eventDescript = listItem.findViewById(R.id.descripcionTxtBox);
        eventDescript.setText(cortarText(event.getDescripcion(),20));

        TextView eventFechaIni = listItem.findViewById(R.id.fechasIni);
        eventFechaIni.setText(event.getFechaIni());

        TextView eventFechaFin = listItem.findViewById(R.id.fechasFin);
        eventFechaFin.setText(event.getFechaFin());

        TextView eventHoraIni = listItem.findViewById(R.id.horaIni);
        eventHoraIni.setText(event.getHoraIni());

        TextView eventHoraFin = listItem.findViewById(R.id.horaFin);
        eventHoraFin.setText(event.getHoraFin());

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ir a la nueva pantalla por detalles
            }
        });

        return listItem;
    }

    public String cortarText(String txt, int length){
        if (txt.length() > length) {
            return txt.substring(0, length) + "...";
        } else {
            return txt;
        }
    }
}
