package com.example.tareasandroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NuevaTareaActivity extends AppCompatActivity {


    NotificationCompat.Builder mBuilder;
    int mNotificationId=1;
    String channelId = "my_channel_01";

    private AdaptadorBBDD dbAdapter;
    private Cursor cursor;

    //
    // Modo del formulario
    //
    private int modo ;

    //
    // Identificador del registro que se edita cuando la opción es MODIFICAR
    //
    private long id ;

    //
    // Elementos de la vista
    //
    private EditText nombre;
    private EditText detalle;
    private EditText fecha;
    private EditText estado;

    private Spinner alarma_hora;
    private Spinner alarma_minuto;

    private FloatingActionButton boton_guardar;
    private FloatingActionButton boton_cancelar;

    //INTENTO DE NOTIFICACION EN BARRA DE ESTADO
    NotificationManager manager;
    Notification myNotication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_tarea);

        this.setTitle("Nueva Tarea");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd--hh:mm:ss", Locale.getDefault());
        Date date = new Date();

        String fecha2 = dateFormat.format(date);

        EditText fechax = (EditText) findViewById(R.id.tarea_fecha_insertar);
        fechax.setText(fecha2);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();

        if (extra == null) return;

        //
        // Obtenemos los elementos de la vista
        //
        nombre = (EditText) findViewById(R.id.nombre2);
        detalle = (EditText) findViewById(R.id.detalle2);
        fecha = (EditText) findViewById(R.id.tarea_fecha_insertar);

        alarma_hora = (Spinner) findViewById(R.id.alarma_hora);
        alarma_minuto = (Spinner) findViewById(R.id.alarma_minuto);

        boton_guardar = (FloatingActionButton) findViewById(R.id.boton_guardar);
        boton_cancelar = (FloatingActionButton) findViewById(R.id.boton_cancelar);


        //LLENAMOS LOS SPINNER DE HORA Y MINUTOS
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                        R.array.values_hora,
                        android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        alarma_hora.setAdapter(adapter);


        ArrayAdapter<CharSequence> adapter2 =
                ArrayAdapter.createFromResource(this,
                        R.array.values_minuto,
                        android.R.layout.simple_spinner_item);

        adapter2.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        alarma_minuto.setAdapter(adapter2);

        //
        // Creamos el adaptador
        //
        dbAdapter = new AdaptadorBBDD(this);
        dbAdapter.abrir();

        //
        // Obtenemos el identificador del registro si viene indicado
        //

        if (extra.containsKey(AdaptadorBBDD.C_COLUMNA_ID))
        {
            id = extra.getLong(AdaptadorBBDD.C_COLUMNA_ID);
            consultar(id);
        }
        //
        // Establecemos el modo del formulario
        //
        establecerModo(extra.getInt(MainActivity.C_MODO));

        //
        // Definimos las acciones para los dos botones
        //
        boton_guardar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                guardar();
            }
        });



        boton_cancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                cancelar();
            }
        });

    }

    private void consultar(Long id)
    {
        //
        // Consultamos los datos, metodo de modo visualizar
        //
        cursor = dbAdapter.getRegistro(id);

        nombre.setText(cursor.getString(cursor.getColumnIndex(AdaptadorBBDD.C_COLUMNA_NOMBRE)));
        detalle.setText(cursor.getString(cursor.getColumnIndex(AdaptadorBBDD.C_COLUMNA_DETALLE)));
        fecha.setText(cursor.getString(cursor.getColumnIndex(AdaptadorBBDD.C_COLUMNA_FECHA)));
        estado.setText(cursor.getString(cursor.getColumnIndex(AdaptadorBBDD.C_COLUMNA_ESTADO)));
        String index = cursor.getString(cursor.getColumnIndex(AdaptadorBBDD.C_COLUMNA_ID));
    }

    private void establecerModo(int m)
    {
        this.modo = m ;

        if (modo == MainActivity.C_VISUALIZAR)
        {
            //this.setTitle(nombre.getText().toString());
            this.setEdicion(false);
        }
        else if (modo == MainActivity.C_CREAR)
        {
            //this.setTitle("Insertar");
            this.setEdicion(true);
        }

    }



    private void setEdicion(boolean opcion)
    {
        nombre.setEnabled(opcion);
        detalle.setEnabled(opcion);
    }

    private void guardar()
    {
        //
        // Obtenemos los datos del formulario
        //
        ContentValues reg = new ContentValues();

        reg.put(AdaptadorBBDD.C_COLUMNA_NOMBRE, nombre.getText().toString());
        reg.put(AdaptadorBBDD.C_COLUMNA_DETALLE, detalle.getText().toString());
        reg.put(AdaptadorBBDD.C_COLUMNA_FECHA, fecha.getText().toString());
        reg.put(AdaptadorBBDD.C_COLUMNA_ESTADO, "Pendiente");
        reg.put(AdaptadorBBDD.C_COLUMNA_ALARMA, alarma_hora.getSelectedItem().toString()+":"+alarma_minuto.getSelectedItem().toString());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss", Locale.getDefault());
        Date date = new Date();

        String fechaid = dateFormat.format(date);


        //GUARDAMOS EN BBDD Y PROCEDEMOS A REGRESAR EL CONTROL

        reg.put(AdaptadorBBDD.C_COLUMNA_ID, fechaid);

        if (modo == MainActivity.C_CREAR)
        {
            dbAdapter.insert(reg);
            Toast.makeText(NuevaTareaActivity.this, "Nueva tarea pendiente", Toast.LENGTH_SHORT).show();
        }




        //
        // Devolvemos el control
        //
        setResult(RESULT_OK);
        finish();
    }


    //INTENTO DE ALARMA
    /*
    private void activar_alarma(){

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

        mBuilder = new NotificationCompat.Builder(this, null);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            CharSequence name = "Notif";
            String description = "Comunicacion de ofertas a usuarios";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, name, importance);

            mChannel.setDescription(description);
            mChannel.enableLights(true);

            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern( new long[]{100,200,300,400,500,400,300,200,400});
            mNotificationManager.createNotificationChannel(mChannel);
        }

        mBuilder.setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentTitle("Titulo")
                .setContentText("Descripcion de notificacion");

        mNotificationManager.notify(mNotificationId,mBuilder.build());


    }
*/


//RETROCEDER CON BOTON ATRAS
    private void cancelar()
    {
        setResult(RESULT_CANCELED, null);
        finish();
    }
}