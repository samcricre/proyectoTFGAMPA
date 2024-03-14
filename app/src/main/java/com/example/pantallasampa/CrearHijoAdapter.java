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

public class CrearHijoAdapter extends ArrayAdapter <Hijo> {

    private Context mContext;
    private ArrayList<Hijo> mHijos;

    public CrearHijoAdapter(Context context, ArrayList <Hijo> hijos){

        super(context,0, hijos);
        mContext = context;
        mHijos = hijos;

    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.cardviewhijos, parent, false);
        }

        Hijo hijo = mHijos.get(position);

        TextView textNombre = listItem.findViewById(R.id.cVNombreHijo);
        textNombre.setText(hijo.getNombre());

        TextView textApellidos = listItem.findViewById(R.id.cVApellidosHijo);
        textApellidos.setText(hijo.getApellidos());

        TextView textEdad = listItem.findViewById(R.id.cVEdadHijo);
        textEdad.setText(hijo.getEdad());

        TextView textCurso = listItem.findViewById(R.id.cVCursoHijo);
        textCurso.setText(hijo.getCurso());


        return listItem;
    }
}
