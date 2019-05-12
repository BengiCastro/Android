package com.example.tareasandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ControladorBBDD extends SQLiteOpenHelper {

    private static int version = 1;
    private static String name = "TareasDb" ;
    private static CursorFactory factory = null;

    public ControladorBBDD (Context context)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.i(this.getClass().toString(), "Creando BBDD SQLite");

        //CREA LA BASE Y LLENA LOS DATOS AL EJECUTARSE POR PRIMERA VEZ

        db.execSQL( "CREATE TABLE TAREAS(" +
                " _id TEXT PRIMARY KEY," +
                " tarea_nombre TEXT NOT NULL, " +
                " tarea_detalle TEXT, " +
                " tarea_estado INTEGER," +
                " tarea_alarma INTEGER," +
                " tarea_fecha TEXT)" );

        db.execSQL( "CREATE UNIQUE INDEX tarea_nombre ON TAREAS(tarea_nombre ASC)" );

        Log.i(this.getClass().toString(), "Tabla TAREAS creada");

        /*
         * Insertamos datos iniciales
         */
        /*db.execSQL("INSERT INTO TAREAS(_id, tarea_nombre) VALUES('1','nota1')");
        db.execSQL("INSERT INTO TAREAS(_id, tarea_nombre) VALUES('2','nota2')");
        db.execSQL("INSERT INTO TAREAS(_id, tarea_nombre) VALUES('3','notax')");
        db.execSQL("INSERT INTO TAREAS(_id, tarea_nombre) VALUES('4','notay')");
        db.execSQL("INSERT INTO TAREAS(_id, tarea_nombre) VALUES('5','nota5')");
        db.execSQL("INSERT INTO TAREAS(_id, tarea_nombre) VALUES('6','nota6')");
        db.execSQL("INSERT INTO TAREAS(_id, tarea_nombre) VALUES('7','nota7')");
        db.execSQL("INSERT INTO TAREAS(_id, tarea_nombre) VALUES('8','nota8')");
        db.execSQL("INSERT INTO TAREAS(_id, tarea_nombre) VALUES('9','nota9')");
        db.execSQL("INSERT INTO TAREAS(_id, tarea_nombre) VALUES('10','nota10')");
        db.execSQL("INSERT INTO TAREAS(_id, tarea_nombre) VALUES('11','nota11')");*/

        db.execSQL("INSERT INTO TAREAS(_id, tarea_nombre, tarea_detalle, tarea_estado, tarea_alarma, tarea_fecha) VALUES('1','Ordenar los papeles','Sacar los papeles del sobre y ordenar segun la fecha','Pendiente','15:30','2019-06-28--11:30:12')");
        db.execSQL("INSERT INTO TAREAS(_id, tarea_nombre, tarea_detalle, tarea_estado, tarea_alarma, tarea_fecha) VALUES('2','Estudiar para el examen','Repasar apartados 1 y 2 del libro de fisica','Realizada','17:30','2019-04-30--06:30:54')");
        db.execSQL("INSERT INTO TAREAS(_id, tarea_nombre, tarea_detalle, tarea_estado, tarea_alarma, tarea_fecha) VALUES('3','Comprar los pikeos','Elegir los necesario para la fiesta y tener todo listo','Pendiente','20:30','2019-05-30--08:55:20')");
        db.execSQL("INSERT INTO TAREAS(_id, tarea_nombre, tarea_detalle, tarea_estado, tarea_alarma, tarea_fecha) VALUES('4','Practicar la canción 2','Descargar la canción para el recital y practicarla','Realizada','16:00','2019-04-08--10:25:50')");
        db.execSQL("INSERT INTO TAREAS(_id, tarea_nombre, tarea_detalle, tarea_estado, tarea_alarma, tarea_fecha) VALUES('5','Reparar mouse','Determinar el error del mouse y repararlo','Pendiente','09:00','2019-05-11--11:30:20')");
        db.execSQL("INSERT INTO TAREAS(_id, tarea_nombre, tarea_detalle, tarea_estado, tarea_alarma, tarea_fecha) VALUES('6','Terminar Software','Revisar los requerimientos dl usuario y terminar trabajo','Pendiente','18:00','2019-05-14--08:30:13')");
        db.execSQL("INSERT INTO TAREAS(_id, tarea_nombre, tarea_detalle, tarea_estado, tarea_alarma, tarea_fecha) VALUES('7','Revisar Requerimientos','Revisar rq para dar conformidad al sw desarrollado','Realizado','19:30','2019-04-11--11:55:20')");


        Log.i(this.getClass().toString(), "Datos iniciales TAREAS insertados");

        Log.i(this.getClass().toString(), "Base de datos creada");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
