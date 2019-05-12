package com.example.tareasandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AdaptadorBBDD {


     // Definimos constante con el nombre de la tabla

    public static final String C_TABLA = "TAREAS" ;


     // Definimos constantes con el nombre de las columnas de la tabla

    public static final String C_COLUMNA_ID   = "_id";
    public static final String C_COLUMNA_NOMBRE = "tarea_nombre";
    public static final String C_COLUMNA_DETALLE = "tarea_detalle";
    public static final String C_COLUMNA_ESTADO = "tarea_estado";
    public static final String C_COLUMNA_ALARMA = "tarea_alarma";
    public static final String C_COLUMNA_FECHA = "tarea_fecha";

    private Context contexto;
    private ControladorBBDD dbHelper;
    private SQLiteDatabase db;


     // Definimos lista de columnas de la tabla para utilizarla en las consultas a la base de datos

    private String[] columnas = new String[]{ C_COLUMNA_ID, C_COLUMNA_NOMBRE, C_COLUMNA_DETALLE, C_COLUMNA_ESTADO, C_COLUMNA_ALARMA, C_COLUMNA_FECHA} ;

    public AdaptadorBBDD(Context context)
    {
        this.contexto = context;
    }

    public AdaptadorBBDD abrir() throws SQLException
    {
        dbHelper = new ControladorBBDD(contexto);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void cerrar()
    {
        dbHelper.close();
    }


     // Devuelve cursor con todos las columnas de la tabla

    public Cursor getCursor() throws SQLException
    {
        Cursor c = db.query( true, C_TABLA, columnas, null, null, null, null, null, null);

        return c;
    }


     // Devuelve cursor con todos las columnas del registro

    public Cursor getRegistro(long id) throws SQLException
    {
        Cursor c = db.query( true, C_TABLA, columnas, C_COLUMNA_ID + "=" + id, null, null, null, null, null);

        //Nos movemos al primer registro de la consulta
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    // Inserta nuevos registros
    public long insert(ContentValues reg)
    {
        if (db == null)
            abrir();

        return db.insert(C_TABLA, null, reg);
    }

    // Elimina tareas segun id
    public long delete(long id)
    {
        if (db == null)
            abrir();

        return db.delete(C_TABLA, "_id=" + id, null);
    }

    //Actualiza estado de tareas
    public long update(long id)
    {
        long result = 0;

        ContentValues reg = new ContentValues();
        reg.put(AdaptadorBBDD.C_COLUMNA_ESTADO, "Realizada");

        if (db == null)
            abrir();

        result = db.update(C_TABLA, reg, "_id=" + id, null);
        return result;
    }

    //Permite hacer las muestras con filtros
    public Cursor getCursor(String filtro, String filtro_fechas) throws SQLException
    {
        Cursor c = db.query( true, C_TABLA, columnas, filtro, null, null, null,filtro_fechas, null);

        return c;
    }

}
