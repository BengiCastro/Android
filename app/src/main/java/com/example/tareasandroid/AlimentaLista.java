package com.example.tareasandroid;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class AlimentaLista extends CursorAdapter
{

    private AdaptadorBBDD dbAdapter = null ;

    public AlimentaLista(Context context, Cursor c)
    {
        super(context, c);
        dbAdapter = new AdaptadorBBDD(context);
        dbAdapter.abrir();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView tv = (TextView) view ;
        String estado = cursor.getString(cursor.getColumnIndex(AdaptadorBBDD.C_COLUMNA_ESTADO));

        if(estado.equals("Pendiente")){
            estado=estado+" ✘";
        }
        else estado=estado+" ✔";

        // LLENA LA LISTA CON LOS DATOS DE LA BBDD

        tv.setText(cursor.getString(cursor.getColumnIndex(AdaptadorBBDD.C_COLUMNA_NOMBRE))
                +"\n"+cursor.getString(cursor.getColumnIndex(AdaptadorBBDD.C_COLUMNA_FECHA))
                +" - "+estado);
        //tv.setText("Hola jeje");
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        //DEFINE TIPO DE LISTA DETALLE
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);

        return view;
    }
}
